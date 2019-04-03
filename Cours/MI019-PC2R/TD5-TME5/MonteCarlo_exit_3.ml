(* dessin: un cercle de rayon 1 dans un carré de coté 2 *)

(***** A compiler avec

   ocamlc -thread unix.cma threads.cma nums.cma MonteCarlo_exit_3.ml \
   	-o MonteCarlo_exit_3

*****)


open Printf;;
open Num;;
open Event;;


type info =
    De_main
  | Du_testeur of float;;

let voie:(info channel) = new_channel();;
let pi                  = ref 0.;;
let ok                  = ref(num_of_int 0);;
let pasok               = ref(num_of_int 0);;
let clefok              = Mutex.create();;
let clefpasok           = Mutex.create();;

let compteur (numero, channel) =
  while true do
    match sync (receive channel) with
        De_main -> Thread.exit()
      | Du_testeur x
          -> printf "Compteur %d " numero;
             if x < 1. then
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
      sync (send channel (Du_testeur (x *. x +. y *. y)))
  done;;

let main () =
  let th1 = Thread.create compteur (1, voie)
      and th2 = Thread.create compteur (2, voie)
      and th3 = Thread.create testeur (1, int_of_string Sys.argv.(1), voie)
      and th4 = Thread.create testeur (2, int_of_string Sys.argv.(1), voie) in

    Thread.delay 0.1;          (* le temps pour les printf *)
    Thread.join th3;
    print_string "Fin th3\n";

    Thread.delay 0.1;          (* le temps pour les printf *)
    Thread.join th4;
    print_string "Fin th4\n";

    sync (send voie De_main);
    sync (send voie De_main);

    Thread.join th1;
    print_string "Fin th1\n";
    Thread.join th2;
    print_string "Fin th2\n";

    pi:=((float_of_num !ok)/.((float_of_num !pasok)+.(float_of_num !ok)))*.4.;
    print_float !pi;
    print_newline();
    
    print_string "j'ai fini\n";
    exit 0;;                   (* un peu violent *)

main ();;

