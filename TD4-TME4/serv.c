#include <sys/types.h>
#include <sys/socket.h>
#include <sys/un.h>
#include <stdlib.h>
#include <stdio.h>

int main()
{
	int sd,sd_serv;
	struct sockaddr_un adresse,addserv;
	adresse.sun_family = AF_UNIX;
//	adresse.sun_path=malloc(10);
	strcpy(adresse.sun_path,"/home/cl/sock");

	sd=socket(AF_UNIX,SOCK_STREAM,PF_UNIX);
	bind(sd,(struct sockaddr *) &adresse,sizeof(struct sockaddr_un));
	listen(sd,10);
	sd_serv=accept(sd,&addserv,sizeof(addserv));
	printf("hello world %d\n",sd_serv);

}
