/********************* ft_await_generate_avec_fin.c **********************/

/***** A compiler avec

   gcc -m32 -o ft_await_generate_avec_fin ft_await_generate_avec_fin.c \
       traceinstantsf.c -I $CHEMIN/ft_v1.1/include -L $CHEMIN/ft_v1.1/lib \
       -lfthread -lpthread
*****/


#include "fthread.h"
#include "stdio.h"
#include "unistd.h"
#include "traceinstantsf.h"
#include "stdlib.h"
#include "pthread.h"


ft_thread_t    ft_trace, ft_generator;
ft_thread_t    ft_awaiter[3];
ft_scheduler_t sched;
ft_event_t     evt;

pthread_mutex_t fin;


void awaiter (void *arg)
{
  int i, n, res;

  for (i = 0; i< 5; i++) {
    fprintf(stdout, "debut awater%d.\n", (int)arg);

    /*****
	  ft_thread_await(evt) permet au thread courant de se mettre en
	  attente dans la liste d'evenement evt.
	  Si dans l'instant courant l'evenement est genere, tous les threads
	  de la liste d'evenement evt sont liberes et continuent leur
	  execution dans l'instant courant.

	  Attention, sur le meme sched, un seul thread s'execute, les autres
	  se mettent en attente de leur tour par cooperation.

	  Attention, on fait un await d'un evenement appartenant au meme
	  sched que l'appel de await. Sinon, ca renvoie une erreur sans
	  etre bloquant.
    *****/
    res = ft_thread_await(evt);
    if (res == OK) {

      /*****
	    Plusieurs evenements differents peuvent etre generes dans un meme
	    instant.
	    Mais un meme evenement peut aussi etre genere plusieurs fois dans
	    un meme instant.
	    Plusieurs meme evt peuvent etre generes dans le meme instant.
	    Le 0 de ft_thread_get_value(evt, 0, (void *)&n) indique que l'on
	    veut la valeur du premier meme evt genere.
	    Si on choisit 1 a la place, cela indique que c'est la valeur du
	    2eme meme evt genere, etc, ...

	    Attention : si l'evt n'existe pas ou ce nombre est plus grand que
	    le nombre (- 1) du meme evt, ca renvoie l'erreur NEXT avec
	    n <== NULL et ft_thread_get_value() termine au prochain instant.
      *****/
      ft_thread_get_value(evt, 0, (void *)&n);
      fprintf(stdout,
	      "evenement numero %d recu par awaiter%d!\n",
	      n, (int)arg);
    }

    fprintf(stdout, "awaiter%d coopere %d instants.\n", (int)arg, (int)arg);
    ft_thread_cooperate_n((int)arg);
  }
  fprintf(stdout, "stoper le generateur par awaiter%d!\n", (int)arg);
  ft_scheduler_stop(ft_generator);
}


void generator (void *arg)
{
  int i;

  for (i=0;; ++i) {
    fprintf(stdout, "demande de generer l'evenement numero %d ?\n", i);

    /*****
	  Attention : on genere un evt qui appartient au meme sched sinon
	  ca renvoie une erreur. Pire ==> ca bloque.
    *****/
    ft_thread_generate_value(evt, (void *)i);
    ft_thread_cooperate_n(7);
  }
}


void join_awaiters (void *arg)
{
  int i;
  pthread_t pthread_sched;

  pthread_mutex_lock(&fin);

  /*****
	Les joins ne servent pas vraiment ici car les awaiters et generator
	finissent très souvent avant tous leurs affichages sur stdout.

	Attention : le ft_thread qui appelle le join, doit etre dans le meme
	            ft_scheduler que les ft_threads passes en argument du join.

  *****/


	for (i = 0; i < 3; ++i) {
	  ft_thread_join(ft_awaiter[i]);
	}

	ft_scheduler_stop(ft_trace);

	pthread_mutex_unlock(&fin);

	fprintf(stdout, "********** Fin des joins. **********\n");

}



