#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>
#include<sys/types.h>
#include<sys/socket.h>
#include <netinet/ip.h>
#include<time.h>
#include <netdb.h>

int main(int argc, char** argv)
{
  char buff[20];
  struct sockaddr_in servaddress;
  struct hostent *resbyname;
  int sock = socket(PF_INET,SOCK_STREAM,0),i;
  int err;
  if (argc < 2) {
      fprintf(stderr,"il me faut 2 arguments!\n");
      exit(1);
  }
  printf("argv 1 %s\n",argv[1]);
  resbyname = gethostbyname(argv[1]);
  servaddress.sin_family = AF_INET;
  servaddress.sin_port = htons(atoi(argv[2]));
  servaddress.sin_addr.s_addr = atoi(resbyname->h_addr_list[0]);
  resbyname->h_addr_list[0][0] = '\0';
  err = connect(sock, (struct sockaddr*) &servaddress, sizeof(struct sockaddr_in));
  printf("err %d\n",err);fflush(stdout);
  for(i=0; i<3; i++) {
    write(sock, "Ping", 5); //pour le '\0'
    read(sock, buff, sizeof(buff));
    printf("J'ai lu %s\n", buff);
   }
  write(sock, "Toto", 5);//pour le '\0'
  read(sock, buff, sizeof(buff));
  printf("J'ai lu %s\n", buff);
  write(sock, "Exit", 5);//pour le '\0'
  read(sock, buff, sizeof(buff));
  printf("J'ai lu %s\n", buff);

  close(sock);
}

