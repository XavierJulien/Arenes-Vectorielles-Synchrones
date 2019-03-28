(*let my_input_line fd =
  let s = Bytes.create 200 and r = ref "" in
  try
  while (Unix.read fd s 0 1 > 0) && (Bytes.get s 0 <> '\n') do
    r := !r ^ Bytes.to_string s;
    print_string !r
  done;
  !r
  with Invalid_argument _ -> "il y a un argument invalide"
*)


(*let echo_service socket_client =
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
*)


