(* dessin: un cercle de rayon 1 dans un carré de coté 2 *)

(***** A compiler avec

   ocamlc -thread unix.cma threads.cma nums.cma MonteCarlo_exit_1.ml \
   	-o MonteCarlo_exit_1

*****)


open Printf;;
open Num;;
open Event;;


let voie      = new_channel();;
let pi        = ref 0.;;
let ok        = ref(num_of_int 0);;
let pasok     = ref(num_of_int 0);;
let clefok    = Mutex.create();;
let clefpasok = Mutex.create();;

let compteur (numero, channel) =
  while true do
    let x = (sync (receive channel)) in
      printf "Compteur %d " numero;
      if x then
        begin
          Mutex.lock clefok;
          incr_num ok;
          printf "Valeur OK = %d\n" (int_of_num !ok);
          Mutex.unlock clefok
        end
      else begin
             Mutex.lock clefpasok;
             incr_num pasok;
             printf "Valeur PAS_OK = %d\n" (int_of_num !pasok);
             Mutex.unlock clefpasok
           end
  done;;

let testeur (numero, nbop, channel) =
  for i = 1 to nbop do
    let x = (Random.float 2.0) -. 1.
        and y = (Random.float 2.0) -. 1. in
      printf "Testeur %d\n" numero;
      Thread.delay 0.1;
      (sync (send channel (x *. x +. y *. y < 1.0)))
  done;;

let main () =
  let th1 = Thread.create compteur (1, voie)
      and th2 = Thread.create compteur (2, voie)
      and th3 = Thread.create testeur (1, int_of_string Sys.argv.(1), voie)
      and th4 = Thread.create testeur (2, int_of_string Sys.argv.(1), voie) in

    Thread.delay 0.1;          (* Le temps pour les printf *)
    Thread.join th3;
    print_string "Fin th3\n";

    Thread.delay 0.1;          (* Le temps pour les printf *)
    Thread.join th4;
    print_string "Fin th4\n";

    Thread.delay 0.1;          (* Le temps pour les printf et compteur.
                                  Pas sur que les compteurs aient le temps
                                  de traiter le dernier OK ou PAS_OK.
                                  Meme avec un lock ou non.
                                  PAS TRES PROPRE *)
                               (* Un kill de th1 et/ou th2 est mauvais car
                                  on ne sait pas à ce niveau s'ils ont fini
                                  ou pas *)
    pi:=((float_of_num !ok)/.((float_of_num !pasok)+.(float_of_num !ok)))*.4.;
    print_float !pi;
    print_newline();
    
    print_string "j'ai fini\n";
    exit 0;;                   (* un peu violent *)

main ();;

