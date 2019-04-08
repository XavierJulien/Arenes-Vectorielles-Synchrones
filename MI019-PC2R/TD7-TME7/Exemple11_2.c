/* sscc : C CODE OF SORTED EQUATIONS Exemple11_2 - SIMULATION MODE */

/* AUXILIARY DECLARATIONS */

#ifndef STRLEN
#define STRLEN 81
#endif
#define _COND(A,B,C) ((A)?(B):(C))
#ifdef TRACE_ACTION
#include <stdio.h>
#endif
#ifndef NULL
#define NULL ((char*)0)
#endif

#ifndef __EXEC_STATUS_H_LOADED
#define __EXEC_STATUS_H_LOADED

typedef struct {
unsigned int start:1;
unsigned int kill:1;
unsigned int active:1;
unsigned int suspended:1;
unsigned int prev_active:1;
unsigned int prev_suspended:1;
unsigned int exec_index;
unsigned int task_exec_index;
void (*pStart)();
void (*pRet)();
} __ExecStatus;

#endif
#define __ResetExecStatus(status) {\
   status.prev_active = status.active; \
   status.prev_suspended = status.suspended; \
   status.start = status.kill = status.active = status.suspended = 0; }
#define __DSZ(V) (--(V)<=0)
#define BASIC_TYPES_DEFINED
typedef int boolean;
typedef int integer;
typedef char* string;
#define CSIMUL_H_LOADED
typedef struct {char text[STRLEN];} symbolic;
extern void _boolean(boolean*, boolean);
extern boolean _eq_boolean(boolean, boolean);
extern boolean _ne_boolean(boolean, boolean);
extern boolean _cond_boolean(boolean ,boolean ,boolean);
extern char* _boolean_to_text(boolean);
extern int _check_boolean(char*);
extern void _text_to_boolean(boolean*, char*);
extern void _integer(integer*, integer);
extern boolean _eq_integer(integer, integer);
extern boolean _ne_integer(integer, integer);
extern integer _cond_integer(boolean ,integer ,integer);
extern char* _integer_to_text(integer);
extern int _check_integer(char*);
extern void _text_to_integer(integer*, char*);
extern void _string(string, string);
extern boolean _eq_string(string, string);
extern boolean _ne_string(string, string);
extern string _cond_string(boolean ,string ,string);
extern char* _string_to_text(string);
extern int _check_string(char*);
extern void _text_to_string(string, char*);
extern void _float(float*, float);
extern boolean _eq_float(float, float);
extern boolean _ne_float(float, float);
extern float _cond_float(boolean ,float ,float);
extern char* _float_to_text(float);
extern int _check_float(char*);
extern void _text_to_float(float*, char*);
extern void _double(double*, double);
extern boolean _eq_double(double, double);
extern boolean _ne_double(double, double);
extern double _cond_double(boolean ,double ,double);
extern char* _double_to_text(double);
extern int _check_double(char*);
extern void _text_to_double(double*, char*);
extern void _symbolic(symbolic*, symbolic);
extern boolean _eq_symbolic(symbolic, symbolic);
extern boolean _ne_symbolic(symbolic, symbolic);
extern symbolic _cond_symbolic(boolean ,symbolic ,symbolic);
extern char* _symbolic_to_text(symbolic);
extern int _check_symbolic(char*);
extern void _text_to_symbolic(symbolic*, char*);
extern char* __PredefinedTypeToText(int, char*);
#define _true 1
#define _false 0
#define __Exemple11_2_GENERIC_TEST(TEST) return TEST;
typedef void (*__Exemple11_2_APF)();
static __Exemple11_2_APF *__Exemple11_2_PActionArray;
static int **__Exemple11_2_PCheckArray;
struct __SourcePoint {
int linkback;
int line;
int column;
int instance_index;
};
struct __InstanceEntry {
char *module_name;
int father_index;
char *dir_name;
char *file_name;
struct __SourcePoint source_point;
struct __SourcePoint end_point;
struct __SourcePoint instance_point;
};
struct __TaskEntry {
char *name;
int   nb_args_ref;
int   nb_args_val;
int  *type_codes_array;
struct __SourcePoint source_point;
};
struct __SignalEntry {
char *name;
int code;
int variable_index;
int present;
struct __SourcePoint source_point;
int number_of_emit_source_points;
struct __SourcePoint* emit_source_point_array;
int number_of_present_source_points;
struct __SourcePoint* present_source_point_array;
int number_of_access_source_points;
struct __SourcePoint* access_source_point_array;
};
struct __InputEntry {
char *name;
int hash;
char *type_name;
int is_a_sensor;
int type_code;
int multiple;
int signal_index;
int (*p_check_input)(char*);
void (*p_input_function)();
int present;
struct __SourcePoint source_point;
};
struct __ReturnEntry {
char *name;
int hash;
char *type_name;
int type_code;
int signal_index;
int exec_index;
int (*p_check_input)(char*);
void (*p_input_function)();
int present;
struct __SourcePoint source_point;
};
struct __ImplicationEntry {
int master;
int slave;
struct __SourcePoint source_point;
};
struct __ExclusionEntry {
int *exclusion_list;
struct __SourcePoint source_point;
};
struct __VariableEntry {
char *full_name;
char *short_name;
char *type_name;
int type_code;
int comment_kind;
int is_initialized;
char *p_variable;
char *source_name;
int written;
unsigned char written_in_transition;
unsigned char read_in_transition;
struct __SourcePoint source_point;
};
struct __ExecEntry {
int task_index;
int *var_indexes_array;
char **p_values_array;
struct __SourcePoint source_point;
};
struct __HaltEntry {
struct __SourcePoint source_point;
};
struct __NetEntry {
int known;
int value;
int number_of_source_points;
struct __SourcePoint* source_point_array;
};
struct __ModuleEntry {
char *version_id;
char *name;
int number_of_instances;
int number_of_tasks;
int number_of_signals;
int number_of_inputs;
int number_of_returns;
int number_of_sensors;
int number_of_outputs;
int number_of_locals;
int number_of_exceptions;
int number_of_implications;
int number_of_exclusions;
int number_of_variables;
int number_of_execs;
int number_of_halts;
int number_of_nets;
int number_of_states;
int state;
unsigned short *halt_list;
unsigned short *awaited_list;
unsigned short *emitted_list;
unsigned short *started_list;
unsigned short *killed_list;
unsigned short *suspended_list;
unsigned short *active_list;
int run_time_error_code;
int error_info;
void (*init)();
int (*run)();
int (*reset)();
char *(*show_variable)(int);
void (*set_variable)(int, char*, char*);
int (*check_value)(int, char*);
int (*execute_action)();
struct __InstanceEntry* instance_table;
struct __TaskEntry* task_table;
struct __SignalEntry* signal_table;
struct __InputEntry* input_table;
struct __ReturnEntry* return_table;
struct __ImplicationEntry* implication_table;
struct __ExclusionEntry* exclusion_table;
struct __VariableEntry* variable_table;
struct __ExecEntry* exec_table;
struct __HaltEntry* halt_table;
struct __NetEntry* net_table;
};

