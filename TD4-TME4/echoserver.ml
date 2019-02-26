let client_addr toto = 
  match toto with
    | Unix.ADDR_INET(host,_) -> Unix.string_of_inet_addr host
    | _ -> "Unexpected client";;
exception Fin;;
let main() = 
  let host =  Unix.gethostbyname (Unix.gethostname()) in
  let host_addr = host.Unix.h_addr_list.(0) in 
    (* let host_addr = Unix.inet_addr_of_string "127.0.0.1" in 
-- En général, il vaut mieux éviter de se contenter d'écouter sur 127.0.0.1.*)
  let sock_descr = Unix.socket Unix.PF_INET Unix.SOCK_STREAM 0 in 
    Unix.bind sock_descr(Unix.ADDR_INET(host_addr, int_of_string Sys.argv.(1)));
    Unix.listen sock_descr 10;
    while true do
      let (service_sock,client_sock_addr) = Unix.accept sock_descr in 
      let inchan =Unix.in_channel_of_descr service_sock in
      let outchan = Unix.out_channel_of_descr service_sock in
        Printf.printf "Bonjour <%s> !\n" (client_addr client_sock_addr);
        try 
          while true do
	    let ligne = (input_line inchan) in
	      if (ligne = "") (* || (ligne = "\013") *) then raise Fin;
	      output_string outchan (ligne^"\n"); 
	      flush outchan;
          done
        with 
          | End_of_file -> Printf.printf "Fin de connexion \n";flush stdout; 
          | exn->
              print_string (Printexc.to_string exn);
              print_newline() 
    done;;

main();;
