/********
* ec2c version 0.67
* c file generated for node : modulo_n_clock 
* context   method = HEAP
* ext call  method = PROCEDURES
********/
#include <stdlib.h>
#include <string.h>
#define _modulo_n_clock_EC2C_SRC_FILE
#include "modulo_n_clock.h"
/*--------
Internal structure for the call
--------*/
typedef struct  {
   void* client_data;
   //INPUTS
   _integer _n;
   //OUTPUTS
   _integer _max_modulo;
   _integer _nombre;
   //REGISTERS
   _integer M13;
   _boolean M13_nil;
   _integer M10;
   _boolean M10_nil;
   _boolean M5;
} modulo_n_clock_ctx;
/*--------
Output procedures must be defined,
Input procedures must be used:
--------*/
void modulo_n_clock_I_n(modulo_n_clock_ctx* ctx, _integer V){
   ctx->_n = V;
}
extern void modulo_n_clock_O_max_modulo(void* cdata, _integer);
extern void modulo_n_clock_O_nombre(void* cdata, _integer);
#ifdef CKCHECK
extern void modulo_n_clock_BOT_max_modulo(void* cdata);
extern void modulo_n_clock_BOT_nombre(void* cdata);
#endif
/*--------
Internal reset input procedure
--------*/
static void modulo_n_clock_reset_input(modulo_n_clock_ctx* ctx){
   //NOTHING FOR THIS VERSION...
}
/*--------
Reset procedure
--------*/
void modulo_n_clock_reset(modulo_n_clock_ctx* ctx){
   ctx->M13_nil = _true;
   ctx->M10_nil = _true;
   ctx->M5 = _true;
   modulo_n_clock_reset_input(ctx);
}
/*--------
Copy the value of an internal structure
--------*/
void modulo_n_clock_copy_ctx(modulo_n_clock_ctx* dest, modulo_n_clock_ctx
* src){
   memcpy((void*)dest, (void*)src, sizeof(modulo_n_clock_ctx));
}
/*--------
Dynamic allocation of an internal structure
--------*/
modulo_n_clock_ctx* modulo_n_clock_new_ctx(void* cdata){
   modulo_n_clock_ctx* ctx = (modulo_n_clock_ctx*)calloc(1, sizeof(
modulo_n_clock_ctx));
   ctx->client_data = cdata;
   modulo_n_clock_reset(ctx);
   return ctx;
}
/*--------
Step procedure
--------*/
void modulo_n_clock_step(modulo_n_clock_ctx* ctx){
//LOCAL VARIABLES
   _integer L8;
   _integer L4;
   _integer L3;
   _boolean L2;
   _integer L12;
   _integer L11;
   _integer L1;
   _integer T13;
   _integer T10;
//CODE
   L8 = (1 + ctx->M10);
   if (ctx->M5) {
      L4 = 0;
   } else {
      L4 = L8;
   }
   L3 = (L4 % ctx->_n);
   L2 = (L3 == 0);
   L12 = (L4 + ctx->_n);
   L11 = (L12 - ctx->_n);
   if (L2) {
      L1 = L11;
   } else {
      L1 = ctx->M13;
   }
   modulo_n_clock_O_max_modulo(ctx->client_data, L1);
   modulo_n_clock_O_nombre(ctx->client_data, L4);
   T13 = L11;
   T10 = L4;
   if(L2) {ctx->M13 = T13;
      ctx->M13_nil = _false;
   }
   ctx->M10 = T10;
   ctx->M10_nil = _false;
   ctx->M5 = ctx->M5 && !(_true);
}
