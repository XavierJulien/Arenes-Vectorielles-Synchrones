/************************** ft_await_broadcast.c **************************/

/***** A compiler avec

   gcc -m32 -o ft_await_broadcast ft_await_broadcast.c \
       traceinstantsf.c -I $CHEMIN/ft_v1.1/include -L $CHEMIN/ft_v1.1/lib \
       -lfthread -lpthread
*****/


#include "fthread.h"
#include "stdio.h"
#include "unistd.h"
#include "traceinstantsf.h"


ft_thread_t    ft_trace;
ft_thread_t    ft_awaiter[3];
ft_scheduler_t sched;
ft_event_t     evt;

ft_thread_t    ft_broadcastor;


void awaiter (void *arg)
{
  int i, n, res;

  for (i = 0; i< 5; i++) {
    fprintf(stdout, "debut awater%d.\n", (int)arg);
    res = ft_thread_await(evt);
    if (res == OK) {
      ft_thread_get_value(evt, 0, (void *)&n);
      fprintf(stdout,
	      "event number %d recu par awaiter%d!\n",
	      n, (int)arg);
    }

    fprintf(stdout, "awaiter%d coopere %d instants.\n", (int)arg, (int)arg);
    ft_thread_cooperate_n((int)arg);
  }
  fprintf(stdout, "stop generator par awaiter%d!\n", (int)arg);
  ft_scheduler_stop((ft_thread_t)ft_broadcastor);
}


void broadcastor (void *arg)
{
  int i;

  for (i=0; i < 10; ++i) {
    fprintf(stdout, "event number %d generated!\n", i);
    ft_scheduler_broadcast_value(evt, (void *)i);
    ft_thread_cooperate_n(7);
  }
}


void autre_traceinstants (void *a)
{int i;
 int j = (int)a; 
 for (i=0;i<j;i++) {
    fprintf (stdout,">>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant %d :\n",i);
    ft_thread_cooperate ();
  }
  fprintf(stdout, "Last exit\n");
}


int main (void)
{
  int i;

  ft_scheduler_t autre_sched;

  sched       = ft_scheduler_create();
  autre_sched = ft_scheduler_create();
  evt         = ft_event_create(sched);

  /***** du meme sched *****/
  ft_trace = ft_thread_create(sched, traceinstants, NULL, (void *)50);
  for (i = 0; i < 3; ++i) {
    ft_awaiter[i] = ft_thread_create(sched, awaiter, NULL, (void *)i+1);
  }

  ft_scheduler_start(sched);



  /***** d'un autre_sched *****/
  ft_broadcastor = ft_thread_create(autre_sched, broadcastor, NULL, NULL);
  ft_thread_create(autre_sched, autre_traceinstants, NULL, (void *)50);

  ft_scheduler_start(autre_sched);


  
  fprintf(stdout, "\n\nC'est fini pour le programme principal.\n");

  ft_exit();

  return 0;
}


/*

$ ft_await_broadcast 
>>>>>>>>>>> instant 0 :
debut awater1.
debut awater2.
debut awater3.
>>>>>>>>>>> instant 1 :
>>>>>>>>>>> instant 2 :
>>>>>>>>>>> instant 3 :
event number 0 generated!
>>>>>>>>>>> instant 4 :
event number 0 recu par awaiter1!
awaiter1 coopere 1 instants.
event number 0 recu par awaiter2!
awaiter2 coopere 2 instants.
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 0 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 1 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 2 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 3 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 4 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 5 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 6 :
event number 1 generated!
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 7 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 8 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 9 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 10 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 11 :
event number 0 recu par awaiter3!
awaiter3 coopere 3 instants.


C'est fini pour le programme principal.
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 12 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 13 :
>>>>>>>>>>> instant 5 :
debut awater1.
event number 1 recu par awaiter1!
awaiter1 coopere 1 instants.
event number 2 generated!
>>>>>>>>>>> instant 6 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 14 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 15 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 16 :
debut awater1.
debut awater2.
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 17 :
>>>>>>>>>>> instant 7 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 18 :
event number 2 recu par awaiter1!
awaiter1 coopere 1 instants.
event number 2 recu par awaiter2!
awaiter2 coopere 2 instants.
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 19 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 20 :
event number 3 generated!
debut awater3.
event number 2 recu par awaiter3!
awaiter3 coopere 3 instants.
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 21 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 22 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 23 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 24 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 25 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 26 :
>>>>>>>>>>> instant 8 :
debut awater1.
event number 3 recu par awaiter1!
awaiter1 coopere 1 instants.
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 27 :
>>>>>>>>>>> instant 9 :
event number 4 generated!
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 28 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 29 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 30 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 31 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 32 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 33 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 34 :
event number 5 generated!
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 35 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 36 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 37 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 38 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 39 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 40 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 41 :
event number 6 generated!
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 42 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 43 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 44 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 45 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 46 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 47 :
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 48 :
event number 7 generated!
>>>>>>>>>>>>>>>>>>>>>>>> autre_sched - instant 49 :
Last exit
event number 8 generated!
event number 9 generated!
debut awater1.
debut awater2.
>>>>>>>>>>> instant 10 :
event number 4 recu par awaiter1!
awaiter1 coopere 1 instants.
event number 4 recu par awaiter2!
awaiter2 coopere 2 instants.
debut awater3.
event number 4 recu par awaiter3!
awaiter3 coopere 3 instants.
>>>>>>>>>>> instant 11 :
stop generator par awaiter1!
>>>>>>>>>>> instant 12 :
debut awater2.
>>>>>>>>>>> instant 13 :
debut awater3.
>>>>>>>>>>> instant 14 :
>>>>>>>>>>> instant 15 :
>>>>>>>>>>> instant 16 :
>>>>>>>>>>> instant 17 :
>>>>>>>>>>> instant 18 :
>>>>>>>>>>> instant 19 :
>>>>>>>>>>> instant 20 :
>>>>>>>>>>> instant 21 :
>>>>>>>>>>> instant 22 :
>>>>>>>>>>> instant 23 :
>>>>>>>>>>> instant 24 :
>>>>>>>>>>> instant 25 :
>>>>>>>>>>> instant 26 :
>>>>>>>>>>> instant 27 :
>>>>>>>>>>> instant 28 :
>>>>>>>>>>> instant 29 :
>>>>>>>>>>> instant 30 :
>>>>>>>>>>> instant 31 :
>>>>>>>>>>> instant 32 :
>>>>>>>>>>> instant 33 :
>>>>>>>>>>> instant 34 :
>>>>>>>>>>> instant 35 :
>>>>>>>>>>> instant 36 :
>>>>>>>>>>> instant 37 :
>>>>>>>>>>> instant 38 :
>>>>>>>>>>> instant 39 :
>>>>>>>>>>> instant 40 :
>>>>>>>>>>> instant 41 :
>>>>>>>>>>> instant 42 :
>>>>>>>>>>> instant 43 :
>>>>>>>>>>> instant 44 :
>>>>>>>>>>> instant 45 :
>>>>>>>>>>> instant 46 :
>>>>>>>>>>> instant 47 :
>>>>>>>>>>> instant 48 :
>>>>>>>>>>> instant 49 :
Last exit

*/
