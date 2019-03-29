import java.io.BufferedReader;
import java.io.IOException;

public class Receive extends Thread {
  private Client client;
  private BufferedReader inchan;
  private String server_input;

  public Receive(Client c,BufferedReader br){
    client = c;
    inchan = br;
  }

  public void run(){
    while(true){
      try{
        server_input = inchan.readLine();
        String[] server_split = server_input.split("/");
        switch(server_split[0]){
          case "NEWPLAYER" : client.process_newplayer(server_split[1]);break;
          case "PLAYERLEFT" : client.process_playerleft(server_split[1]);break;
          case "SESSION" : client.process_session(server_split[1],server_split[2]);break;
          case "WINNER" : client.process_winner(server_split[1]);break;
          case "TICK" : client.process_tick(server_split[1]);break;
          case "NEWOBJ" : client.process_newobj(server_split[1],server_split[2]);break;
        }
      }catch(IOException e){
          System.out.println("je ne sait pas quand ça arrive mais fin de receive");break;
      }
    }
  }
}
