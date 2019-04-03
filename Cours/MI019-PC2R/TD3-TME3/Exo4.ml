(* Ordonnanceur concurrent *) 
(* version 1 : synchro par conditions *)

(* Probleme : le thread ordonnanceur peut recuperer la main avant               que la tache ne soit executee *)

(* pour compiler :    ocamlc -thread unix.cma threads.cma ordon1.ml -o ordon1 *) 

let ordon_cond = Condition.create ()
let servi_cond = Condition.create()
let ordon_mutex = Mutex.create ()
let servi_clef = Mutex.create()

let ordon_valeur = ref 0 
let ordonnanceur nb =   
  print_endline "Ordonnanceur START" ;

  while true do     
    for i = 1 to nb do       
      Mutex.lock ordon_mutex;
      ordon_valeur:=i;
      Mutex.lock servi_clef;
      Condition.broadcast ordon_cond;
      Mutex.unlock ordon_mutex;
      Condition.wait servi_cond servi_clef;
      Mutex.unlock servi_clef
    done   
  done;; 

let task nb =   
  print_string "Tache #" ; 
  print_int nb ; 
  print_endline " START\n";

  Mutex.lock ordon_mutex;
  while true do
    Condition.wait ordon_cond ordon_mutex;
    (if(!ordon_valeur=nb) then
      begin        
	print_string "Tache #" ; print_int nb ; print_endline " activee";
        (* tache a realiser et ca prend un certain temps *)
	Thread.delay (Random.float 5.0);
	print_string "Fin de Tache #" ; print_int nb ; print_endline "";
	Mutex.lock servi_clef;
	Condition.signal servi_cond;
	Mutex.unlock servi_clef;
      end     
    else ())
done
;; 

let main () =   
  let nbtask = int_of_string Sys.argv.(1)    in
  for i=1 to nbtask do       
    let th = Thread.create task i in ()
  done ;     

  (* Pour laisser le temps aux taches de se mettre en attente *)
  Thread.delay 5.0;

  let th = (Thread.create ordonnanceur nbtask)     in
  Thread.join th;;

main ();;
