exception Fin
exception BadRequest
exception AlreadyExists

(* OK pour les mutables, pas besoin de les passer en paramètre *)

let mutex_players_list = Mutex.create () (*sync for access to *)

let cond_least1player = Condition.create ()

let server_tickrate = 10 (* le serveur envoie un tick toutes les server_tickrate secondes *)
let waiting_time = 20
let obj_radius = 0.05
let l = 200.0
let h = 150.0

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
    car : vehicule
    (*mutable playing: status*)
	}

type session = {
		mutable players_list : player list;
    mutable playing : bool;
    mutable target: float * float;
		win_cap : int
}



let current_session =
{ players_list = [];
		playing = false;
		target = (0.0,0.0);
		win_cap = 10
  }

(**************************** ADDITIONNAL FUNCTIONS *****************************)

let parse_request req_string =
  let s = Str.split (Str.regexp "/") req_string in
  if (List.length s) > 0 then s else raise BadRequest

let parse_coord c_string =
	let lcoord = Str.split (Str.regexp "X\|Y") c_string in
		(float_of_string (List.nth lcoord 0),float_of_string (List.nth lcoord 1))

let rec stringify_scores p_list =
	 match p_list with
	  |hd::[] -> hd.name^":"^(string_of_int hd.score)^"/"
	 	|hd::tl -> hd.name^":"^(string_of_int hd.score)^"|"^(stringify_scores tl)
		|[] -> "" (* n'arrivera jamais juste pour la complétude du pattern matching*)


let stringify_coord (x,y) =
	"X"^(string_of_float x)^"Y"^(string_of_float y)

let rec stringify_coords p_list =
	match p_list with
	|hd::[] -> hd.name^":"^(stringify_coord hd.car.position)^"/"
	|hd::tl -> hd.name^":"^(stringify_coord hd.car.position)^"|"^(stringify_coords tl)
	|[] -> "" (* n'arrivera jamais juste pour la complétude du pattern matching*)

let find_player user_name =
	List.find (fun p -> if p.name=user_name then true else false) current_session.players_list

let exists_player user_name =
	List.exists (fun p -> if p.name=user_name then true else false) current_session.players_list

let get_distance (x1,y1) (x2,y2) =
	sqrt((x2-.x1)*.(x2-.x1)+.(y2-.y1)*.(y2-.y1))

let alea_x () =
	(Random.float (-.l)) +. Random.float l

let alea_y () =
	(Random.float (-.h)) +. Random.float h

let alea_pos () =
	(alea_x (),alea_y ())

let initposplayers () =
	let f p = p.car.position <- alea_pos() in
	List.iter f current_session.players_list

	(*permet de créer un joueur*)
	let create_player user sock inc out =
		(* ajouter le comportement random de la position *)
		{ name = user;
	  	socket = sock;
	    inchan = inc;
	    outchan = out;
	    score = 0;
	    car = {position=alea_pos();direction=0.0;speed=(0.0,0.0)}
		}
(********************** SENDING FUNCTIONS ***********************)
let send_welcome user_name =
	Mutex.lock mutex_players_list;
	let phase = if current_session.playing then "playing/" else "waiting/"
	and scores = stringify_scores current_session.players_list
	and coord = stringify_coord current_session.target
	and player = find_player user_name
	in
	output_string player.outchan ("WELCOME/"^phase^scores^coord^"/\n");
	flush player.outchan;
	Mutex.unlock mutex_players_list

let send_newplayer user_name =
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


let send_session () =
	let send_fun coords c_target p =
		output_string p.outchan ("SESSION/"^coords^"/"^c_target^"/\n");
		flush p.outchan
	in
	let coords = stringify_coords current_session.players_list in
		let coord_target = stringify_coord current_session.target in
			List.iter (send_fun coords coord_target) current_session.players_list


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
	let send_fun coords p =
		output_string p.outchan ("TICK/"^coords^"/\n");
		flush p.outchan
	in
	let f_coords = stringify_coords current_session.players_list in
		List.iter (send_fun f_coords) current_session.players_list


(* protégé par le mutex de l'appelant *)
let send_newobj () =
	let send_fun coord scores p =
		output_string p.outchan ("NEWOBJ/"^coord^"/"^scores^"/\n");
		flush p.outchan
	in
	let coord = stringify_coord current_session.target
	and scores = stringify_scores current_session.players_list in
		List.iter (send_fun coord scores) current_session.players_list


(*let send_denied chan =
	output_string chan "DENIED/";
	flush chan *)


(*********************** RESTARTING SESSION **************************)

(* normalement il n'y a qu'un thread client qui a accès à cette fonction à un moment *)
let restart_session () =
	Mutex.lock mutex_players_list;
	current_session.playing <- false;
	while (List.length current_session.players_list = 0) do
		(* peut être pas besoin de boucle *)
		Condition.wait cond_least1player mutex_players_list
	done;
	initposplayers ();
	current_session.target <- alea_pos ();
	Mutex.unlock mutex_players_list;
	(* le mutex est rendu pour que d'autres clients puissent se connecter entre-temps *)
	Unix.sleep waiting_time;
	(* Mutex.lock mutex_players_list; *)
	current_session.playing <- true;
	send_session ()

(********************** PROCESSING FUNCTIONS ***********************)

let process_exit user_name =
	try
		begin
		Mutex.lock mutex_players_list;
		let player = find_player user_name in
		current_session.players_list <- List.filter (fun p -> p.name=user_name) current_session.players_list;
		Mutex.unlock mutex_players_list;
		(*ferme le thread*)
		close_in player.inchan;
		close_out player.outchan;
		Unix.close player.socket;
		send_playerleft player.name
		end
	with Not_found -> print_endline "Le joueur n'est plus dans la liste"


let process_newpos coord user_name =
		Mutex.lock mutex_players_list;
		if (current_session.waiting) then
			let player = find_player user_name
			and parsed_coord = parse_coord coord in
			player.car.position <- parsed_coord;
			let (x,y) = parsed_coord in
			print_endline "yo";
			Printf.printf "(%f,%f)\n" x y;
			print_endline "yop";
			if (get_distance current_session.target parsed_coord <= obj_radius) then
				(* le joueur a touché l'objectif *)
				begin
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
					current_session.target <- alea_pos ();
					send_newobj ();
					Mutex.unlock mutex_players_list
					end
				end
			else Mutex.unlock mutex_players_list
		else Mutex.unlock mutex_players_list



(********************** thread's looping  ***********************)
let receive_req user_name =
	let player = find_player user_name in
		while true do
			let request = input_line player.inchan in
			let parsed_req = parse_request request in
				match List.hd parsed_req with
				|"EXIT" -> process_exit (List.nth parsed_req 1);Thread.exit ()
				|"NEWPOS" -> process_newpos (List.nth parsed_req 1) user_name
				|_ -> raise BadRequest
		done

let tick_thread () =
	while true do
		Unix.sleep server_tickrate;
		Mutex.lock mutex_players_list;
		if current_session.playing then
			begin
			send_tick ()
			end;
		Mutex.unlock mutex_players_list
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
		Condition.signal cond_least1player
		end;
	Mutex.unlock mutex_players_list;
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
							| "CONNECT" -> process_connect (List.nth parsed_req 1) client_socket inchan outchan;
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
    while true do
      let (client_socket, _) = Unix.accept server_socket in
      	print_endline "Nouvelle connexion\n";
        ignore (Thread.create start_new_client client_socket);
    done
  end;;

(* le nombre maximum de client est donné en paramètre de lu lancement server *)
start_server (int_of_string Sys.argv.(1));;
