/********** alea.c **********/

#include <stdio.h>
#include <sys/types.h>
#include <time.h>
#include <limits.h>
#include <stdlib.h>

void init (int *sortieinutile, int entreeinutile) {
  time_t t1;

  (void) time(&t1);
  srand((long) t1);
}

void alea (int *resalea, int borne) {
  int rd;

  rd = rand();
  *resalea = (((double) rd) / RAND_MAX) * borne; 
}
