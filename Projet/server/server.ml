exception Fin
exception BadRequest
exception AlreadyExists

<<<<<<< Updated upstream
let mutex_list_players = Mutex.create (); (*sync for creating player with name*)

type car = {
	position: float * float;
	direction: float;
	speed: float * float
	}
=======
(* VERIFIER AVEC UN PROF AU NIVEAU DE LA LIAISON STATIQUE,
POUR LES CHAMPS MUTABLES ENREGISTREMENTS, EST CE QUE LA VALEUR EST FIXEE DES
LA D2FINITION DE LA FONCTION OU EST CE QUE CELA PREND EN COMPTE TOUTES LES MODIFICATIONS
EFFECTUEES SUR LE CHAMPS AU COURS DU PROGRAMME ?  => SI ON DOIT FAIRE PASSER LA
	DES JOUEURS EN PARAM7TRE

	LE FAIT D ECRIRE UNE FONCTION POUR sending ET processing M'AMENE A POSER CETTE QUESTION *)

let mutex_players_list = Mutex.create (); (*sync for access to *)


type vehicule = {
	mutable position: float * float;
	mutable direction: float;
	mutable speed: float * float
}
>>>>>>> Stashed changes

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
<<<<<<< Updated upstream
    mutable objectif : float * float
	win_cap : int
	}

(*permet de créer un joueur*)
let create_player user sock inc out = {
    name = user;
    socket = sock;
=======
    mutable target: float * float
		obj_radius: float
		win_cap : int
}

(*permet de créer un joueur*)
let create_player user sock inc out =
	{name = user;
  	socket = sock;
>>>>>>> Stashed changes
    inchan = inc;
    outchan = out;
    score = 0;
    car = {position=(0.0,0.0);direction=0.0;speed=(0.0,0.0)}
    (*playing = WAITING*)
	}

<<<<<<< Updated upstream
let current_session = {
    list_players = [];
    playing = false;
		win_cap = 10
  }

let parse_request req =
  let s = Str.split (Str.regexp "/") req in
  if (List.length s) > 0 then s else raise BadRequest

let parse_coord c =
	let lcoord = Str.split (Str.regexp "X\|Y") c in
		(float_of_string (List.nth lcoord 0),float_of_string (List.nth lcoord 1))

let rec stringify_scores list_players =
	 match list_players with
	 	|hd::tl -> if tl <> [] then hd.name":"^hd.score^"|")^(stringify_scores tl)
	 						   else s^hd.name":"^hd.score^"/"

let make_welcome player =
	let phase = if current_session.playing then "playing/" else "waiting/"
	and scores = stringify_scores current_session.list_players
	and coords = let (x,y) = current_session.objectif in
	"("^(string_of_float x)^","(string_of_float y)^")" in
	output_string player.outchan "WELCOME/"^phase^scores^coords^"/\n";
=======
let current_session =
  {players_list = [];
		playing = false;
		target = (0.0*0.0);
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
	 	|hd::tl when tl <> [] -> hd.name":"^hd.score^"|")^(stringify_scores tl)
	 	|hd::_ -> hd.name":"^hd.score^"/"

let stringify_coord (x,y) =
	"X"^(string_of_float x)^"Y"(string_of_float y)

let rec stringify_coords p_list =
	match p_list with
		|hd::tl when tl <> [] -> hd.name":"^(stringify_coord hd.car.position)^"|")^(stringify_coords tl)
		|hd::_ -> hd.name":"^(stringify_coord hd.car.position)^"/"

let find_player user_name p_list =
	List.find (fun p -> if p.name=user_name then true else false) p_list

(********************** SENDING FUNCTIONS ***********************)
let send_welcome player =
	let phase = if current_session.playing then "playing/" else "waiting/"
	and scores = stringify_scores current_session.players_list
	and coord = stringify_coord current_session.target
	in
	output_string player.outchan "WELCOME/"^phase^scores^coord^"/\n";
>>>>>>> Stashed changes
	flush player.outchan

let send_newplayer user_name =
	let send_fun p =
		if p.name<>user_name then
		output_string p.outchan "NEWPLAYER/"^user_name^"/\n";
		flush p.outchan
		else ()
	in
	List.iter send_fun current_session.players_list

let send_playerleft user_name =
	let send_fun p =
		output_string p.outchan "PLAYERLEFT/"^user_name^"/\n";
		flush p.outchan
	in
	List.iter send_fun current_session.players_list

let send_session () =
	let coords = stringify_coords current_session.players_list in
		let coord_target = stringify_coord current_session.target in
			output_string p.outchan "SESSION/"^coords^"/"^coord_target^"/\n";
			flush p.outchan

(*  Fin de la session courante, scores finaux de la session. ->
il faut recommencer la SESSION dans l'appelant ? a voir *)
let send_winner () =
	let send_fun scores p =
		output_string p.outchan "WINNER/"^scores^"/\n";
		flush p.outchan
	in
	Mutex.lock mutex_players_list;
	let f_scores = stringify_scores current_session.players_list in
		List.iter (send_fun f_scores) current_session.players_list;
	Mutex.unlock mutex_players_list;


let send_tick () =
	let send_fun coords p =
		output_string p.outchan "TICK/"^coords^"/\n";
		flush p.outchan
	in
	Mutex.lock mutex_players_list;
	let f_coords = stringify_coords current_session.players_list in
		List.iter (send_fun f_coords) current_session.players_list;
	Mutex.unlock mutex_players_list;

let send_newobj () =
	let send_fun coord scores p =
		output_string p.outchan "NEWOBJ/"^coord^"/"^scores^"/\n";
		flush p.outchan
	in
	let coord = stringify_coord current_session.target
	and scores = stringify_scores current_session.players_list in
		List.iter send_fun coord scores current_session.
(*let send_denied chan =
	output_string chan "DENIED/";
<<<<<<< Updated upstream
	flush chan*)
let make_newplayer user_name =
	let send_fun p =
	if p.name<>user_name then
	output_string p.outchan "NEWPLAYER/"^user_name^"/\n";
	flush player.outchan
	else ()

let find_player user_name =
	List.find (fun p -> if p.name=user_name then true else false) current_session.list_players

let process_exit user_name =
	let rec process_aux l_players =
=======
	flush chan *)



