(* compilation: ocamlc -thread unix.cma threads.cma echoserver2.ml -o echoserver2 *)
let creer_serveur port max_con =
  let sock = Unix.socket Unix.PF_INET Unix.SOCK_STREAM 0
  and addr = Unix.inet_addr_of_string "127.0.0.1"
  in
    Unix.bind sock (Unix.ADDR_INET(addr, port));
    Unix.listen sock max_con ;
    sock;;

let serveur_process sock service =
  while true do
    let (s, caller) = Unix.accept sock
    in
      ignore(Thread.create service (Unix.in_channel_of_descr s,Unix.out_channel_of_descr s))
  done;; 

let echo_service chans=
  let inchan = fst chans
  and outchan = snd chans
  in
    while true do
      let line = input_line inchan
      in
        output_string outchan (line^"\n") ; flush outchan
    done;;

let main () =
  let port = int_of_string Sys.argv.(1)
  in
  let sock = creer_serveur port 4 ;
  in
    serveur_process sock echo_service;;

let _ = main ();;
