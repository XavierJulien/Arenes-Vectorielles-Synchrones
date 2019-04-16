





import java.io.BufferedReader;
import java.io.IOException;


public class Receive extends Thread {

	private SpaceRun client;
	private BufferedReader inchan;
	private String server_input;
	private boolean isRunning;

	public Receive(SpaceRun c,BufferedReader br){
		client = c;
		inchan = br;
		isRunning = true;
	}

	public void setRunning(boolean b){isRunning = b;}

	public void run(){
		while(isRunning){
			try{
				server_input = inchan.readLine();
				if(server_input != null){
					//System.out.println(server_input);
					String[] server_split = server_input.split("/");
					switch(server_split[0]){
					case "NEWPLAYER" : {client.process_newplayer(server_split[1]);break;}
					case "PLAYERLEFT" : client.process_playerleft(server_split[1]);break;
					case "SESSION" : 
						if (server_split.length == 4) {
							client.process_session(server_split[1],server_split[2],server_split[3]);break;
						}else{
							System.out.println(server_input);
							client.process_session(server_split[1],server_split[2],server_split[3],server_split[4]);break;
							}
					case "WINNER" : client.process_winner(server_split[1]);break;
					case "TICK" :
						if (server_split.length == 2) client.process_tick(server_split[1]);
						if (server_split.length == 3) client.process_tick(server_split[1], server_split[2]);
						if (server_split.length == 4) client.process_tick(server_split[1], server_split[2],server_split[3]);
						break;
					case "NEWOBJ" : 
						if (server_split.length == 3) {
							client.process_newobj(server_split[1],server_split[2]);break;							
						}else{
							client.process_newobj(server_split[1],server_split[2],server_split[3]);break;
						}

					case "RECEPTION" :
						if (server_split.length == 2) {
							client.process_reception(server_split[1]);break;
						}else{
							client.process_reception(server_split[1], server_split[2]);break;
						}

					case "PRECEPTION" : client.process_preception(server_split[1], server_split[2]);break;
					case "NEXT" : client.process_next(server_split[1]);break;
					case "DENIED" : client.process_denied(server_split[1]);break;
					default : System.out.println("Le client ne reconnait pas la commande "+server_split[0]+" dans son protocole");
					}
				}else{continue;}
			}catch(IOException e){
				System.out.println(e.getMessage());
			}
		}
	}
}
