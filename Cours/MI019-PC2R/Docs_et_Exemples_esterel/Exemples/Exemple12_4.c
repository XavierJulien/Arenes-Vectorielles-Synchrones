/* sscc : C CODE OF SORTED EQUATIONS Exemple12_4 - SIMULATION MODE */

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
#define __Exemple12_4_GENERIC_TEST(TEST) return TEST;
typedef void (*__Exemple12_4_APF)();
static __Exemple12_4_APF *__Exemple12_4_PActionArray;
static int **__Exemple12_4_PCheckArray;
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

#include "Exemple12_4.h"

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
#ifndef _NO_FUNCTION_DEFINITIONS
#ifndef _max_DEFINED
#ifndef max
extern integer max(integer ,integer);
#endif
#endif
#endif
#endif

/* INITIALIZED CONSTANTS */

/* MEMORY ALLOCATION */

static integer __Exemple12_4_V0;
static integer __Exemple12_4_V1;
static boolean __Exemple12_4_V2;
static integer __Exemple12_4_V3;

static unsigned short __Exemple12_4_HaltList[7];
static unsigned short __Exemple12_4_AwaitedList[3];
static unsigned short __Exemple12_4_EmittedList[3];
static unsigned short __Exemple12_4_StartedList[1];
static unsigned short __Exemple12_4_KilledList[1];
static unsigned short __Exemple12_4_SuspendedList[1];
static unsigned short __Exemple12_4_ActiveList[1];
static unsigned short __Exemple12_4_AllAwaitedList[3]={0,};


/* INPUT FUNCTIONS */


/* FUNCTIONS RETURNING NUMBER OF EXEC */

int Exemple12_4_number_of_execs () {
return (0);
}


/* AUTOMATON (STATE ACTION-TREES) */

/* ACTIONS */

/* PREDEFINED ACTIONS */

/* PRESENT SIGNAL TESTS */

/* OUTPUT ACTIONS */

static void __Exemple12_4_A1 () {
#ifdef __OUTPUT
Exemple12_4_O_S1(__Exemple12_4_V0);
#endif
__AppendToList(__Exemple12_4_EmittedList,0);
}
static int __Exemple12_4_Check1 [] = {1,0,0};
static void __Exemple12_4_A2 () {
#ifdef __OUTPUT
Exemple12_4_O_S2(__Exemple12_4_V1);
#endif
__AppendToList(__Exemple12_4_EmittedList,1);
}
static int __Exemple12_4_Check2 [] = {1,0,0};

/* ASSIGNMENTS */

static void __Exemple12_4_A3 () {
__Exemple12_4_V2 = _false;
}
static int __Exemple12_4_Check3 [] = {1,0,1,2};
static void __Exemple12_4_A4 () {
__Exemple12_4_V1 = 0;
}
static int __Exemple12_4_Check4 [] = {1,0,1,1};
static void __Exemple12_4_A5 () {
__Exemple12_4_V3 = 0;
}
static int __Exemple12_4_Check5 [] = {1,0,1,3};
static void __Exemple12_4_A6 () {
__Exemple12_4_V3 = __Exemple12_4_V3+__Exemple12_4_V1;
}
static int __Exemple12_4_Check6 [] = {1,2,3,1,1,3};
static void __Exemple12_4_A7 () {
__Exemple12_4_V0 = __Exemple12_4_V3;
}
static int __Exemple12_4_Check7 [] = {1,1,3,1,0};
static void __Exemple12_4_A8 () {
__Exemple12_4_V1 = _COND(__Exemple12_4_V2,max(__Exemple12_4_V1,1),(__Exemple12_4_V2=_true,1));
}
static int __Exemple12_4_Check8 [] = {1,0,1,1};
static void __Exemple12_4_A9 () {
__Exemple12_4_V1 = _COND(__Exemple12_4_V2,max(__Exemple12_4_V1,2),(__Exemple12_4_V2=_true,2));
}
static int __Exemple12_4_Check9 [] = {1,0,1,1};
static void __Exemple12_4_A10 () {
__Exemple12_4_V1 = _COND(__Exemple12_4_V2,max(__Exemple12_4_V1,3),(__Exemple12_4_V2=_true,3));
}
static int __Exemple12_4_Check10 [] = {1,0,1,1};
static void __Exemple12_4_A11 () {
__Exemple12_4_V1 = _COND(__Exemple12_4_V2,max(__Exemple12_4_V1,4),(__Exemple12_4_V2=_true,4));
}
static int __Exemple12_4_Check11 [] = {1,0,1,1};