#include "Exemple11_2.h"

/* EXTERN DECLARATIONS */

extern int __CheckVariables(int*);
extern void __ResetInput();
extern void __ResetExecs();
extern void __ResetVariables();
extern void __ResetVariableStatus();
extern void __AppendToList(unsigned short*, unsigned short);
extern void __ListCopy(unsigned short*, unsigned short**);
extern void __WriteVariable(int);
extern void __ResetVariable(int);
extern void __ResetModuleEntry();
extern void __ResetModuleEntryBeforeReaction();
extern void __ResetModuleEntryAfterReaction();
#ifndef _NO_EXTERN_DEFINITIONS
#ifndef _NO_PROCEDURE_DEFINITIONS
#ifndef _factoriel_DEFINED
#ifndef factoriel
extern void factoriel(integer* ,integer);
#endif
#endif
#endif
#endif

/* INITIALIZED CONSTANTS */

/* MEMORY ALLOCATION */

static integer __Exemple11_2_V0;
static boolean __Exemple11_2_V1;
static integer __Exemple11_2_V2;
static integer __Exemple11_2_V3;

static unsigned short __Exemple11_2_HaltList[3];
static unsigned short __Exemple11_2_AwaitedList[3];
static unsigned short __Exemple11_2_EmittedList[3];
static unsigned short __Exemple11_2_StartedList[1];
static unsigned short __Exemple11_2_KilledList[1];
static unsigned short __Exemple11_2_SuspendedList[1];
static unsigned short __Exemple11_2_ActiveList[1];
static unsigned short __Exemple11_2_AllAwaitedList[3]={1,0};


