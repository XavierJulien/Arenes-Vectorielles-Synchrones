node modulo_n_clock
  (n: int)
returns
  (max_modulo: int;
  nombre: int);

var
  V6_bool_clock: bool;
  (V24_n: int) when V6_bool_clock;

let
  max_modulo = (current ((((nombre) when V6_bool_clock) + V24_n) - V24_n));
  nombre = (0 -> (1 + (pre nombre)));
  V6_bool_clock = ((nombre mod n) = 0);
  V24_n = ((n) when V6_bool_clock);
tel