/* PROCEDURE CALLS */

/* CONDITIONS */

/* DECREMENTS */

/* START ACTIONS */

/* KILL ACTIONS */

/* SUSPEND ACTIONS */

/* ACTIVATE ACTIONS */

/* WRITE ARGS ACTIONS */

/* RESET ACTIONS */

static void __Exemple12_4_A12 () {
;
__ResetVariable(0);
}
static int __Exemple12_4_Check12 [] = {1,0,0};

/* ACTION SEQUENCES */


static int *__Exemple12_4_CheckArray[] = {
0,
__Exemple12_4_Check1,
__Exemple12_4_Check2,
__Exemple12_4_Check3,
__Exemple12_4_Check4,
__Exemple12_4_Check5,
__Exemple12_4_Check6,
__Exemple12_4_Check7,
__Exemple12_4_Check8,
__Exemple12_4_Check9,
__Exemple12_4_Check10,
__Exemple12_4_Check11,
__Exemple12_4_Check12
};
static int **__Exemple12_4_PCheckArray =  __Exemple12_4_CheckArray;

/* INIT FUNCTION */

#ifndef NO_INIT
void Exemple12_4_initialize () {
}
#endif

/* SHOW VARIABLE FUNCTION */

char* __Exemple12_4_show_variable (int __V) {
extern struct __VariableEntry __Exemple12_4_VariableTable[];
struct __VariableEntry* p_var = &__Exemple12_4_VariableTable[__V];
if (p_var->type_code < 0) {return __PredefinedTypeToText(p_var->type_code, p_var->p_variable);
} else {
switch (p_var->type_code) {
default: return 0;
}
}
}

/* SET VARIABLE FUNCTION */

static void __Exemple12_4_set_variable(int __Type, char* __pVar, char* __Text) {
}

/* CHECK VALUE FUNCTION */

static int __Exemple12_4_check_value (int __Type, char* __Text) {
return 0;
}

/* SIMULATION TABLES */

struct __InstanceEntry __Exemple12_4_InstanceTable [] = {
{"Exemple12_4",0,"","Exemple12_4.strl",{1,1,1,0},{1,21,1,0},{0,0,0,0}},
};

struct __SignalEntry __Exemple12_4_SignalTable [] = {
{"S1",2,0,0,{1,5,10,0},0,(void*) NULL,0,(void*) NULL,0,(void*) NULL},
{"S2",66,1,0,{1,5,25,0},0,(void*) NULL,0,(void*) NULL,0,(void*) NULL}};

struct __VariableEntry __Exemple12_4_VariableTable [] = {
{"__Exemple12_4_V0","V0","integer",-3,1,0,(char*)&__Exemple12_4_V0,"S1",0,0,0,{1,5,10,0}},
{"__Exemple12_4_V1","V1","integer",-3,1,0,(char*)&__Exemple12_4_V1,"S2",0,0,0,{1,5,25,0}},
{"__Exemple12_4_V2","V2","boolean",-2,2,0,(char*)&__Exemple12_4_V2,"S2",0,0,0,{1,5,25,0}},
{"__Exemple12_4_V3","V3","integer",-3,0,0,(char*)&__Exemple12_4_V3,"accumulateur",0,0,0,{1,7,7,0}}
};

struct __HaltEntry __Exemple12_4_HaltTable [] = {
{{1,21,1,0}},
{{1,9,7,0}},
{{1,11,7,0}},
{{1,13,7,0}},
{{1,15,7,0}},
{{1,17,7,0}}
};


static void __Exemple12_4__reset_input () {
__Exemple12_4_V2 = _false;
}


/* MODULE DATA FOR SIMULATION */

