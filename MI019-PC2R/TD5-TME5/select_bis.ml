(* ocamlopt -thread unix.cmxa threads.cmxa select_bis.ml -o select_bis *)

let c_b = Event.new_channel ()
and c_p = Event.new_channel ()
and c_o = Event.new_channel ()
and max = 7


let rec work (str,chan,n) =
  if (n < max) then
    begin
      Thread.delay(2.);
      print_endline(str);
      let evt = Event.send chan (str^" "^(string_of_int n)) in
      if str = "pomme" then
	begin
	  Thread.delay(10.);
	end
      else ();
      Event.sync(evt);
      work (str,chan,(n+1))
    end
  else ()
    
let rec consumer () =
  let evt = Event.choose
      [Event.receive c_p; Event.receive c_b;  Event.receive c_o]
  in
  print_endline("Fin de choose");
  print_endline(Event.sync(evt));
  consumer ()

let main () = 
  let t1 = Thread.create work ("pomme",c_p,1)
  and t2 = Thread.create work ("orange",c_o,1)
  and t3 = Thread.create work ("banane",c_b,1)
  and _ = Thread.create consumer () in
  Thread.join t1;Thread.join t2;Thread.join t3

let _ = main ()
;;


(* Remarquer que "Fin de choose" est sortie le premier. Pourtant les work
   n'ont pas eu encore le temps de faire leur send.

$ ./select_bis 
Fin de choose
banane
orange
banane 1
Fin de choose
orange 1
Fin de choose
pomme
banane
banane 2
Fin de choose
orange
orange 2
Fin de choose
banane
banane 3
Fin de choose
orange
orange 3
Fin de choose
banane
banane 4
Fin de choose
orange
orange 4
Fin de choose
banane
orange
banane 5
Fin de choose
orange 5
Fin de choose
orange
pomme 1
Fin de choose
banane
orange 6
Fin de choose
banane 6
Fin de choose
pomme
pomme 2
Fin de choose
pomme
pomme 3
Fin de choose
pomme
pomme 4
Fin de choose
pomme
pomme 5
Fin de choose
pomme
pomme 6
Fin de choose
$ 

*)
