(* Utilisation des conditions *) 
(* pour compiler :    ocamlc -thread unix.cma threads.cma cond1.ml -o cond1 *) 
let cond = Condition.create () 
let cond_lock = Mutex.create ()
let task1 () =   
	Thread.delay 2.0; 
	(* laisse le temps aux autres de se mettre en attente *)   
	Mutex.lock cond_lock;   
	Condition.signal cond;   
	Mutex.unlock cond_lock;      
	Thread.delay 2.0;   
	Mutex.lock cond_lock;   
	Condition.broadcast cond;   
	Mutex.unlock cond_lock;
	Thread.delay 2.0;;

let task2 nb =   
	Condition.wait cond cond_lock;
	print_int nb ; print_endline " => tache reveil 1";
	Mutex.unlock cond_lock;
	Condition.wait cond cond_lock;
	print_int nb ; 
	print_endline " => tache reveil 2";
	Mutex.unlock cond_lock;;
	
let main () =   
	let target1 = Thread.create task2 1   
	and target2 = Thread.create task2 2   
	and source = Thread.create task1 ()   in     
	    Thread.join source;
	    print_endline "Termine";
	    exit 0;;

main ();;
