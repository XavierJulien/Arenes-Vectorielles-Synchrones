(* Exceptions *)
exception Fin
exception BadRequest
exception AlreadyExists
exception Disconnection

<<<<<<< Updated upstream

=======
let _ = Random.self_init ()

let mutex_players_list = Mutex.create () (*sync for access to *)
>>>>>>> Stashed changes

(* Variables  *)
let mutex_players_list = Mutex.create () 
let cond_least1player = Condition.create ()
let win_cap = 3
let maxspeed = 5.0
let turnit = 45.0
let thrustit = 2.0
let server_tickrate = 10 
let server_refresh_tickrate = 30.0
let waiting_time = 10
let obj_radius = 30.0
let ve_radius = 30.0
let ob_radius = 50.0
let pi_radius = 20.0
let la_radius = 20.0
let demil = 450.0
let demih = 350.0
<<<<<<< Updated upstream
let _ = Random.self_init ()
=======
let nb_targets = int_of_string Sys.argv.(2) (*1 pour le mode de jeu normal, > 1 pour le mode de jeu course*)
let nb_obstacles = int_of_string Sys.argv.(3)

>>>>>>> Stashed changes

(* Types *)
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
    car: vehicule;
    mutable is_colliding: bool;
    mutable is_colliding_ve: bool;
    mutable nb_reach: int
	}

type session = {
	mutable players_list : player list;
    mutable playing : bool;
    mutable targets_list: (float * float) list option;
	mutable obstacles_list : (float * float) list;
	mutable pieges_list : (float * float ) list;
	mutable lasers_list : vehicule list;
}

let current_session =
	{ players_list = [];
		playing = false;
		target = None;
		win_cap = 3;
		obstacles_list = [];
		pieges_list = [];
		lasers_list = []
  	}

(* modifier dans le serveur : ajouter la vérif de radius_obj à chaque calcul de nouvelle position : si ok touché -> incre score et envoyer newobj  *)

(********************** AUXILLARY FUNCTIONS ***********************)
let alea_x () = (Random.float demil*.2.0) -.demil
let alea_y () = (Random.float demih*.2.0) -.demih
let alea_pos () = (alea_x (),alea_y ())
let alea_angle () = (Random.float (Float.pi *. 2.0))
let alea_vxy () =
	let random_chooser = (Random.int 2) in
	if random_chooser == 1 then maxspeed else -.maxspeed