(********************** PROCESSING FUNCTIONS ***********************)

let process_exit user_name =
	let rec remove_aux l_players =
>>>>>>> Stashed changes
		match l_players with
		|{name;_} as hd::tl -> if name=user_name then tl else hd::(process_aux tl)
		|[] -> raise NotFound in
		try
<<<<<<< Updated upstream
			let player = find_player user_name in
=======
			let player = find_player user_name current_session.players_list in
>>>>>>> Stashed changes
			Unix.close player.socket;
			Mutex.lock mutex_players_list;
			current_session.players_list <- remove_aux current_session.players_list;
			Mutex.unlock mutex_players_list; (* est ce que ca lock aussi pdt toute la duree de remove ? *)
			send_playerleft player.name
		with NotFound -> print_endline "trouver quoi faire si cela arrive"

<<<<<<< Updated upstream
let process_newpos user_name coord =
	let player = find_player user_name in
		player.vehicule.position <- parse_coord coord

let process_player player =
	while true do
		let request = input_line player.inchan
		and req_parse = parse_request request in
			match List.hd req_parse with
			|"EXIT" -> process_exit (List.nth req_parse 1)
			|"NEWPOS" -> process_newpos (List.nth req_parse 1)
			|_ -> ()
	done

let process_new_connect client_socket =
	let inchan = Unix.in_channel_of_descr client_socket
	and outchan = Unix.out_channel_of_descr client_socket in
		let request = input_line inchan in
		let req_parse = parse_request request in
			try
				match List.hd req_parse with
					| "CONNECT" ->
						let n = (List.nth req_parse 1) in
							Mutex.lock mutex_list_players;
							if (List.exists (fun {name;_} -> name==n) current_session.list_players)
							then raise AlreadyExists
							else begin
								let player = (create_player n client_socket inchan outchan) in
								current_session.list_players<-player::current_session.list_players;
								make_welcome player;
								process_player player
								end
						    Mutex.unlock mutex_list_players
					| _ -> raise BadRequest
			with
				|BadRequest -> output_string outchan "DENIED/BadRequest\n";
							   				 flush outchan
				|AlreadyExists -> output_string outchan "DENIED/AlreadyExists\n";
							   					flush outchan

=======
let process_newpos coord player =
		player.car.position <- parse_coord coord


(********************** thread's looping  ***********************)
let receive_req player =
	while true do
		let request = input_line player.inchan
		and parsed_req = parse_request request in
			match List.hd res_parse with
			|"EXIT" -> process_exit (List.nth parsed_req 1)
			|"NEWPOS" -> process_newpos (List.nth parsed_req 1) player
			|_ -> raise BadRequest
	done


(********************** PROCESSING NEW CONNECTION ********************)

(* les opérations de verification et d'ajout du player dans la liste
 doivent être dans le même bloc mutex, sinon un autre client peut potentiellement
 s'être inséré entre la vérif et l'ajout de ce client  *)
let process_connect user_name client_socket inchan outchan =
	Mutex.lock mutex_players_list;
	if (List.exists (fun {name;_} -> name==user_name) current_session.players_list)
	then raise AlreadyExists
	else
	begin
		let player = (create_player user_name client_socket inchan outchan) in
		current_session.players_list<-player::current_session.players_list;
		send_welcome player;
		receive_req player
		(* peut être mettre ces 2 lignes en dehors de la SC à voir *)
	end
	Mutex.unlock mutex_players_list



(********************** FIRST CONNECTION ***********************)
(* c'est ce thread qui est lancé lorsqu'un client se connecte *)
let start_new_client client_socket =
	let inchan = Unix.in_channel_of_descr client_socket
	and outchan = Unix.out_channel_of_descr client_socket in
		let request = input_line inchan in
		let parsed_req = parse_request request in
			try
				match List.hd parsed_req with
					| "CONNECT" -> process_connect (List.nth parsed_req 1) client_socket inchan outchan;
					| _ -> raise BadRequest
			with
				|BadRequest -> output_string outchan "DENIED/BadRequest\n";
							   flush outchan
				|AlreadyExists -> output_string outchan "DENIED/AlreadyExists\n";
							   flush outchan


(********************** SERVER STARTING ***********************)
(* fonction de lancement du serveur : à chaque nouvelle connexion
 		lance un thread sur start_new_client sur le socket du client *)
>>>>>>> Stashed changes
let start_server nb_c =
  let server_socket = Unix.socket Unix.PF_INET SOCK_STREAM 0
  and addr = Unix.inet_addr_of_string "127.0.0.1" in
  begin
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
    Unix.bind server_socket (Unix.ADDR_INET(addr, 2019));
    Unix.listen server_socket nb_c;
    while true do
      let (client_socket, _) = Unix.accept server_socket in
      	print_endline "Nouvelle connexion\n";
        ignore (Thread.create start_new_client client_socket);
        print_endline "Nb"
    done
  end;;


start_server (int_of_string Sys.argv.(1));;