int Exemple12_4();
int Exemple12_4_reset();

static struct __ModuleEntry __Exemple12_4_ModuleData = {
"Simulation interface release 5","Exemple12_4",
1,0,2,0,0,0,2,0,0,0,0,4,0,6,0,0,0,
__Exemple12_4_HaltList,
__Exemple12_4_AwaitedList,
__Exemple12_4_EmittedList,
__Exemple12_4_StartedList,
__Exemple12_4_KilledList,
__Exemple12_4_SuspendedList,
__Exemple12_4_ActiveList,
0,0,
Exemple12_4_initialize,Exemple12_4,Exemple12_4_reset,
__Exemple12_4_show_variable,__Exemple12_4_set_variable,__Exemple12_4_check_value,0,
__Exemple12_4_InstanceTable,
0,
__Exemple12_4_SignalTable,0,0,
0,0,
__Exemple12_4_VariableTable,
0,
__Exemple12_4_HaltTable,
0};

/* REDEFINABLE BIT TYPE */

#ifndef __SSC_BIT_TYPE_DEFINED
typedef char __SSC_BIT_TYPE;
#endif

/* REGISTER VARIABLES */

static __SSC_BIT_TYPE __Exemple12_4_R[6] = {_true,
 _false,
 _false,
 _false,
 _false,
 _false};

/* AUTOMATON ENGINE */