let alea_speed () = (alea_vxy (),alea_vxy ())
let find_player user_name = List.find (fun p -> if p.name=user_name then true else false) current_session.players_list
let exists_player user_name = List.exists (fun p -> if p.name=user_name then true else false) current_session.players_list
let get_distance (x1,y1) (x2,y2) = sqrt((x2-.x1)*.(x2-.x1)+.(y2-.y1)*.(y2-.y1))
let not_colliding_obs position obs = if (get_distance position obs) > (ve_radius+.ob_radius) then true else false
let rec get_valid_pos () =
    let position = alea_pos() in
    if List.for_all (not_colliding_obs position) current_session.obstacles_list
    then position
    else get_valid_pos () (*la position n'est pas valide*)

<<<<<<< Updated upstream
let create_laser pos angle vitesse= 
	{
		position=pos;
		direction=angle;
		speed=vitesse;
	}
let create_player user sock inc out =
	{ name = user;
  	socket = sock;
    inchan = inc;
    outchan = out;
    score = 0;
    car = {position=get_valid_pos();direction=0.0;speed=(0.0,0.0)};
    is_colliding = false;
    is_colliding_ve = false
	}
let init_players () =
	let f p =
	    p.car.position <- get_valid_pos() ;
	    p.car.speed <- (0.0,0.0);
	    p.car.direction <- 0.0;
	    p.score <- 0;
	    p.is_colliding <- false;
	    p.is_colliding_ve <- false
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
    |None -> failwith "No target"
=======
let current_session =
	{ players_list = [];
		playing = false;
		targets_list = None;
		obstacles_list = [];
		pieges_list = []
  	}
>>>>>>> Stashed changes

let remove_piege p =
	let rec remove l =
		match l with
		| hd::[] -> if (hd==p) then [] else hd::[]
		| hd::tl ->	if (hd==p) then tl else hd::(remove tl)
		| [] -> []
	in
		current_session.pieges_list <- remove current_session.pieges_list
let remove_laser p =
	let rec remove l =
		match l with
		| hd::[] -> if (hd==p) then [] else hd::[]
		| hd::tl ->	if (hd==p) then tl else hd::(remove tl)
		| [] -> []
	in
		(current_session.lasers_list <- remove current_session.lasers_list)
let checked_vx newvx =
    if newvx > maxspeed then maxspeed
    else if newvx < -.maxspeed then -.maxspeed else newvx

let checked_vy newvy =
    if newvy > maxspeed then maxspeed
    else if newvy < -.maxspeed then -.maxspeed else newvy

(********************** PARSING FUNCTIONS ***********************)
let parse_cmd cmd_string =
	let s = Str.split (Str.regexp "A\\|T") cmd_string in
		(float_of_string (List.nth s 0), float_of_string (List.nth s 1))

let parse_request req_string =
  let s = Str.split (Str.regexp "/") req_string in
  if (List.length s) > 0 then s else raise BadRequest

let parse_coord c_string =
	let lcoord = Str.split (Str.regexp "X\\|Y") c_string in
		(float_of_string (List.nth lcoord 0),float_of_string (List.nth lcoord 1))
let parse_vcoord c_string =
	let lcoord = Str.split (Str.regexp "VX\\|VY") c_string in
		(float_of_string (List.nth lcoord 0),float_of_string (List.nth lcoord 1))
let parse_laser laser = 
	let laser_list = Str.split (Str.regexp "X\\|Y\\|A\\|VX\\|VY") laser in
		create_laser (float_of_string (List.nth laser_list 0),float_of_string (List.nth laser_list 1)) 
					 (float_of_string(List.nth laser_list 2)) 
					 (float_of_string(List.nth laser_list 3),float_of_string(List.nth laser_list 4))

<<<<<<< Updated upstream

(********************** STRINGIFY FUNCTIONS ***********************)
let stringify_coord (x,y) = "X"^(string_of_float x)^"Y"^(string_of_float y)
let stringify_speed (vx,vy) = "VX"^(string_of_float vx)^"VY"^(string_of_float vy)
let stringify_angle a = "T"^(string_of_float a)
let stringify_coord_opt target =
    match target with
    |Some(x,y) -> "X"^(string_of_float x)^"Y"^(string_of_float y)
    |None -> "XY"
let rec stringify_scores p_list =
	 match p_list with
	  |hd::[] -> hd.name^":"^(string_of_int hd.score)^"/"
	 	|hd::tl -> hd.name^":"^(string_of_int hd.score)^"|"^(stringify_scores tl)
		|_ -> "" (* n'arrivera jamais juste pour la complétude du pattern matching*)
=======
let stringify_coord (x,y) =
	"X"^(string_of_float x)^"Y"^(string_of_float y)

let stringify_speed (vx,vy) =
	"VX"^(string_of_float vx)^"VY"^(string_of_float vy)

(* angle en radian sur le sujet, à decider ici si degre ou radian *)
let stringify_angle a =
	"T"^(string_of_float a)

>>>>>>> Stashed changes
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
let rec stringify_lasers p_list = (* string du TICK pour la partie Extension Lasers : avec les vitesses et l'angle *)
	match p_list with
	|hd::[] -> (stringify_coord hd.position)^(stringify_speed hd.speed)^(stringify_angle hd.direction)^"/"
	|hd::tl -> (stringify_coord hd.position)^(stringify_speed hd.speed)^(stringify_angle hd.direction)^"|"^(stringify_lasers tl)
	|[] -> ""

<<<<<<< Updated upstream
let rec stringify_coordsXY o_list = (* nouveau strindigy pour les obstacles car ne peut réutiliser le stringify_coords qui s'appliquent aux joueurs *)
    match o_list with
    |hd::[] -> (stringify_coord hd)
    |hd::tl -> (stringify_coord hd)^"|"^(stringify_coordsXY tl)
    |[] -> ""





(********************** SENDING FUNCTIONS ***********************)
let send_session () =
	let send_fun coords c_target c_obstacles p =
		output_string p.outchan ("SESSION/"^coords^c_target^"/"^c_obstacles^"/\n");
		flush p.outchan
	in
		let coords = stringify_coords current_session.players_list
		and co_target = stringify_coord_opt current_session.target
		and coords_obs = stringify_coordsXY current_session.obstacles_list in
			List.iter (send_fun coords co_target coords_obs) current_session.players_list
=======
let rec stringify_coordsXY points_list = (* nouveau strindigy pour les points car ne peut réutiliser le stringify_coords qui s'appliquent aux joueurs *)
    match points_list with
    |hd::[] -> (stringify_coord hd)^"/"
    |hd::tl -> (stringify_coord hd)^"|"^(stringify_coordsXY tl)
    |[] -> ""

let find_player user_name =
	List.find (fun p -> if p.name=user_name then true else false) current_session.players_list

let exists_player user_name =
	List.exists (fun p -> if p.name=user_name then true else false) current_session.players_list

let not_colliding_obs position obs =
    if (get_distance position obs) > (ve_radius+.ob_radius) then true else false

let rec get_valid_pos () =
    let position = alea_pos() in
    print_endline "la position est valide : true ou false ";
    print_endline (string_of_bool (List.for_all (not_colliding_obs position) current_session.obstacles_list));
    if List.for_all (not_colliding_obs position) current_session.obstacles_list
    then position
    else get_valid_pos () (*la position n'est pas valide*)


(*permet de créer un joueur*)
let create_player user sock inc out =
	{ name = user;
  	socket = sock;
    inchan = inc;
    outchan = out;
    score = 0;
    car = {position=get_valid_pos();direction=0.0;speed=(0.0,0.0)};
    is_colliding = false;
    is_colliding_ve = false;
    nb_reach = 0
	}


let init_players () =
	let f p =
	    p.car.position <- get_valid_pos() ;
	    p.car.speed <- (0.0,0.0);
	    p.car.direction <- 0.0;
	    p.score <- 0;
	    p.is_colliding <- false;
	    p.is_colliding_ve <- false;
	    p.nb_reach <- 0
	in
	List.iter f current_session.players_list


let get_new_obstacles n =
    let rec aux_get nb =
        match nb with
        |0 -> []
        |x -> alea_pos()::aux_get (x-1)
     in aux_get n

let get_targets_list () =
    match current_session.targets_list with
    |Some(l) -> l
    |None -> failwith "No target list"


let get_list_n_targets n =
    let rec aux_get nb =
        match nb with
        |0-> []
        |x -> (get_valid_pos())::(aux_get (nb-1))
    in aux_get n

(********************** SENDING FUNCTIONS ***********************)
let send_session () =
	let send_fun coords c_target c_obstacles c_targets p =
		output_string p.outchan ("SESSION/"^coords^c_target^"/"^c_obstacles^c_targets^"\n");
		flush p.outchan
	in
	let coords = stringify_coords current_session.players_list
	and co_t = stringify_coord (List.hd (get_targets_list ()))
    and co_targets = stringify_coordsXY (List.tl (get_targets_list ()))
	and coords_obs = stringify_coordsXY current_session.obstacles_list in
		List.iter (send_fun coords co_t coords_obs co_targets) current_session.players_list
>>>>>>> Stashed changes

let send_welcome user_name =
	Mutex.lock mutex_players_list;
	print_endline "dans sens_welcome";
	let phase = if current_session.playing then "jeu/" else "attente/"
	and scores = stringify_scores current_session.players_list
	and co_t = stringify_coord (List.hd (get_targets_list ()))
	and coords_obs = stringify_coordsXY current_session.obstacles_list
	and co_targets = stringify_coordsXY (List.tl (get_targets_list ()))
	and player = find_player user_name
	in
<<<<<<< Updated upstream
		output_string player.outchan ("WELCOME/"^phase^scores^coord_target^"/"^coords_obs^"\n");
		flush player.outchan;
		if current_session.playing then
		begin
			let coords = stringify_coords current_session.players_list in
				output_string player.outchan ("SESSION/"^coords^coord_target^"/"^coords_obs^"\n");
				flush player.outchan
		end;
		Mutex.unlock mutex_players_list
=======
	output_string player.outchan ("WELCOME/"^phase^scores^co_t^"/"^coords_obs^co_targets^"\n");
	flush player.outchan;
	if current_session.playing then
	begin
		let coords = stringify_coords current_session.players_list in
			output_string player.outchan ("SESSION/"^coords^co_t^"/"^coords_obs^co_targets^"\n");
            flush player.outchan
	end;
	Mutex.unlock mutex_players_list
>>>>>>> Stashed changes

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
			current_session.lasers_list <- [];
			current_session.pieges_list <- [];
			List.iter (send_fun f_scores) current_session.players_list

(* protégé par le mutex de l'appelant *)
let send_tick p =
	let f_coords = stringify_tick current_session.players_list in
        output_string p.outchan ("TICK/"^f_coords^"\n");
        flush p.outchan


<<<<<<< Updated upstream
let send_tick_newpieges_newlasers () =
	let send_fun ticks pieges lasers p =
		output_string p.outchan ("TICK/"^ticks^pieges^"/"^lasers^"\n");
=======
let send_tick_newpieges () =
	let send_fun ticks pieges p =
		output_string p.outchan ("TICK/"^ticks^pieges^"\n");
>>>>>>> Stashed changes
		flush p.outchan
	in
		let p_coords = stringify_coordsXY current_session.pieges_list
		and l_coords = stringify_lasers current_session.lasers_list
		and f_coords = stringify_tick current_session.players_list in
			List.iter (send_fun f_coords p_coords l_coords) current_session.players_list

(* protégé par le mutex de l'appelant *)
let send_newobj () =
<<<<<<< Updated upstream
   let send_fun coord scores p =
		output_string p.outchan ("NEWOBJ/"^coord^"/"^scores^"/\n");
		flush p.outchan
	in
		let coord = stringify_coord_opt current_session.target
		and scores = stringify_scores current_session.players_list in
			List.iter (send_fun coord scores) current_session.players_list
=======
    print_endline "dans send_newobj";
	let send_fun co_t scores co_targets p =
		output_string p.outchan ("NEWOBJ/"^co_t^"/"^scores^co_targets^"/\n");
		flush p.outchan
	in
	let coord = stringify_coord (List.hd (get_targets_list ()))
	and scores = stringify_scores current_session.players_list
	and co_targets = stringify_coordsXY (List.tl (get_targets_list ())) in
	    	print_endline coord;
	    	print_endline co_targets;
		List.iter (send_fun coord scores co_targets) current_session.players_list
>>>>>>> Stashed changes

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
		output_string player.outchan ("PRECEPTION/"^message^"/"^from_user^"\n");
		flush player.outchan

<<<<<<< Updated upstream
(********************** SESSIONS FUNCTIONS ***********************)
(* utilisation de cette fonction suite à un gagnant de session *)
(* normalement il n'y a qu'un thread client qui a accès à cette fonction à un moment *)
let start_session () =
	Mutex.lock mutex_players_list;
	current_session.obstacles_list <- get_new_obstacles 5;
	while (List.length current_session.players_list = 0) do(* ERREUR surement pas besoin car condition attent toit seul*)
=======

let start_session () =
	Mutex.lock mutex_players_list;
	print_endline "start_session lock";
	current_session.obstacles_list <- get_new_obstacles nb_obstacles;
	while (List.length current_session.players_list = 0) do
>>>>>>> Stashed changes
		(* peut être pas besoin de boucle *)
		Condition.wait cond_least1player mutex_players_list
	done;
	Mutex.unlock mutex_players_list;
	print_endline "start_session unlock";
	Unix.sleep waiting_time;
	Mutex.lock mutex_players_list;
	print_endline "start_session lock";
	if (List.length current_session.players_list) > 0 then
	begin
	current_session.playing <- true;
	send_session ()
	end
	else ();
	Mutex.unlock mutex_players_list;
	print_endline "start_session unlock"

let restart_session () =
	Mutex.lock mutex_players_list;
	print_endline "restart_session lock";
	current_session.playing <- false;
	while (List.length current_session.players_list = 0) do
		(* peut être pas besoin de boucle *)
		Condition.wait cond_least1player mutex_players_list
	done;
	current_session.obstacles_list <- get_new_obstacles nb_obstacles; (*l'initialisation des obstacles doit se faire en 1er car le reste en depend,*)
	init_players ();
	current_session.pieges_list <- [];
	current_session.targets_list <- Some(get_list_n_targets nb_targets);
	Mutex.unlock mutex_players_list;
    print_endline "restart_session unlock";
	(* le mutex est rendu pour que d'autres clients puissent se connecter entre-temps *)
	Unix.sleep waiting_time;
	(* Mutex.lock mutex_players_list; *)
	Mutex.lock mutex_players_list;
		print_endline "restart_session lock";
	current_session.playing <- true;
	send_session ();
	Mutex.unlock mutex_players_list;
    	print_endline "restart_session unlock"


<<<<<<< Updated upstream

(********************** PROCESS FUNCTIONS ***********************)
let maybe_target_reached player =
=======
(********************** PROCESSING FUNCTIONS ***********************)
let normal_mode player =
>>>>>>> Stashed changes
    Mutex.lock mutex_players_list;
    print_endline "normal_mode lock";
    let target_coord = List.hd (get_targets_list()) in
    if (get_distance target_coord player.car.position <= obj_radius) then
        (* le joueur a touché l'objectif *)
        begin
        player.score <- player.score+1;
        if (player.score = win_cap) then
            (* le joueur a atteint win_cap : send_winner & restart_session *)
            begin
            send_winner ();
            Mutex.unlock mutex_players_list;
            print_endline "normal_mode unlock";
            (* besoin d'exécuter restart hors SC car unix.sleep à l'intérieur *)
            restart_session ()
            end
        else
            (* nouvel objectif : send_newobj *)
            begin
            current_session.targets_list <- Some(get_list_n_targets nb_targets);
            send_newobj ();
            Mutex.unlock mutex_players_list;
            print_endline "normal_mode unlock"
            end
        end
    else (Mutex.unlock mutex_players_list;print_endline "normal_mode unlock")

let rush_mode player =
    Mutex.lock mutex_players_list;
    print_endline "rush_mode lock";
    let target_coord = List.nth (get_targets_list()) player.nb_reach in
    if (get_distance target_coord player.car.position <= obj_radius) then
        (* le joueur a touché l'objectif qu'il devait atteindre dans l'ordre *)
        begin
        player.nb_reach <- player.nb_reach+1;
        if (player.nb_reach = nb_targets) then
            (* le joueur a complété tous les targets *)
            begin
            player.score <- player.score+1;
            player.nb_reach <- 0;
            output_string player.outchan ("NEXT/"^(string_of_int player.nb_reach)^"/\n");
            flush player.outchan;
            if (player.score = win_cap) then
               (* le joueur a atteint win_cap : send_winner & restart_session *)
                begin
                send_winner ();
                Mutex.unlock mutex_players_list;
                print_endline "rush_mode score=wincap unlock";
                (* besoin d'exécuter restart hors SC car unix.sleep à l'intérieur *)
                restart_session ()
                end
             else
                (*le joueur n'a pas encore atteint win cap, c'est reparti pour une course *)
                begin
                current_session.targets_list <- Some(get_list_n_targets nb_targets);
                send_newobj();
                Mutex.unlock mutex_players_list;
                print_endline "rush_mode score <> wincap unlock"
                end
            end
        else
            begin
            output_string player.outchan ("NEXT/"^(string_of_int player.nb_reach)^"/\n");
            flush player.outchan;
            Mutex.unlock mutex_players_list;
            print_endline "rush_mode jkjkjkj unlock"
            end
        end
    else (Mutex.unlock mutex_players_list; print_endline "rush_mode unlock")

let maybe_target_reached player =
    match nb_targets with
    |0 -> failwith "Cannot play without any targets"
    |1 -> normal_mode player
    |_ -> rush_mode player



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

let compute_laser laser =
		let new_x = (fst laser.position) +. (fst laser.speed)
		and new_y = (snd laser.position) +. (snd laser.speed) in
		laser.position<-(new_x,new_y);
		if new_x > demil then laser.position <- ((-.demil)+.(mod_float (fst laser.position) demil),
													    snd laser.position);
		if new_y > demih then laser.position <- (fst laser.position,
													    (-.demih)+.((snd laser.position)-.demih));
		if new_x < -.demil then laser.position <- (demil-.(mod_float (fst laser.position) demil),
														snd laser.position);
		if new_y < -.demih then laser.position <- (fst laser.position,
														demih-.(mod_float (snd laser.position) demih))

let check_collisions player =
    let check_collision_obstacle o =
        let distance = get_distance player.car.position o in
        if (not player.is_colliding) && distance <= (ob_radius+.ve_radius) then
            (* n'était pas en collision, et la nouvelle distance donne une collision *)
            begin
            player.is_colliding <- true;
            player.car.speed <- (-.(fst player.car.speed),-.(snd player.car.speed))
            end
        else
            begin
            (* était en collision, mais s'est assez éloigné de l'obstacle *)
<<<<<<< Updated upstream
            if player.is_colliding && distance > (ob_radius+.ve_radius+.100.0) then
=======
            if player.is_colliding && distance > (ob_radius+.ve_radius+.60.0) then
            (*print_endline "sortie de la zone de collision nlnl";
            print_endline ("distance = "^(string_of_float distance));*)
>>>>>>> Stashed changes
            player.is_colliding <- false
            end (* probleme : si je "touche" 2 obstacles, il ne se passe rien, tout continu normalement puisque les signges se seront inversé 2 fois *);
     and check_collision_players other_player =
        if (player.name <> other_player.name)
        then
            let distance = get_distance player.car.position other_player.car.position in
            if player.is_colliding_ve = false && other_player.is_colliding_ve = false && distance <= (ve_radius*.2.0)
            then
                begin
                player.is_colliding_ve <- true;
                other_player.is_colliding_ve <- true;
                (player.car.speed <- (-.(fst player.car.speed),-.(snd player.car.speed));
                other_player.car.speed <- (-.(fst other_player.car.speed),-.(snd other_player.car.speed)))
                end
            else
                begin
                if player.is_colliding_ve = true && other_player.is_colliding_ve = true && distance > (ve_radius*.2.0) && distance < (ve_radius*.2.0+.15.0)
                then
                    (player.is_colliding_ve <- false;
                    other_player.is_colliding_ve <- false)
                end
		and check_collision_pieges p =
			if (get_distance player.car.position p)<(ve_radius+.pi_radius)
	    then
			  (player.car.speed <- alea_speed();
				player.car.direction <- alea_angle ();
				remove_piege p)
		and check_collision_lasers laser =
			let distance = get_distance player.car.position laser.position in
				if distance <= (ve_radius+.la_radius) 
				then 
					(player.car.speed <- (0.0,0.0);
					 remove_laser laser)
    in
    List.iter check_collision_obstacle current_session.obstacles_list;
    List.iter check_collision_players current_session.players_list;
<<<<<<< Updated upstream
	List.iter check_collision_pieges current_session.pieges_list;
	List.iter check_collision_lasers current_session.lasers_list
=======
	List.iter check_collision_pieges current_session.pieges_list
>>>>>>> Stashed changes


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
			let coord_target = List.hd (get_targets_list()) in
			if (get_distance coord_target parsed_coord <= obj_radius) then
				(* le joueur a touché l'objectif *)
				begin
				player.score <- player.score+1;
				if (player.score = win_cap) then
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
					current_session.targets_list <- Some(get_list_n_targets nb_targets);
					send_newobj ();
					Mutex.unlock mutex_players_list
					end
				end
			else Mutex.unlock mutex_players_list
		else Mutex.unlock mutex_players_list


let process_newcom cmd_string user_name =
	let player = find_player user_name in
		Mutex.lock mutex_players_list;
		print_endline "process_newcom lock";
		compute_cmd player (parse_cmd cmd_string); (* met a jour les (vx,vy) du joueur user_name e et refresh les données du client  *)
		List.iter compute_laser current_session.lasers_list;
		check_collisions player;
<<<<<<< Updated upstream
		send_tick_newpieges_newlasers(); 		(*send_tick player *)
=======
		send_tick_newpieges();
		(*send_tick player *)
>>>>>>> Stashed changes
		Mutex.unlock mutex_players_list;
	    print_endline "process_newcom unlock";
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

let process_newpiege coord =
	Mutex.lock mutex_players_list;
	current_session.pieges_list <- (parse_coord coord)::current_session.pieges_list;
	send_tick_newpieges_newlasers ();
	Mutex.unlock mutex_players_list
let process_newlaser laser_string =
	Mutex.lock mutex_players_list;
	let laser = parse_laser laser_string in 
		current_session.lasers_list <- laser::current_session.lasers_list;
		send_tick_newpieges_newlasers ();
		Mutex.unlock mutex_players_list
(********************** THREADING FUNCTIONS ***********************)
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
				|"PENVOI" ->	if List.length parsed_req < 3 then raise BadRequest;
											process_penvoi (List.nth parsed_req 1) (List.nth parsed_req 2) user_name
				|"NEWPIEGE" ->	if List.length parsed_req <> 2 then raise BadRequest;
											process_newpiege (List.nth parsed_req 1)
				|"NEWLASER" ->	if List.length parsed_req <> 2 then raise BadRequest;
											process_newlaser (List.nth parsed_req 1)		
				|_ -> raise BadRequest
			with BadRequest -> output_string player.outchan "DENIED/BadRequest\n";
										 			flush player.outchan
		done
	with Disconnection -> ()

let server_refresh_tick_thread () =
	let refresh p =
	    Mutex.lock mutex_players_list;
	    print_endline "server_refrssh thread lock";
	    p.car.position <- (fst p.car.position+.(fst p.car.speed),snd p.car.position+.(snd p.car.speed));
	    Mutex.unlock mutex_players_list;
<<<<<<< Updated upstream
        maybe_target_reached p;
	and refresh_lasers p =
	    Mutex.lock mutex_players_list;
		print_endline (string_of_float (fst p.speed));
	    p.position <- (fst p.position+.(fst p.speed),snd p.position+.(snd p.speed));
	    Mutex.unlock mutex_players_list;
	and check_collision_lasers_obstacles obstacle = 
		let check_collision laser = 
			let distance = get_distance obstacle laser.position in
						if distance <= (ob_radius+.la_radius) then remove_laser laser
		in 
		List.iter check_collision current_session.lasers_list

=======
	    print_endline "server_refrssh thread unlock";
        maybe_target_reached p
>>>>>>> Stashed changes
	in
		while true do
			Unix.sleepf (1.0/.server_refresh_tickrate);
			if current_session.playing then
			    print_endline "dans refresh playing";
				List.iter refresh current_session.players_list;
				List.iter refresh_lasers current_session.lasers_list;
				List.iter check_collisions current_session.players_list;
				List.iter check_collision_lasers_obstacles current_session.obstacles_list
		done



(********************** PROCESSING NEW CONNECTION ********************)
(* les opérations de verification et d'ajout du player dans la liste
 doivent être dans le même bloc mutex, sinon un autre client peut potentiellement
 s'être inséré entre la vérif et l'ajout de ce client  *)
let process_connect user_name client_socket inchan outchan =
    print_endline "dans process_connect";
	Mutex.lock mutex_players_list;
	print_endline "après l'acquisition du mutex";
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
	if current_session.targets_list = None then current_session.targets_list <- Some(get_list_n_targets nb_targets); (* la première connection initialise le target *)
	send_welcome user_name;
	print_endline "après send_welcome";
	send_newplayer user_name;
	receive_req user_name



(********************** FIRST CONNECTION ***********************)
(* c'est ce thread qui est lancé lorsqu'un client se connecte *)
let start_new_client client_socket =
    print_endline "dans start new client";
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
let start_server nb_c mode =
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
start_server (int_of_string Sys.argv.(1)) (int_of_string Sys.argv.(2));;