/* INPUT FUNCTIONS */

void Exemple11_2_I_FACT (integer __V) {
__WriteVariable(0);
__Exemple11_2_V0 = __V;
__Exemple11_2_V1 = _true;
}
void Exemple11_2_IS_FACT (string __V) {
__WriteVariable(0);
_text_to_integer(&__Exemple11_2_V0,__V);
__Exemple11_2_V1 = _true;
}

/* FUNCTIONS RETURNING NUMBER OF EXEC */

int Exemple11_2_number_of_execs () {
return (0);
}


/* AUTOMATON (STATE ACTION-TREES) */

/* ACTIONS */

/* PREDEFINED ACTIONS */

/* PRESENT SIGNAL TESTS */

static int __Exemple11_2_A1 () {
__Exemple11_2_GENERIC_TEST(__Exemple11_2_V1);
}
static int __Exemple11_2_Check1 [] = {1,0,0};

/* OUTPUT ACTIONS */

static void __Exemple11_2_A2 () {
#ifdef __OUTPUT
Exemple11_2_O_RESULTAT(__Exemple11_2_V2);
#endif
__AppendToList(__Exemple11_2_EmittedList,1);
}
static int __Exemple11_2_Check2 [] = {1,0,0};

/* ASSIGNMENTS */

static void __Exemple11_2_A3 () {
__Exemple11_2_V1 = _false;
}
static int __Exemple11_2_Check3 [] = {1,0,1,1};
static void __Exemple11_2_A4 () {
__Exemple11_2_V0 = 0;
}
static int __Exemple11_2_Check4 [] = {1,0,1,0};
static void __Exemple11_2_A5 () {
__Exemple11_2_V3 = 1;
}
static int __Exemple11_2_Check5 [] = {1,0,1,3};
static void __Exemple11_2_A6 () {
__Exemple11_2_V2 = __Exemple11_2_V3;
}
static int __Exemple11_2_Check6 [] = {1,1,3,1,2};

/* PROCEDURE CALLS */

static void __Exemple11_2_A7 () {
factoriel(&__Exemple11_2_V3,__Exemple11_2_V0);
}
static int __Exemple11_2_Check7 [] = {1,1,0,1,3};

/* CONDITIONS */

/* DECREMENTS */

/* START ACTIONS */

/* KILL ACTIONS */

/* SUSPEND ACTIONS */

/* ACTIVATE ACTIONS */

/* WRITE ARGS ACTIONS */

/* RESET ACTIONS */

static void __Exemple11_2_A8 () {
;
__ResetVariable(2);
}
static int __Exemple11_2_Check8 [] = {1,0,0};

/* ACTION SEQUENCES */


static int *__Exemple11_2_CheckArray[] = {
0,
__Exemple11_2_Check1,
__Exemple11_2_Check2,
__Exemple11_2_Check3,
__Exemple11_2_Check4,
__Exemple11_2_Check5,
__Exemple11_2_Check6,
__Exemple11_2_Check7,
__Exemple11_2_Check8
};
static int **__Exemple11_2_PCheckArray =  __Exemple11_2_CheckArray;

/* INIT FUNCTION */

#ifndef NO_INIT
void Exemple11_2_initialize () {
}
#endif

/* SHOW VARIABLE FUNCTION */

