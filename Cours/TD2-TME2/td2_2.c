/******************************* td2_2.canevas *******************************/

#include "fthread.h"
#include "stdio.h"
#include "unistd.h"
#include "traceinstantsf.h"

ft_event_t  evt;

void awaiter (void *args)
{
  int i, res;

  for (i = 0; i< 5; i++) {
    fprintf(stdout, "debut awater\n");
    ft_thread_await(evt);
    printf("Evenement reçu\n");
    ft_thread_cooperate_n(3);
    
  }  
  fprintf(stdout, "stop generator!\n");
  ft_scheduler_stop((ft_thread_t) args);
}

void generator (void *args)
{
  int i;

  for (i=0;; ++i) {
	ft_thread_cooperate_n(7);
	printf("Evenement numero %d envoyé.\n", i);
	ft_thread_generate (evt);
  }
}


int main (void)
{
	ft_thread_t ft_trace, ft_awaiter, ft_generator;
	ft_scheduler_t sched = ft_scheduler_create ();
	evt = ft_event_create(sched);
	ft_trace = ft_thread_create(sched,traceinstants,NULL,(void*)40);
	ft_generator = ft_thread_create(sched,generator,NULL,NULL);
	ft_awaiter = ft_thread_create(sched, awaiter,NULL,ft_generator);
	  
    ft_scheduler_start(sched);
    
    ft_exit ();
    return 0;
}
