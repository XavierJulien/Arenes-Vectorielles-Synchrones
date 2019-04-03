(* Pour tester le programme : #!/bin/bash for i in {1..10} ; do
   ocamlopt -thread unix.cmxa threads.cmxa une_solution_pour_event.ml -o
   ppnu-event.exe && ./ppnu-event.exe ; done
*)

(* Par Philippe Wang *)

(** Creer quelques joueurs aleatoirement pour pouvoir tester ce mini
   programme. *)
let () =
  (* S'assurer du non determinisme ! *)
  Random.self_init ()

let jVec =
  (* Creation des joueurs. *)
  Array.init 300 (fun i -> fun () -> Random.int 50) 

(* Q14. *)
let cRes : (int * int) Event.channel =
  (* Canal permettant de transmettre des paires d'entiers. *)
  Event.new_channel ()

(* Variable globale necessaire pour repondre a Q18 au vu de la suite
   du programme. (On peut bien sûr faire autrement.)  *)
let continue : bool ref =
  ref true

(* Q15. *)
let arbitre : unit -> (int * int) = fun () ->
  (* Rend le plus petit nombre unique recu s'il existe, avec le numero
     du joueur; sinon, rend la paire (-1,-1). *)
  let module M =
    (* Module locale pour utiliser a moindre frais des arbres
       equilibres dont les noeuds sont indexes par des entiers. *)
    Map.Make(struct type t = int let compare = (-) end)
  in
  let memory : int M.t ref =
    (* La memoire pour stocker certains resultats des joueurs. *)
    ref M.empty 
  in
    for i = 1 to Array.length jVec do
      (* Lire le bon nombre de valeurs dans le canal,
         ce nombre correspond a la taille du vecteur jVec. *)
      let (j : int), (v:int) =
        (* Recevoir une paire (valeur*joueur) sur le canal. On synchronise
           tout de suite car ca ne sert a rien de continuer sinon.*)
        Event.sync (Event.receive cRes)
      in
        if M.mem v !memory then
          (* Si la valeur existait dans la memoire, alors elle n'est
             pas unique, on la marque avec le joueur -1 (par convention
             arbitraire). *)
          memory := M.add v (-1) (M.remove v !memory)
        else
          (* Sinon, on ajoute la valeur et le joueur dans la
             memoire. *)
          memory := M.add v j !memory
    done;
    (* On peut signaler a tous qu'il est inutile de continuer.  Ce
       thread etant le seul a ecrire dans la reference 'continue', il
       n'est pas necessaire de proteger cette ecriture. *)
    continue := false;
    if M.is_empty !memory then
      (* Si la memoire est vide, alors c'est qu'il n'y avait aucun
         nombre unique. *)
      (-1, -1)
    else
      (* Sinon, il faut chercher le plus petit nombre unique. *)
      (let rec find () =
        if M.is_empty !memory then
          (-1, -1)
        else
          match M.min_binding !memory with
            | k, -1 -> memory := M.remove k !memory; find()
            | v, j -> v, j
        in find ())
        
        (* Plus court aurait ete les 2 lignes suivantes : 
           try M.min_binding (M.filter (fun v j -> j <> -1) !memory)
           with Not_found -> -1, -1
           mais cela est moins efficace du point de vue des performances
           car on parcourt alors l'ensemble de l'arbre d'abord.
        *)


(* Q16. *)
let cFun : (int * (unit -> int)) Event.channel =
  (* Canal permettant de transmettre les numeros des joueurs 
     avec leurs fonctions. *)
  Event.new_channel ()

(* Q17. La creation même du thread sera plus tard...
   mais elle aurait pu se faire tout de suite. *)
let send = fun () ->
  (* On cree un thread a chaque fois pour s'assurer que les envois
     peuvent se faire (presque) n'importe quand.  Pas obligatoire
     d'attendre la mort des threads (parce qu'on est en OCaml et qu'on
     beneficie d'un GC qui va se charger de faire cela et qu'on n'a
     pas besoin de le faire explicitement dans notre cas precis). *)
  Array.iteri
    (fun i j ->
      ignore (Thread.create
                (fun () -> Event.sync (Event.send cFun (i, j))) ()))
    jVec

(* Q18. *)
let make_threads = fun nTh ->
  (* Les nTh threads qui vont se charger d'executer les fonctions. *)
  for i = 1 to nTh do
    ignore
      (Thread.create
         (fun () ->
           while !continue do
              (* En fait, on pourrait mettre true a la place de
                 !continue car on ne fait qu'un seul tour. *)
             let ef = Event.receive cFun in
             let (j:int), (f:unit -> int) = Event.sync ef in
             Event.sync (Event.send cRes (j, f ()))
            (* On a un risque d'avoir des threads qui ne meurent
               pas... Mais d'apres les specifications du
               programme, comme ces threads ne sont pas attendus
               et qu'il ne s'agit pas du thread principal, ce
               n'est pas grave qu'ils restent passivement
               bloques sur une attente (Event.sync). *)
           done)
         ())
  done

let _ =
  (* lancement du programme... *)
  ignore (Thread.create send ());
  ignore (Thread.create make_threads 42);
  let nombre, gagnant = arbitre ()
  in Printf.printf "Le gagnant est %d avec le nombre %d\n%!" gagnant nombre
