let printer_lock = Mutex.create () 
let scanner_lock = Mutex.create ()

let rec serveur1 nb =     
	if nb > 0 then       
	begin          
	print_string "Serveur 1 : Job#" ; 
	print_int nb ; 
	print_newline();         
	Mutex.lock scanner_lock ;
	Mutex.lock printer_lock ;
	print_string "Scanne\n";
	Mutex.unlock scanner_lock ;
	print_string "Imprime\n";
	Mutex.unlock printer_lock ;
	serveur1 (nb-1)
	end      
	else       
	print_string "Fin Serveur 1\n"

let rec serveur2 nb = 
	if nb > 0 then       
	begin          
	print_string "Serveur 2 : Job#" ; 
	print_int nb ; 
	print_newline();         
	Mutex.lock printer_lock ;         
	Mutex.lock scanner_lock ;         
	print_string "Scanne\n";         
	Mutex.unlock scanner_lock ;          
	print_string "Imprime\n";         
	Mutex.unlock printer_lock ;         
	serveur2 (nb-1)       
	end      
	else       
	print_string "Fin Serveur 2\n"

let main () =   
	let t1 = Thread.create serveur1 10     
	and t2 = Thread.create serveur2 10   in      
	   Thread.join t1 ;     
	   Thread.join t2 ;     
	   print_string "Termine\n";     
	   exit 0;;

main ();;
