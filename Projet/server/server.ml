exception Fin
exception BadRequest
exception AlreadyExists
exception Disconnection

let _ = Random.self_init ()

(* OK pour les mutables, pas besoin de les passer en paramètre *)

let mutex_players_list = Mutex.create () (*sync for access to *)

let cond_least1player = Condition.create ()
let maxspeed = 5.0
let turnit = 45.0
let thrustit = 2.0
let server_tickrate = 10 (* le serveur envoie server_tickrate fois par seconde *)
let server_refresh_tickrate = 40.0
let waiting_time = 10
let obj_radius = 20.0
let ve_radius = 30.0
let ob_radius = 50.0
let demil = 450.0
let demih = 350.0



type vehicule = {
	mutable position: float * float;
	mutable direction: float;
	mutable speed: float * float
}

type player = {
    name: string;
    socket: Unix.file_descr;
    inchan: in_channel;
    outchan: out_channel;
    mutable score: int;
    car : vehicule;
	}

type session = {
	mutable players_list : player list;
    mutable playing : bool;
    mutable target: (float * float) option;
	win_cap : int;
	mutable obstacles_list : (float * float) list
}


(* modifier dans le serveur : ajouter la vérif de radius_obj à chaque calcul de nouvelle position : si ok touché -> incre score et envoyer newobj  *)

(**************************** ADDITIONNAL FUNCTIONS *****************************)



let get_distance (x1,y1) (x2,y2) =
	sqrt((x2-.x1)*.(x2-.x1)+.(y2-.y1)*.(y2-.y1))

let alea_x () =
	(Random.float demil*.2.0) -.demil
let alea_y () =
	(Random.float demih*.2.0) -.demih

let alea_pos () =
	(alea_x (),alea_y ())

let current_session =
	{ players_list = [];
		playing = false;
		target = None;
		win_cap = 3;
		obstacles_list = []
  }


let parse_cmd cmd_string =
	let s = Str.split (Str.regexp "A\\|T") cmd_string in
		(float_of_string (List.nth s 0), float_of_string (List.nth s 1))

let parse_request req_string =
  let s = Str.split (Str.regexp "/") req_string in
  if (List.length s) > 0 then s else raise BadRequest

let parse_coord c_string =
	let lcoord = Str.split (Str.regexp "X\\|Y") c_string in
		(float_of_string (List.nth lcoord 0),float_of_string (List.nth lcoord 1))

