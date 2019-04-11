

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
					System.out.println(server_input);
					String[] server_split = server_input.split("/");
					switch(server_split[0]){
					case "NEWPLAYER" : {client.process_newplayer(server_split[1]);break;}
					case "PLAYERLEFT" : client.process_playerleft(server_split[1]);break;
					case "SESSION" : client.process_session(server_split[1],server_split[2]);break;
					case "WINNER" : client.process_winner(server_split[1]);break;
					case "TICK" : client.process_tick(server_split[1]);break;
					case "NEWOBJ" : client.process_newobj(server_split[1],server_split[2]);break;
					case "DENIED" : client.process_denied(server_split[1]);break;
					}
				}else{/*System.out.println("on ne recoit rien");*/continue;}
			}catch(IOException e){
				System.out.println("Le serveur a envoyé une commande erronée");
			}
		}
	}
}
