#include "fthread.h"
#include "pthread.h"
#include "stdio.h"

void run_p (void *phrase) {
  while(1){
    fprintf(stderr,"%s \n", (char *)phrase);
    ft_thread_cooperate();
  }
}


int main(void) {
  
  ft_scheduler_t sched = ft_scheduler_create ();
  
  ft_thread_create (sched,run_p,NULL, (void *)"Belle marquise");
  ft_thread_create (sched,run_p,NULL, (void *)"vos beaux yeux");
  ft_thread_create (sched,run_p,NULL, (void *)"me font mourir");
  ft_thread_create (sched,run_p,NULL, (void *)"d'amour");
    
  ft_scheduler_start (sched); 
  
  ft_exit();
  return 0;
}
