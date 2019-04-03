let c_sell = Event.new_channel ()
and c_brok = Event.new_channel ()
and c_buye1 = Event.new_channel ()
and c_buye2 = Event.new_channel ()
and log1 = ref ""
and log2 = ref ""

let rec seller n =
  let (y,z) = Event.sync (Event.receive c_sell) in
  Event.sync (Event.send y (z^" "^(string_of_int n)));
  seller (n+1)

    
let rec buyer arg =
  let (a,n,c_buye,log,varlog) = arg in
  if n == 0 then varlog:=log else
    begin
      Event.sync (Event.send c_brok (a,c_buye));
      let chan = Event.sync (Event.receive c_buye) in
      let prod = Event.sync (Event.receive chan) in      
      buyer (a,n-1,c_buye,(log^"J'avais demande "^a^", j'ai recu "^prod^".\n"),varlog)
    end



let rec broker () =
  let (x,c_buye) = Event.sync (Event.receive c_brok) 
  and nu_c = Event.new_channel () in
  Event.sync (Event.send c_sell (nu_c,x));
  Event.sync (Event.send c_buye nu_c);broker ()

let main = 
  let _ = Thread.create seller 1
  and _ = Thread.create broker ()
  and tbuy1 = Thread.create buyer ("the",3,c_buye1,"",log1)
  and tbuy2 = Thread.create buyer ("cafe",2,c_buye2,"",log2)
  in
  Thread.join tbuy1; 
  Thread.join tbuy2;
  print_endline !log1;
  print_endline !log2