let rec stringify_scores p_list =
	 match p_list with
	  |hd::[] -> hd.name^":"^(string_of_int hd.score)^"/"
	 	|hd::tl -> hd.name^":"^(string_of_int hd.score)^"|"^(stringify_scores tl)
		|[] -> "" (* n'arrivera jamais juste pour la complétude du pattern matching*)


let stringify_coord_opt target =
    match target with
    |Some(x,y) -> "X"^(string_of_float x)^"Y"^(string_of_float y)
    |None -> "XY"

let stringify_coord (x,y) =
	"X"^(string_of_float x)^"Y"^(string_of_float y)

let stringify_speed (vx,vy) =
	"VX"^(string_of_float vx)^"VY"^(string_of_float vy)

(* angle en radian sur le sujet, à decider ici si degre ou radian *)
let stringify_angle a =
	"T"^(string_of_float a)

let rec stringify_coords p_list = (* string du TICK pour la partie A : seulement les positions *)
	match p_list with
	|hd::[] -> hd.name^":"^(stringify_coord hd.car.position)^"/"
	|hd::tl -> hd.name^":"^(stringify_coord hd.car.position)^"|"^(stringify_coords tl)
	|[] -> ""

let rec stringify_tick p_list = (* string du TICK pour la partie B : avec les vitesses et l'angle *)
	match p_list with
	|hd::[] -> hd.name^":"^(stringify_coord hd.car.position)^(stringify_speed hd.car.speed)^(stringify_angle hd.car.direction)^"/"
	|hd::tl -> hd.name^":"^(stringify_coord hd.car.position)^(stringify_speed hd.car.speed)^(stringify_angle hd.car.direction)^"|"^(stringify_tick tl)
	|[] -> ""

let rec stringify_coordsXY o_list = (* nouveau strindigy pour les obstacles car ne peut réutiliser le stringify_coords qui s'appliquent aux joueurs *)
    match o_list with
    |hd::[] -> (stringify_coord hd)^"/"
    |hd::tl -> (stringify_coord hd)^"|"^(stringify_coordsXY tl)
    |[] -> ""

let find_player user_name =
	List.find (fun p -> if p.name=user_name then true else false) current_session.players_list

let exists_player user_name =
	List.exists (fun p -> if p.name=user_name then true else false) current_session.players_list

let is_colliding_obs player_pos obs =
    if (get_distance player_pos obs) < (ve_radius+.ob_radius) then true else false

let rec get_valid_pos () =
    let player_pos = alea_pos() in
    if List.exists (is_colliding_obs player_pos) current_session.obstacles_list
    then get_valid_pos ()
    else player_pos

(*permet de créer un joueur*)
let create_player user sock inc out =
	{ name = user;
  	socket = sock;
    inchan = inc;
    outchan = out;
    score = 0;
    car = {position=get_valid_pos();direction=0.0;speed=(0.0,0.0)};
	}


let init_players () =
	let f p =
	    p.car.position <- alea_pos() ;
	    p.car.speed <- (0.0,0.0);
	    p.car.direction <- 0.0;
	    p.score <- 0
	in
	List.iter f current_session.players_list


let get_new_obstacles n =
    let rec aux_get nb =
        match nb with
        |0 -> []
        |x -> alea_pos()::aux_get (x-1)
     in aux_get n

let get_val_target () =
    match current_session.target with
    |Some(x,y) -> (x,y)
    |None -> failwith "No target to compare"

(********************** SENDING FUNCTIONS ***********************)
let send_session () =
	let send_fun coords c_target c_obstacles p =
		output_string p.outchan ("SESSION/"^coords^c_target^"/"^c_obstacles^"\n");
		flush p.outchan
	in
	let coords = stringify_coords current_session.players_list
    and co_target = stringify_coord_opt current_session.target
	and coords_obs = stringify_coordsXY current_session.obstacles_list in
		List.iter (send_fun coords co_target coords_obs) current_session.players_list

let send_welcome user_name =
	Mutex.lock mutex_players_list;
	let phase = if current_session.playing then "jeu/" else "attente/"
	and scores = stringify_scores current_session.players_list
	and coord_target = stringify_coord_opt current_session.target
	and coords_obs = stringify_coordsXY current_session.obstacles_list
	and player = find_player user_name
	in
	print_endline phase;
	print_endline scores;
	print_endline coord_target;
	print_endline coords_obs;
	output_string player.outchan ("WELCOME/"^phase^scores^coord_target^"/"^coords_obs^"\n");
	flush player.outchan;
	if current_session.playing then
	begin
		let coords = stringify_coords current_session.players_list in
			output_string player.outchan ("SESSION/"^coords^coord_target^"/"^coords_obs^"\n");
            flush player.outchan
	end;
	Mutex.unlock mutex_players_list

let send_newplayer user_name = (* donne seulement le nom du nouveau joueur, le client attend le tick pour placer le joueur sur le canvas *)
	let send_fun p =
		if p.name<>user_name then
		begin
		output_string p.outchan ("NEWPLAYER/"^user_name^"/\n");
		flush p.outchan
		end
		else ()
	in
	List.iter send_fun current_session.players_list

let send_playerleft user_name =
	let send_fun p =
		output_string p.outchan ("PLAYERLEFT/"^user_name^"/\n");
		flush p.outchan
	in
	Mutex.lock mutex_players_list;
	List.iter send_fun current_session.players_list;
	Mutex.unlock mutex_players_list

(*  Fin de la session courante, scores finaux de la session. protégé par le mutex de l'appelant *)
let send_winner () =
	let send_fun scores p =
		output_string p.outchan ("WINNER/"^scores^"/\n");
		flush p.outchan
	in
	let f_scores = stringify_scores current_session.players_list in
		List.iter (send_fun f_scores) current_session.players_list

(* protégé par le mutex de l'appelant *)
let send_tick () =
	let send_fun to_send p =
		output_string p.outchan ("TICK/"^to_send^"\n");
		flush p.outchan
	in
	(* print_endline "Envoie à tous les joueurs, premier joueur dans la liste : %s\n"; *)
	(* print_endline (List.hd current_session.players_list).name; *)
	let f_coords = stringify_tick current_session.players_list in
		List.iter (send_fun f_coords) current_session.players_list

(* protégé par le mutex de l'appelant *)
let send_newobj () =
    print_endline "dans send_newobj";
	let send_fun coord scores p =
		output_string p.outchan ("NEWOBJ/"^coord^"/"^scores^"/\n");
		flush p.outchan
	in
	let coord = stringify_coord_opt current_session.target
	and scores = stringify_scores current_session.players_list in
		List.iter (send_fun coord scores) current_session.players_list

(*let send_denied chan =
	output_string chan "DENIED/";
	flush chan *)

let send_mess message =
	let send_fun to_send p =
		output_string p.outchan ("RECEPTION/"^to_send^"\n");
		flush p.outchan
	in
	List.iter (send_fun message) current_session.players_list

let send_mess_from message from=
	let send_fun to_send p =
		if p.name<>from then
		begin
		output_string p.outchan ("RECEPTION/"^to_send^"/"^from^"\n");
		flush p.outchan
		end
		else ()
	in
	List.iter (send_fun message) current_session.players_list

let send_pmess user_name message from_user=
	let player = find_player user_name in
		print_endline player.name;
		print_endline from_user;
		print_endline message;
		output_string player.outchan ("PRECEPTION/"^message^"/"^from_user^"\n");
		flush player.outchan

let start_session () =
	Mutex.lock mutex_players_list;
	current_session.obstacles_list <- get_new_obstacles 5;
	while (List.length current_session.players_list = 0) do
		(* peut être pas besoin de boucle *)
		Condition.wait cond_least1player mutex_players_list
	done;
	Mutex.unlock mutex_players_list;
	Unix.sleep waiting_time;
	Mutex.lock mutex_players_list;
	if (List.length current_session.players_list) > 0 then
	begin
	current_session.playing <- true;
	send_session ()
	end
	else ();
	Mutex.unlock mutex_players_list


(*********************** RESTARTING SESSION **************************)
(* utilisation de cette fonction suite à un gagnant de session *)
(* normalement il n'y a qu'un thread client qui a accès à cette fonction à un moment *)
let restart_session () =
	Mutex.lock mutex_players_list;
	current_session.playing <- false;
	while (List.length current_session.players_list = 0) do
		(* peut être pas besoin de boucle *)
		Condition.wait cond_least1player mutex_players_list
	done;
	init_players ();
	current_session.target <- Some(get_valid_pos());
	current_session.obstacles_list <- get_new_obstacles 5;
	Mutex.unlock mutex_players_list;
	(* le mutex est rendu pour que d'autres clients puissent se connecter entre-temps *)
	Unix.sleep waiting_time;
	(* Mutex.lock mutex_players_list; *)
	Mutex.lock mutex_players_list;
	current_session.playing <- true;
	send_session ();
	Mutex.unlock mutex_players_list


(********************** PROCESSING FUNCTIONS ***********************)


let maybe_target_reached player =
    Mutex.lock mutex_players_list;
    let target_coord = get_val_target() in
    if (get_distance target_coord player.car.position <= obj_radius) then
        (* le joueur a touché l'objectif *)
        begin
        Printf.printf "joueur qui touche target : %s \n" player.name;
        print_endline "le joueur a touché le target";
        player.score <- player.score+1;
        if (player.score = current_session.win_cap) then
            (* le joueur a atteint win_cap : send_winner & restart_session *)
            begin
            send_winner ();
            Mutex.unlock mutex_players_list;
            (* besoin d'exécuter restart hors SC car unix.sleep à l'intérieur *)
            restart_session ()
            end
        else
            (* nouvel objectif : send_newobj *)
            begin
            current_session.target <- Some(get_valid_pos());
            send_newobj ();
            Mutex.unlock mutex_players_list;
            end
        end
    else Mutex.unlock mutex_players_list

let checked_vx newvx =
    if newvx > maxspeed then maxspeed
    else if newvx < -.maxspeed then -.maxspeed else newvx

let checked_vy newvy =
    if newvy > maxspeed then maxspeed
    else if newvy < -.maxspeed then -.maxspeed else newvy

(* compute_cmd calcul les nouvelles donnnées pour le joueur, et le stock en mémoire *)
let compute_cmd player (angle,pousse) =
		player.car.direction <- mod_float (player.car.direction+.angle) (2.0*.Float.pi);
		let new_vx = (fst player.car.speed) +. ((thrustit *. cos player.car.direction) *. pousse)
		and new_vy = (snd player.car.speed) +. ((thrustit *. sin player.car.direction) *. pousse) in
		player.car.speed <- (checked_vx new_vx,checked_vy new_vy);
		let new_x = (fst player.car.position) +. (fst player.car.speed)
		and new_y = (snd player.car.position) +. (snd player.car.speed) in
		player.car.position<-(new_x,new_y);
		if new_x > demil then player.car.position <- ((-.demil)+.(mod_float (fst player.car.position) demil),
													    snd player.car.position);
		if new_y > demih then player.car.position <- (fst player.car.position,
													    (-.demih)+.((snd player.car.position)-.demih));
		if new_x < -.demil then player.car.position <- (demil-.(mod_float (fst player.car.position) demil),
														snd player.car.position);
		if new_y < -.demih then player.car.position <- (fst player.car.position,
														demih-.(mod_float (snd player.car.position) demih))

let check_collisions player =
    let check_collision_obstacle o =
        if (get_distance player.car.position o)<(ob_radius+.ve_radius)
        then player.car.speed <- (-.(fst player.car.speed),-.(snd player.car.speed))  (* probleme : si je "touche" 2 obstacles, il ne se passe rien, tout continu normalement puisque les signges se seront inversé 2 fois *);
     and check_collision_players other_player =
        if (player.name <> other_player.name)
        then
            if (get_distance player.car.position other_player.car.position)<(ve_radius*.2.0)
            then
                (player.car.position <- (-.(fst player.car.speed),-.(snd player.car.speed));
                other_player.car.position <- (-.(fst other_player.car.speed),-.(snd other_player.car.speed)))
    in
    List.iter check_collision_obstacle current_session.obstacles_list;
    List.iter check_collision_players current_session.players_list


let process_exit user_name =
	try
		begin
		Mutex.lock mutex_players_list;
		let player = find_player user_name in
		current_session.players_list <- List.filter (fun p -> p.name<>user_name) current_session.players_list;
		if (List.length current_session.players_list) = 0 then (* dernier joueur*)
		begin
		current_session.playing <- false;
		ignore (Thread.create start_session ());
		end;
		Mutex.unlock mutex_players_list;
		(*ferme le thread*)
		(* close_in player.inchan; *)
		(* close_out player.outchan; *)
		Unix.close player.socket;
		send_playerleft player.name
		end
	with Not_found -> print_endline "Le joueur n'est plus dans la liste"

let process_newpos coord user_name =
		Mutex.lock mutex_players_list;
		if (current_session.playing) then
			let player = find_player user_name
			and parsed_coord = parse_coord coord in
			player.car.position <- parsed_coord;
			let coord_target = get_val_target() in
			if (get_distance coord_target parsed_coord <= obj_radius) then
				(* le joueur a touché l'objectif *)
				begin
				print_endline "le joueur a touché le target";
				player.score <- player.score+1;
				if (player.score = current_session.win_cap) then
					(* le joueur a atteint win_cap : send_winner & restart_session *)
					begin
					send_winner ();
					Mutex.unlock mutex_players_list;
					(* besoin d'exécuter restart hors SC car unix.sleep à l'intérieur *)
					restart_session ()
					end
				else
					(* nouvel objectif : send_newobj *)
					begin
					current_session.target <- Some(get_valid_pos());
					send_newobj ();
					Mutex.unlock mutex_players_list
					end
				end
			else Mutex.unlock mutex_players_list
		else Mutex.unlock mutex_players_list


let process_newcom cmd_string user_name =
	let player = find_player user_name in
		(* print_endline cmd_string; *)
		Mutex.lock mutex_players_list;
		compute_cmd player (parse_cmd cmd_string); (* met a jour les (vx,vy) du joueur user_name e et refresh les données du client  *)
		check_collisions player;
		send_tick ();
		Mutex.unlock mutex_players_list;
		maybe_target_reached player

let process_envoi message =
	Mutex.lock mutex_players_list;
	send_mess message;
	Mutex.unlock mutex_players_list

let process_envoi_from message from =
	Mutex.lock mutex_players_list;
	send_mess_from message from;
	Mutex.unlock mutex_players_list

let process_penvoi user_name message from_user =
	Mutex.lock mutex_players_list;
	send_pmess user_name message from_user;
	Mutex.unlock mutex_players_list

(********************** thread's looping  ***********************)
let receive_req user_name =
	let player = find_player user_name in
	try
		while true do
			let request = input_line player.inchan in
			let parsed_req = parse_request request in
			try
				match List.hd parsed_req with
				|"EXIT" -> if List.length parsed_req < 2 then raise BadRequest;
									 process_exit (List.nth parsed_req 1);
									 raise Disconnection
				|"NEWPOS" -> if List.length parsed_req < 2 then raise BadRequest;
										 process_newpos (List.nth parsed_req 1) user_name
				|"NEWCOM" -> if List.length parsed_req < 2 then raise BadRequest;
											process_newcom (List.nth parsed_req 1) user_name
				|"ENVOI" -> if List.length parsed_req < 2 then raise BadRequest;
                            if List.length parsed_req == 2 then (* envoi générique avec seulement le message *)
								process_envoi (List.nth parsed_req 1)
							else
							    process_envoi_from (List.nth parsed_req 1) (List.nth parsed_req 2) (* envoie spécifique, notre version avec le nom *)
				|"PENVOI" ->	if List.length parsed_req <> 3 then raise BadRequest;
											process_penvoi (List.nth parsed_req 1) (List.nth parsed_req 2) user_name
				|_ -> raise BadRequest
			with BadRequest -> output_string player.outchan "DENIED/BadRequest\n";
										 			flush player.outchan
		done
	with Disconnection -> ()



(* let tick_thread () =
	while true do
		Unix.sleep server_tickrate;
		Mutex.lock mutex_players_list;
		if current_session.playing then
			begin
			send_tick ()
			end;
		Mutex.unlock mutex_players_list
	done *)



let server_refresh_tick_thread () =
	let refresh p =
	    Mutex.lock mutex_players_list;
	    p.car.position <- (fst p.car.position+.(fst p.car.speed),snd p.car.position+.(snd p.car.speed));
	    Mutex.unlock mutex_players_list;
        maybe_target_reached p;
	in
		while true do
			Unix.sleepf (1.0/.server_refresh_tickrate);
			if current_session.playing then
				List.iter refresh current_session.players_list;
				List.iter check_collisions current_session.players_list
		done



(********************** PROCESSING NEW CONNECTION ********************)

(* les opérations de verification et d'ajout du player dans la liste
 doivent être dans le même bloc mutex, sinon un autre client peut potentiellement
 s'être inséré entre la vérif et l'ajout de ce client  *)
let process_connect user_name client_socket inchan outchan =
	Mutex.lock mutex_players_list;
	if exists_player user_name then
		begin
		Mutex.unlock mutex_players_list;
		raise AlreadyExists (* est ce qu'il faut libérer avant de raise ? *)
		end
	else
		begin
		let player = (create_player user_name client_socket inchan outchan) in
		current_session.players_list<-player::current_session.players_list;
		(* print_endline user_name; *)
		(* print_endline "le joueur dont le nom est au dessus a été ajouté a la liste\n"; *)
		Condition.signal cond_least1player
		end;
	Mutex.unlock mutex_players_list;
	if current_session.target = None then current_session.target <- Some(get_valid_pos());
	send_welcome user_name;
	send_newplayer user_name;
	receive_req user_name



(********************** FIRST CONNECTION ***********************)
(* c'est ce thread qui est lancé lorsqu'un client se connecte *)
let start_new_client client_socket =
	let inchan = Unix.in_channel_of_descr client_socket
	and outchan = Unix.out_channel_of_descr client_socket in
	let rec try_connect_loop () =
		begin
		let request = input_line inchan in
			let parsed_req = parse_request request in
					try
						match List.hd parsed_req with
							| "CONNECT" -> if (List.length parsed_req) < 2 then raise BadRequest;
							               process_connect (List.nth parsed_req 1) client_socket inchan outchan
							| _ -> raise BadRequest
					with
						|BadRequest -> output_string outchan "DENIED/BadRequest\n";
									   			 flush outchan;
										 		 	 try_connect_loop ()
						|AlreadyExists -> output_string outchan "DENIED/AlreadyExists\n";
									   				  flush outchan;
										 					try_connect_loop ()
		end
	in try_connect_loop ()



(********************** SERVER STARTING ***********************)
(* fonction de lancement du serveur : à chaque nouvelle connexion
 		lance un thread sur start_new_client sur le socket du client *)
let start_server nb_c =
  let server_socket = Unix.socket Unix.PF_INET SOCK_STREAM 0
  and addr = Unix.inet_addr_of_string "127.0.0.1" in
  begin
    Unix.bind server_socket (Unix.ADDR_INET(addr, 2019));
    Unix.listen server_socket nb_c;
		ignore (Thread.create start_session ());
		(* ignore (Thread.create tick_thread ());  *)
		ignore (Thread.create server_refresh_tick_thread ());
		while true do
      let (client_socket, _) = Unix.accept server_socket in
      Unix.setsockopt client_socket Unix.SO_REUSEADDR true;
      	print_endline "Nouvelle connexion socket";
        ignore (Thread.create start_new_client client_socket);
    done
  end;;

(* le nombre maximum de client est donné en paramètre de lu lancement server *)
start_server (int_of_string Sys.argv.(1));;
