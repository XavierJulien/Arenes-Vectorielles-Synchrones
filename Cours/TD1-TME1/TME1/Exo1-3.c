#include <stdlib.h> 
#include <stdio.h> 
#include <unistd.h>
#include<pthread.h> 


int main(void){

	int i = 0 ; 
	int res = fork();
		if(res){
			printf("Je suis le pere et ma variable res est %d\n", res);
			printf("Donne moi un nombre maintenant :");
			scanf("%d",&i);
			printf("Ma variable i vaut %d\n",i);
		}else{
			sleep(5);
			printf("Je suis le fils et ma variable res est %d \n ",res);
			printf("Ma variable i vaut %d\n", i);
		}
	return 0;
}
