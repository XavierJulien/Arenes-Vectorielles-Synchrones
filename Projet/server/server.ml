let creer_serveur max_co =
  let sock = Unix.socket Unix.PF_INET SOCK_STREAM 0
  and addr = Unix.inet_addr_of_string "127.0.0.1"
  and port = 2019 in
    Unix.bind sock (Unix.ADDR_INET(addr, port));
    Unix.listen sock max_co;
    sock;;

let serveur_process sock service =
  while true do
    let (s,caller) = Unix.accept sock in
      ignore(Thread.create service s);
  done;;

let echo_service chan =
  let inchan = Unix.in_channel_of_descr chan
  and outchan = Unix.out_channel_of_descr chan in
    while true do
      try
        let line = input_line inchan in
        print_string ("hello"^line^"\n");
      with
      | End_of_file -> failwith "end of file"
    done;;

let main () =
  let sock = creer_serveur 4 in
    serveur_process sock echo_service;;

let _ = main ();;
