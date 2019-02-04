#include <stdlib.h> 
#include <stdio.h> 
#include <sched.h>
#include <pthread.h> 
#include <unistd.h>


int main(void){
	
	int pid = fork();
	printf("Mon retour fork est : %d \n",pid);
	return 0;
}
