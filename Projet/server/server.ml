(* let creer_serveur max_co =
  let sock = Unix.socket Unix.PF_INET SOCK_STREAM 0
  and addr = Unix.inet_addr_of_string "127.0.0.1"
  and port = 2019 in
    Unix.bind sock (Unix.ADDR_INET(addr, port));
    Unix.listen sock max_co;
    sock;; *)
exception Fin;;

let my_input_line fd =
  let s = Bytes.create 200 and r = ref "" in
  try
  while (Unix.read fd s 0 1 > 0) && (Bytes.get s 0 <> '\n') do
    r := !r ^ Bytes.to_string s;
    print_string !r
  done;
  !r
  with Invalid_argument _ -> "il y a un argument invalide"


let echo_service socket_client =
  (* let message = Bytes.create 10000
  and inchan = Unix.in_channel_of_descr socket_client
  and outchan = Unix.out_channel_of_descr socket_client in *)
  try
    while true do
      let line = my_input_line socket_client in
      (if (line = "") || (line = "\013") then raise Fin ;
      print_string line;
      let result = Bytes.of_string ((String.uppercase_ascii line)^"\n" )in
      (* let result = Bytes.of_string "Bonsoir" in *)
        Unix.write socket_client result 0 (Bytes.length result))
    done
  with Fin -> ()
      | exn -> print_string (Printexc.to_string exn) ; print_newline()


let server_process socket_descr service =
      Thread.create service socket_descr;
      print_string "le thread a été crée"


let main () =
  let server_socket = Unix.socket Unix.PF_INET SOCK_STREAM 0
  and addr = Unix.inet_addr_of_string "127.0.0.1"
  and port = 2019 in
  begin
    Unix.bind server_socket (Unix.ADDR_INET(addr, port));
    Unix.listen server_socket (int_of_string Sys.argv.(1));
    while true do
      let (client_socket, client_so_addr) = Unix.accept server_socket in
        server_process client_socket echo_service
    done
  end

let _ = main ();;
