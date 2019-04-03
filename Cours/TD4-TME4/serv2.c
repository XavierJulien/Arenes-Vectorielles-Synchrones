#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>
#include<sys/types.h>
#include<sys/socket.h>
#include <netinet/ip.h>
#include<time.h>
#include <netdb.h>

int main()
{
	int sd,sd_serv;
	struct sockaddr_in adresse,addserv;
	struct hostent *resbyname;
	int length=sizeof(addserv);
	resbyname=gethostbyname("localhost");
	adresse.sin_family = AF_INET;
	adresse.sin_port=htons(12345);
	adresse.sin_addr.s_addr//=htonl(INADDR_ANY);
	  =htonl(atoi(resbyname->h_addr_list[0]));
	
	sd=socket(PF_INET,SOCK_STREAM,0);
	if(bind(sd,(struct sockaddr *) &adresse,
		sizeof(struct sockaddr_in))==-1)
	  {fprintf(stderr,"cagatte de bind\n"); exit(1);}
	listen(sd,10);
	sd_serv=accept(sd,(struct sockaddr*)&addserv,&length);
	printf("hello world %d\n",sd_serv);
	close(sd_serv);close(sd);
}