char* __Exemple11_2_show_variable (int __V) {
extern struct __VariableEntry __Exemple11_2_VariableTable[];
struct __VariableEntry* p_var = &__Exemple11_2_VariableTable[__V];
if (p_var->type_code < 0) {return __PredefinedTypeToText(p_var->type_code, p_var->p_variable);
} else {
switch (p_var->type_code) {
default: return 0;
}
}
}

/* SET VARIABLE FUNCTION */

static void __Exemple11_2_set_variable(int __Type, char* __pVar, char* __Text) {
}

/* CHECK VALUE FUNCTION */

static int __Exemple11_2_check_value (int __Type, char* __Text) {
return 0;
}

/* SIMULATION TABLES */

struct __InstanceEntry __Exemple11_2_InstanceTable [] = {
{"Exemple11_2",0,"","Exemple11_2.strl",{1,1,1,0},{1,19,1,0},{0,0,0,0}},
};

struct __SignalEntry __Exemple11_2_SignalTable [] = {
{"FACT",1,0,0,{1,3,10,0},0,(void*) NULL,0,(void*) NULL,0,(void*) NULL},
{"RESULTAT",2,2,0,{1,4,10,0},0,(void*) NULL,0,(void*) NULL,0,(void*) NULL}};

struct __InputEntry __Exemple11_2_InputTable [] = {
{"FACT",84,"integer",0,-3,0,0,_check_integer,Exemple11_2_IS_FACT,0,{1,3,10,0}}};

struct __VariableEntry __Exemple11_2_VariableTable [] = {
{"__Exemple11_2_V0","V0","integer",-3,1,0,(char*)&__Exemple11_2_V0,"FACT",0,0,0,{1,3,10,0}},
{"__Exemple11_2_V1","V1","boolean",-2,2,0,(char*)&__Exemple11_2_V1,"FACT",0,0,0,{1,3,10,0}},
{"__Exemple11_2_V2","V2","integer",-3,1,0,(char*)&__Exemple11_2_V2,"RESULTAT",0,0,0,{1,4,10,0}},
{"__Exemple11_2_V3","V3","integer",-3,0,0,(char*)&__Exemple11_2_V3,"aux",0,0,0,{1,11,13,0}}
};

struct __HaltEntry __Exemple11_2_HaltTable [] = {
{{1,19,1,0}},
{{1,16,5,0}}
};


static void __Exemple11_2__reset_input () {
__Exemple11_2_V1 = _false;
}


/* MODULE DATA FOR SIMULATION */

int Exemple11_2();
int Exemple11_2_reset();

static struct __ModuleEntry __Exemple11_2_ModuleData = {
"Simulation interface release 5","Exemple11_2",
1,0,2,1,0,0,1,0,0,0,0,4,0,2,0,0,0,
__Exemple11_2_HaltList,
__Exemple11_2_AwaitedList,
__Exemple11_2_EmittedList,
__Exemple11_2_StartedList,
__Exemple11_2_KilledList,
__Exemple11_2_SuspendedList,
__Exemple11_2_ActiveList,
0,0,
Exemple11_2_initialize,Exemple11_2,Exemple11_2_reset,
__Exemple11_2_show_variable,__Exemple11_2_set_variable,__Exemple11_2_check_value,0,
__Exemple11_2_InstanceTable,
0,
__Exemple11_2_SignalTable,__Exemple11_2_InputTable,0,
0,0,
__Exemple11_2_VariableTable,
0,
__Exemple11_2_HaltTable,
0};

/* REDEFINABLE BIT TYPE */

#ifndef __SSC_BIT_TYPE_DEFINED
typedef char __SSC_BIT_TYPE;
#endif

/* REGISTER VARIABLES */

static __SSC_BIT_TYPE __Exemple11_2_R[2] = {_true,
 _false};

/* AUTOMATON ENGINE */

