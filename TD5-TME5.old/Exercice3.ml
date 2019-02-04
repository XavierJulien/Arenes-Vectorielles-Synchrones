let sortie = Mutex.create ()

let rec ecouteur (chan,id) =
  let x = let _ = Thread.delay ((float_of_int (3 + (Random.int 10)))/. 5.0) in 
	  Event.sync (Event.receive chan) in
  Mutex.lock sortie;
  print_endline ((string_of_int id)^" recoit "^x);
  Mutex.unlock sortie; 
  ecouteur (chan,id)
    
let rec broadcast (l,bd) =
  let rec rem x l =
    let rec rem_a x acc = function
      | [] -> acc 
      | t::q -> if x == t then rem_a x acc q else rem_a x (t::acc) q in
    rem_a x [] l in
  let rec broadcast_aux x l = match l with 
      [] -> ()
    | _ -> Event.select (List.map 
			   (fun ch -> Event.wrap (Event.send ch x) 
			     (fun _ -> broadcast_aux x (rem ch l))) 
			   l)	in
  let y = Event.sync (Event.receive bd) in
  broadcast_aux y l;broadcast (l,bd)
    
let creer_liste n = 
  let rec creer_liste_aux n acc =
    if n = 0 then acc else creer_liste_aux (n-1) ((Event.new_channel ())::acc)
  in creer_liste_aux n []

let creer_threads l =
  let rec creer_threads_a n = function
    | [] -> ()
    | ch::q -> let _ = (Thread.create ecouteur (ch,n)) in
	       creer_threads_a (n+1) q in
  creer_threads_a 1 l 
    
let main () =
  let l = creer_liste 10 in 
  let _ = creer_threads l in
  let bd = Event.new_channel () in
  let _ = Thread.create broadcast (l,bd) in
  let _ = Event.sync (Event.send bd "bonjour") in
  let _ = Event.sync (Event.send bd "ca va ?") in
  let _ = Event.sync (Event.send bd "au revoir") in
  let _ = Thread.delay 30.0 in ()

let _ = main ()
      