int main (void)
{
  int i;

  pthread_mutex_init(&fin, NULL);

  sched = ft_scheduler_create ();

  evt = ft_event_create(sched);

  ft_trace	= ft_thread_create(sched, traceinstants, NULL, (void *)50);
  ft_generator	= ft_thread_create(sched, generator, NULL, NULL);

  for (i = 0; i < 3; ++i) {
    ft_awaiter[i] = ft_thread_create(sched, awaiter, NULL, (void *)i+1);
  }

  ft_thread_create(sched, join_awaiters, NULL, NULL);

  ft_scheduler_start(sched);

  /* le temps que la clef fin soit prise par le ft_thread join_awaiters */
  sleep(5);

  /* la clef fin ne sera relachee par join_awaiters qui si tous les ft_awaiter
     du meme scheduler ont fini.
  */
  pthread_mutex_lock(&fin);
  
  fprintf(stdout, "\n\nC'est fini pour le programme principal.\n");


  /*****
	ft_exit() force le pthread qui fait tourner le main() à terminer et
	laisse les autres threads continuer.
	Il ne retourne jamais ==> il est bloquant.

	Ca laisse tout le temps aux autres threads d'afficher par le stdout.

	A la place de ft_exit() et pour cette exemple, on peut mettre par
	exemple sleep(50) qui permet aussi le temps aux autres threads
	d'afficher.
  *****/
  
  fprintf(stdout, "*********** Fin de programme. ********************\n");

  exit(0);
  return 0;
}





/*
$ ft_await_generate_avec_fin 
>>>>>>>>>>> instant 0 :
demande de generer l'evenement numero 0 ?
debut awater1.
evenement numero 0 recu par awaiter1!
awaiter1 coopere 1 instants.
debut awater2.
evenement numero 0 recu par awaiter2!
awaiter2 coopere 2 instants.
debut awater3.
evenement numero 0 recu par awaiter3!
awaiter3 coopere 3 instants.
>>>>>>>>>>> instant 1 :
debut awater1.
>>>>>>>>>>> instant 2 :
debut awater2.
>>>>>>>>>>> instant 3 :
debut awater3.
>>>>>>>>>>> instant 4 :
>>>>>>>>>>> instant 5 :
>>>>>>>>>>> instant 6 :
>>>>>>>>>>> instant 7 :
demande de generer l'evenement numero 1 ?
evenement numero 1 recu par awaiter1!
awaiter1 coopere 1 instants.
evenement numero 1 recu par awaiter2!
awaiter2 coopere 2 instants.
evenement numero 1 recu par awaiter3!
awaiter3 coopere 3 instants.
>>>>>>>>>>> instant 8 :
debut awater1.
>>>>>>>>>>> instant 9 :
debut awater2.
>>>>>>>>>>> instant 10 :
debut awater3.
>>>>>>>>>>> instant 11 :
>>>>>>>>>>> instant 12 :
>>>>>>>>>>> instant 13 :
>>>>>>>>>>> instant 14 :
demande de generer l'evenement numero 2 ?
evenement numero 2 recu par awaiter1!
awaiter1 coopere 1 instants.
evenement numero 2 recu par awaiter2!
awaiter2 coopere 2 instants.
evenement numero 2 recu par awaiter3!
awaiter3 coopere 3 instants.
>>>>>>>>>>> instant 15 :
debut awater1.
>>>>>>>>>>> instant 16 :
debut awater2.
>>>>>>>>>>> instant 17 :
debut awater3.
>>>>>>>>>>> instant 18 :
>>>>>>>>>>> instant 19 :
>>>>>>>>>>> instant 20 :
>>>>>>>>>>> instant 21 :
demande de generer l'evenement numero 3 ?
evenement numero 3 recu par awaiter1!
awaiter1 coopere 1 instants.
evenement numero 3 recu par awaiter2!
awaiter2 coopere 2 instants.
evenement numero 3 recu par awaiter3!
awaiter3 coopere 3 instants.
>>>>>>>>>>> instant 22 :
debut awater1.
>>>>>>>>>>> instant 23 :
debut awater2.
>>>>>>>>>>> instant 24 :
debut awater3.
>>>>>>>>>>> instant 25 :
>>>>>>>>>>> instant 26 :
>>>>>>>>>>> instant 27 :
>>>>>>>>>>> instant 28 :
demande de generer l'evenement numero 4 ?
evenement numero 4 recu par awaiter1!
awaiter1 coopere 1 instants.
evenement numero 4 recu par awaiter2!
awaiter2 coopere 2 instants.
evenement numero 4 recu par awaiter3!
awaiter3 coopere 3 instants.
>>>>>>>>>>> instant 29 :
stoper le generateur par awaiter1!
>>>>>>>>>>> instant 30 :
stoper le generateur par awaiter2!
>>>>>>>>>>> instant 31 :
stoper le generateur par awaiter3!
********** Fin des joins. **********


C'est fini pour le programme principal.
*********** Fin de programme. ********************

*/
