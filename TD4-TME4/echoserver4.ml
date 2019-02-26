let creer_serveur port max_con =
  let sock = Unix.socket Unix.PF_INET Unix.SOCK_STREAM 0
  and addr = Unix.inet_addr_of_string "127.0.0.1" in
    Unix.setsockopt sock Unix.SO_REUSEADDR true;
    Unix.bind sock (Unix.ADDR_INET(addr, port));  
    Unix.listen sock max_con;
    sock;;

let compteur = ref 0;;
let listsocket = ref [];;

let dispatch ls sock =
  let inchan = Unix.in_channel_of_descr sock in
  let ligne= input_line inchan in
    List.map
      (fun x->
         if (not (x = sock))
         then ignore(Unix.send x ligne 0 (String.length ligne) []))
      ls;;

let process_clients listsocket = 
  match (Unix.select listsocket [] [] (-1.) ) with
    | (l1,l2,l3) -> ignore(List.map (dispatch listsocket) l1);;

let serveur_process sock =
  while true do     
    let (s, caller) = Unix.accept sock in 
      if !compteur = 3 then
        begin
	  listsocket := s :: !listsocket;
	  match Unix.fork() with          
	    | 0 -> (* code du fils *)
                while true do 
	          process_clients !listsocket
	        done
            | id -> (* code du pere *)
                compteur := 0;
	        listsocket := [];
	        ignore(Unix.waitpid [] id);
        end
      else
        begin
	  compteur := !compteur + 1;
	  listsocket := s :: !listsocket;
        end
  done;;

let main () =  
  let port = int_of_string Sys.argv.(1) in
  let sock = creer_serveur port 100 in
    serveur_process sock;;

let _ = main();;