int Exemple11_2 () {
/* AUXILIARY VARIABLES */

static __SSC_BIT_TYPE E[5];

__Exemple11_2_ModuleData.awaited_list = __Exemple11_2_AwaitedList;
__ResetModuleEntryBeforeReaction();
E[0] = __Exemple11_2_R[1]&&!(__Exemple11_2_R[0]);
E[1] = E[0]&&(__CheckVariables(__Exemple11_2_CheckArray[1]),
#ifdef TRACE_ACTION
fprintf(stderr, "test 1\n"),
#endif
__Exemple11_2_A1());
E[2] = __Exemple11_2_R[0]&&!((__CheckVariables(__Exemple11_2_CheckArray[1]),
#ifdef TRACE_ACTION
fprintf(stderr, "test 1\n"),
#endif
__Exemple11_2_A1()));
if (E[2]) {
__CheckVariables(__Exemple11_2_CheckArray[4]);__Exemple11_2_A4();
#ifdef TRACE_ACTION
fprintf(stderr, "__Exemple11_2_A4\n");
#endif
}
if (__Exemple11_2_R[0]) {
__CheckVariables(__Exemple11_2_CheckArray[8]);__Exemple11_2_A8();
#ifdef TRACE_ACTION
fprintf(stderr, "__Exemple11_2_A8\n");
#endif
}
E[1] = E[1]||__Exemple11_2_R[0];
E[3] = E[1]&&(__CheckVariables(__Exemple11_2_CheckArray[1]),
#ifdef TRACE_ACTION
fprintf(stderr, "test 1\n"),
#endif
__Exemple11_2_A1());
if (E[3]) {
__AppendToList(__Exemple11_2_EmittedList,1);
}
if (E[3]) {
__CheckVariables(__Exemple11_2_CheckArray[5]);__Exemple11_2_A5();
#ifdef TRACE_ACTION
fprintf(stderr, "__Exemple11_2_A5\n");
#endif
}
if (E[3]) {
__CheckVariables(__Exemple11_2_CheckArray[7]);__Exemple11_2_A7();
#ifdef TRACE_ACTION
fprintf(stderr, "__Exemple11_2_A7\n");
#endif
}
if (E[3]) {
__CheckVariables(__Exemple11_2_CheckArray[6]);__Exemple11_2_A6();
#ifdef TRACE_ACTION
fprintf(stderr, "__Exemple11_2_A6\n");
#endif
}
if (E[3]) {
__CheckVariables(__Exemple11_2_CheckArray[2]);__Exemple11_2_A2();
#ifdef TRACE_ACTION
fprintf(stderr, "__Exemple11_2_A2\n");
#endif
}
E[4] = !(_true);
E[0] = E[0]&&!((__CheckVariables(__Exemple11_2_CheckArray[1]),
#ifdef TRACE_ACTION
fprintf(stderr, "test 1\n"),
#endif
__Exemple11_2_A1()));
E[1] = E[1]&&!((__CheckVariables(__Exemple11_2_CheckArray[1]),
#ifdef TRACE_ACTION
fprintf(stderr, "test 1\n"),
#endif
__Exemple11_2_A1()));
__Exemple11_2_R[1] = E[3]||(__Exemple11_2_R[1]&&E[0])||E[1];
E[1] = __Exemple11_2_R[1];
__Exemple11_2_R[0] = !(_true);
if (__Exemple11_2_R[1]) { __AppendToList(__Exemple11_2_HaltList,1); }
if (!E[1]) { __AppendToList(__Exemple11_2_HaltList,0); }
__ResetModuleEntryAfterReaction();
__Exemple11_2_ModuleData.awaited_list = __Exemple11_2_AllAwaitedList;
__Exemple11_2__reset_input();
return E[1];
}

/* AUTOMATON RESET */

int Exemple11_2_reset () {
__Exemple11_2_ModuleData.awaited_list = __Exemple11_2_AwaitedList;
__ResetModuleEntry();
__Exemple11_2_ModuleData.awaited_list = __Exemple11_2_AllAwaitedList;
__Exemple11_2_ModuleData.state = 0;
__Exemple11_2_R[0] = _true;
__Exemple11_2_R[1] = _false;
__Exemple11_2__reset_input();
return 0;
}
char* CompilationType = "Compiled Sorted Equations";

int __NumberOfModules = 1;
struct __ModuleEntry* __ModuleTable[] = {
&__Exemple11_2_ModuleData
};