int Exemple12_4 () {
/* AUXILIARY VARIABLES */

static __SSC_BIT_TYPE E[13];

__Exemple12_4_ModuleData.awaited_list = __Exemple12_4_AwaitedList;
__ResetModuleEntryBeforeReaction();
E[0] = __Exemple12_4_R[1]&&!(__Exemple12_4_R[0]);
E[1] = __Exemple12_4_R[2]&&!(__Exemple12_4_R[0]);
E[2] = __Exemple12_4_R[3]&&!(__Exemple12_4_R[0]);
E[3] = __Exemple12_4_R[4]&&!(__Exemple12_4_R[0]);
E[4] = __Exemple12_4_R[5]&&!(__Exemple12_4_R[0]);
E[5] = E[1]||E[2]||E[3]||E[4];
if (E[5]) {
__AppendToList(__Exemple12_4_EmittedList,1);
}
E[6] = E[0]&&E[5];
if (E[6]) {
__AppendToList(__Exemple12_4_EmittedList,0);
}
if (E[1]) {
__CheckVariables(__Exemple12_4_CheckArray[8]);__Exemple12_4_A8();
#ifdef TRACE_ACTION
fprintf(stderr, "__Exemple12_4_A8\n");
#endif
}
if (E[2]) {
__CheckVariables(__Exemple12_4_CheckArray[9]);__Exemple12_4_A9();
#ifdef TRACE_ACTION
fprintf(stderr, "__Exemple12_4_A9\n");
#endif
}
if (E[3]) {
__CheckVariables(__Exemple12_4_CheckArray[10]);__Exemple12_4_A10();
#ifdef TRACE_ACTION
fprintf(stderr, "__Exemple12_4_A10\n");
#endif
}
if (E[4]) {
__CheckVariables(__Exemple12_4_CheckArray[11]);__Exemple12_4_A11();
#ifdef TRACE_ACTION
fprintf(stderr, "__Exemple12_4_A11\n");
#endif
}
if (E[6]) {
__CheckVariables(__Exemple12_4_CheckArray[6]);__Exemple12_4_A6();
#ifdef TRACE_ACTION
fprintf(stderr, "__Exemple12_4_A6\n");
#endif
}
if (E[6]) {
__CheckVariables(__Exemple12_4_CheckArray[7]);__Exemple12_4_A7();
#ifdef TRACE_ACTION
fprintf(stderr, "__Exemple12_4_A7\n");
#endif
}
if (E[6]) {
__CheckVariables(__Exemple12_4_CheckArray[1]);__Exemple12_4_A1();
#ifdef TRACE_ACTION
fprintf(stderr, "__Exemple12_4_A1\n");
#endif
}
if (E[5]) {
__CheckVariables(__Exemple12_4_CheckArray[2]);__Exemple12_4_A2();
#ifdef TRACE_ACTION
fprintf(stderr, "__Exemple12_4_A2\n");
#endif
}
if (__Exemple12_4_R[0]) {
__CheckVariables(__Exemple12_4_CheckArray[12]);__Exemple12_4_A12();
#ifdef TRACE_ACTION
fprintf(stderr, "__Exemple12_4_A12\n");
#endif
}
if (__Exemple12_4_R[0]) {
__CheckVariables(__Exemple12_4_CheckArray[4]);__Exemple12_4_A4();
#ifdef TRACE_ACTION
fprintf(stderr, "__Exemple12_4_A4\n");
#endif
}
E[7] = !(_true);
E[8] = __Exemple12_4_R[1]||__Exemple12_4_R[2]||__Exemple12_4_R[3]||__Exemple12_4_R[4]||__Exemple12_4_R[5];
E[9] = (E[8]&&!(__Exemple12_4_R[1]))||E[6];
E[10] = (E[8]&&!(__Exemple12_4_R[2]))||E[1];
E[11] = (E[8]&&!(__Exemple12_4_R[3]))||E[2];
E[12] = (E[8]&&!(__Exemple12_4_R[4]))||E[3];
E[8] = (E[8]&&!(__Exemple12_4_R[5]))||E[4];
if (__Exemple12_4_R[0]) {
__CheckVariables(__Exemple12_4_CheckArray[5]);__Exemple12_4_A5();
#ifdef TRACE_ACTION
fprintf(stderr, "__Exemple12_4_A5\n");
#endif
}
__Exemple12_4_R[2] = ((E[6]||E[1]||E[2]||E[3]||E[4])&&E[9]&&E[10]&&E[11]&&E[12]&&E[8])||__Exemple12_4_R[0];
E[0] = __Exemple12_4_R[1]&&E[0]&&!(E[5]);
E[8] = __Exemple12_4_R[2]||(E[0]&&(E[9]||E[0])&&E[10]&&E[11]&&E[12]&&E[8]);
__Exemple12_4_R[1] = __Exemple12_4_R[2]||E[0];
__Exemple12_4_R[0] = !(_true);
if (__Exemple12_4_R[1]) { __AppendToList(__Exemple12_4_HaltList,1); }
if (__Exemple12_4_R[2]) { __AppendToList(__Exemple12_4_HaltList,2); }
__Exemple12_4_R[3] = __Exemple12_4_R[2];
if (__Exemple12_4_R[3]) { __AppendToList(__Exemple12_4_HaltList,3); }
__Exemple12_4_R[4] = __Exemple12_4_R[2];
if (__Exemple12_4_R[4]) { __AppendToList(__Exemple12_4_HaltList,4); }
__Exemple12_4_R[5] = __Exemple12_4_R[2];
if (__Exemple12_4_R[5]) { __AppendToList(__Exemple12_4_HaltList,5); }
if (!E[8]) { __AppendToList(__Exemple12_4_HaltList,0); }
__ResetModuleEntryAfterReaction();
__Exemple12_4_ModuleData.awaited_list = __Exemple12_4_AllAwaitedList;
__Exemple12_4__reset_input();
return E[8];
}

/* AUTOMATON RESET */

int Exemple12_4_reset () {
__Exemple12_4_ModuleData.awaited_list = __Exemple12_4_AwaitedList;
__ResetModuleEntry();
__Exemple12_4_ModuleData.awaited_list = __Exemple12_4_AllAwaitedList;
__Exemple12_4_ModuleData.state = 0;
__Exemple12_4_R[0] = _true;
__Exemple12_4_R[1] = _false;
__Exemple12_4_R[2] = _false;
__Exemple12_4_R[3] = _false;
__Exemple12_4_R[4] = _false;
__Exemple12_4_R[5] = _false;
__Exemple12_4__reset_input();
return 0;
}
char* CompilationType = "Compiled Sorted Equations";

int __NumberOfModules = 1;
struct __ModuleEntry* __ModuleTable[] = {
&__Exemple12_4_ModuleData
};
