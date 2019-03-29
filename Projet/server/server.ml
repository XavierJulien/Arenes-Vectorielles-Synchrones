exception Fin
exception BadRequest
exception AlreadyExists

let mutex_list_players = Mutex.create (); (*sync for creating player with name*)

type car = {
	position: float * float;
	direction: float;
	speed: float * float
	}

type player = {
    name: string;
    socket: Unix.file_descr;
    inchan: in_channel;
    outchan: out_channel;
    mutable score: int;
    vehicule: car
    (*mutable playing: status*)
	}

type session = {
	mutable list_players : player list;
    mutable playing : bool;
    mutable objectif : float * float
	win_cap : int
	}

(*permet de créer un joueur*)
let create_player user sock inc out = {
    name = user;
    socket = sock;
    inchan = inc;
    outchan = out;
    score = 0;
    vehicule = {position=(0.0,0.0);direction=0.0;speed=(0.0,0.0)}
    (*playing = WAITING*)
	}

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
	flush player.outchan

(*let make_denied chan =
	output_string chan "DENIED/";
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
		match l_players with
		|{name;_} as hd::tl -> if name=user_name then tl else hd::(process_aux tl)
		|[] -> raise NotFound in
		try
			let player = find_player user_name in
			Unix.close player.socket;
			current_session.list_players <- process_aux current_session.list_players;
			make_playerleft ()
		with NotFound -> print_endline "trouver quoi faire si cela arrive"

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

let start_server nb_c =
  let server_socket = Unix.socket Unix.PF_INET SOCK_STREAM 0
  and addr = Unix.inet_addr_of_string "127.0.0.1" in
  begin

    Unix.bind server_socket (Unix.ADDR_INET(addr, 2019));
    Unix.listen server_socket nb_c;
    while true do
      let (client_socket, _) = Unix.accept server_socket in
      	print_endline "Nouvelle connexion\n";
        ignore (Thread.create process_new_connect client_socket);
        print_endline "Nb"
    done
  end;;


start_server (int_of_string Sys.argv.(1));;
