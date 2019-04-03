/********
* ec2c version 0.67
* context   method = HEAP
* ext call  method = PROCEDURES
* c header file generated for node : modulo_n_clock 
* to be used with : modulo_n_clock.c 
********/
#ifndef _modulo_n_clock_EC2C_H_FILE
#define _modulo_n_clock_EC2C_H_FILE
/*-------- Predefined types ---------*/
#ifndef _EC2C_PREDEF_TYPES
#define _EC2C_PREDEF_TYPES
typedef int _boolean;
typedef int _integer;
typedef char* _string;
typedef double _real;
typedef double _double;
typedef float _float;
#define _false 0
#define _true 1
#endif
/*--------- Pragmas ----------------*/
//MODULE: modulo_n_clock 1 2
//IN: _integer n
//OUT: _integer max_modulo
//OUT: _integer nombre
#ifndef _modulo_n_clock_EC2C_SRC_FILE
/*--------Context type -------------*/
struct modulo_n_clock_ctx;
/*-------- Input procedures -------------*/
extern void modulo_n_clock_I_n(struct modulo_n_clock_ctx* ctx, _integer);
/*-------- Reset procedure -----------*/
extern void modulo_n_clock_reset(struct modulo_n_clock_ctx* ctx);
/*--------Context copy -------------*/
extern void modulo_n_clock_copy_ctx(struct modulo_n_clock_ctx* dest, struct 
modulo_n_clock_ctx* src);
/*--------Context allocation --------*/
extern struct modulo_n_clock_ctx* modulo_n_clock_new_ctx(void* client_data);
/*-------- Step procedure -----------*/
extern void modulo_n_clock_step(struct modulo_n_clock_ctx* ctx);
#endif
#endif
