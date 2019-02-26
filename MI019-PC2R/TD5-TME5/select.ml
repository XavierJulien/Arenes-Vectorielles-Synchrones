(* ocamlopt -thread unix.cmxa threads.cmxa select.ml -o select *)

let c_b = Event.new_channel ()
and c_p = Event.new_channel ()
and c_o = Event.new_channel ()
and max = 7


let rec work (str,chan,n) =
  if (n < max) then
    begin
      Thread.delay ((float_of_int (3 + (Random.int 10)))/. 5.0);
      let _ = Event.sync (Event.send chan (str^" "^(string_of_int n))) in
      work (str,chan,(n+1))
    end
  else ()
    
let rec consumer () =
  let x = Event.select 
    [Event.receive c_p;Event.receive c_b;Event.receive c_o]
  in print_endline x;consumer ()

let main () = 
  let t1 = Thread.create work ("pomme",c_p,1)
  and t2 = Thread.create work ("orange",c_o,1)
  and t3 = Thread.create work ("banane",c_b,1)
  and _ = Thread.create consumer () in
  Thread.join t1;Thread.join t2;Thread.join t3

let _ = main ()
