class virtual client serv p = 
object(s)
	val sock = ThreadUnix.socket Unix.PF_INET Unix.SOCK_STREAM 0
	val port_num = p
	val server = serv
	
	method start () = 
		let host = Unix.gethostbyname server in 
		let h_addr = host.Unix.h_addr_list.(0) in
		let sock_addr = Unix.ADDR_INET(h_addr,port_num) in
			Unix.connect sock sock_addr;
			s#treat sock sock_addr;
			Unix.close sock
	method virtual treat : Unix.file_descr -> Unix.sockaddr -> unit 
end;;

class client_maj s p = 
object 
  inherit client s p
  method treat s sa = 
  	try 
  		while true do 
  			let si = (input_line Unix.stdin) ^ "\n" in
  				ignore (ThreadUnix.write s si 0 (String.length si));
  					let so = (input_line s) in
  					if so = "" then raise Fin
  					else (Printf.printf "%s\n" so; flush stdout)
  				done
  			with Fin -> ()
  		end;;

let main () = 
	if Array.length Sys.argv < 3 
	then Printf.printf "usage : client server port\n"
	else 
		let port = int_of_string(Sys.argv.(2))
		and s = (Sys.argv.(1)) in
			(new client_maj s port )#start();;
			
main();;
