(* Compteur au musee *) 
(* version 2 avec synchronisation et attente passive *)

(* pour compiler :    ocamlc -thread unix.cma threads.cma musee2.ml -o musee2 *) 
let compteur = ref 0 
let compteur_lock = Mutex.create () 
let entree_lock = Mutex.create()
(*let sortie_lock = Mutex.create()*)
let fin_entrees = ref false
let capacite = 20
let cond_plein = Condition.create ()

let rec entree nb =  
	let time = Unix.time();
	print_endline "Un visiteur se présente à l'entrée à l'heure %f" time;
	if nb > 0 then       
		begin  
			if compteur < capacite then 
				begin
					Mutex.lock compteur_lock;          
					compteur:=!compteur+1 ;         
					print_int nb ; 
					print_endline "Un visiteur entre au musée à l'heure %f après avoir attendu au portique pendant %f" Unix.time () (Unix.time())-time_main;
					Mutex.unlock compteur_lock;
					Thread.delay (Random.float 0.2);
					entree (nb-1)
				end
			else 
				begin 
					Mutex.lock entree_lock;
					Condition.wait cond_plein entree_lock;
					entree nb;
					Mutex.unlock entree_lock
				end
		end      
	else       
		begin         
			print_endline "Fin entrees" ;
			fin_entrees := true
		end

let sortie dummy =   
	while not !fin_entrees or !compteur>0 do     
	Mutex.lock compteur_lock;     
	if !compteur >0 then      
		begin        
		compteur:=!compteur - 1;
		Condition.broadcast cond_plein;    
		print_int !compteur ; 
		print_endline " => Sortie" ;     
		end;     
	Mutex.unlock compteur_lock;     
	Thread.delay (Random.float 0.4)   
	done;   
	print_endline "Fin sorties"

let main () =   
	let nb_visiteurs = int_of_string Sys.argv.(1) and nbbis = int_of_string Sys.argv.(2)   in     
	  let t1 = Thread.create sortie ()
	  (*and t1bis = Thread.create sortie ()*)   
	  and time_main = Unix.time ()    
	  and t2 = Thread.create entree nb_visiteurs
	  and t2bis = Thread.create entree nbbis in  
	  (* Attendre la fin des entrees *)       
	     Thread.join t2bis ; 
	     Thread.join t2 ;       
	     (* Attendre la fin des sorties *)       
	     (*Thread.join t1bis ;*) 
	     Thread.join t1 ;
	            
	     exit 0;;

main ();;
