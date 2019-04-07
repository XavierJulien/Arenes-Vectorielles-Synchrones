
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import javafx.scene.shape.Circle;

public class Client {

  protected static final int PORT=2019;
  private final double turnit = 0.1;
  private final double thrustit = 4.0;
  private final double refresh_tickrate = 0.001; // toutes les secondes
  private Receive r;
  private BufferedReader inchan,input;
  private PrintStream outchan;
  private Timer timer = new Timer();
  private RefreshClientTask refreshTask;

  /***************************DATA****************************/
  private String my_name;
  private Player myself;
  private Map<String,Player> player_list; // le client courant fait partie de la liste
  private Point target;
  private boolean isPlaying;
  private ArrayList<Commands> cumulCmds;


  /****************************AUX****************************/

  public Client(BufferedReader inchan,PrintStream outchan){
    this.inchan = inchan;
    this.outchan = outchan;
    this.input = new BufferedReader(new InputStreamReader(System.in));
    this.player_list = new HashMap<>();
    this.target = null;
    this.isPlaying = false;
    this.cumulCmds = new ArrayList<>();
  }
  public void parse_status(String status) {
	  if (status == "jeu") {
		  isPlaying=true;
	  }
	  if (status == "attente") {
		 isPlaying = false;
	  }
  }
  public void parse_scores(String player_score_string){
	  String[] player_score_string_split = player_score_string.split("\\|");
	  if (player_list.isEmpty()) { // initialisation, le joueur vient de se connecter
		 for(int i = 0;i<player_score_string_split.length;i++){
			 String[] player_score = player_score_string_split[i].split("[:]");
		     Player p = new Player(player_score[0],Integer.parseInt(player_score[1]));
		     player_list.put(player_score[0], p);
	    		 if (player_score[0].equals(my_name)) {
	    			myself = p;
	    		 }
		 }
	  }else{ // mise à jour des scores
		  for (int i = 0; i<player_score_string_split.length; i++) {
		      String[] player_score = player_score_string_split[i].split("[:]");
			  player_list.get(player_score[0]).setScore(Integer.parseInt(player_score[1]));
		  }
	  }
  }
  public void parse_coords(String player_coord_string){
    String[] player_coord_string_split = player_coord_string.split("\\|");
    for(String p : player_coord_string_split) {
        String[] xy = p.split(":")[1].split("[XY]");
        double x = Double.parseDouble(xy[1]);
        double y = Double.parseDouble(xy[2]);
    		player_list.get(p.split(":")[0]).getVehicule().set_posX(x);
    		player_list.get(p.split(":")[0]).getVehicule().set_posY(y);
    }
  }
  public void parse_target(String coord_string){
    String[] pos_target = coord_string.split("[XY]");
    target = new Point(Double.parseDouble(pos_target[1]),Double.parseDouble(pos_target[2]));
  }

  /**************************CMDS*****************************/
  //je sais pas encore comment ou et comment elles peuvent être appelées..
  public void commandClock() {
	  Player me = player_list.get(my_name);
	  me.getVehicule().setAngle(me.getVehicule().getAngle()-turnit);
    //me.getVehicule().clock();
	  //cumulCmds.add(Commands.clock);
  }
  public void commandAnticlock() {
	  Player me = player_list.get(my_name);
	  me.getVehicule().setAngle(me.getVehicule().getAngle()+turnit);
    //me.getVehicule().anticlock();
	  //cumulCmds.add(Commands.anticlock);
  }
  public void commandThrust() {
	  /**
	   * vx+turnit∗cos(θ), vy+turnit∗sin(θ)
	   */
	  Player me = player_list.get(my_name);
	  double new_vx = me.getVehicule().get_speedX()+turnit*Math.cos(me.getVehicule().getAngle());
	  double new_vy = me.getVehicule().get_speedY()+turnit*Math.sin(me.getVehicule().getAngle());
	  me.getVehicule().set_speedXY(new_vx, new_vy);
    //me.getVehicule().thrust();
	  //cumulCmds.add(Commands.thrust);
  }

/**
  public Ship parse_car(String coord_string){
    String[] pos_target = coord_string.split("[X,Y]");
    return new Ship(new Circle(500,500,20),Double.parseDouble(pos_target[0]),Double.parseDouble(pos_target[1]));
  }
*
*/

  /******************PROCESS_SERVER_REQUESTS******************/
  public void process_welcome(String[] server_input){
    //PARSE PHASE
    parse_status(server_input[1]);
    //PARSE PLAYER_SCORE met directement les valeurs dans la structure au lieu de retourner
    parse_scores(server_input[2]);
    //PARSE COORD
    parse_target(server_input[3]);
  }
  public void process_newplayer(String new_user){
    System.out.println("newplayer : "+new_user);
    player_list.put(new_user,new Player(new_user,0));
  }
  public void process_denied(String error){
    System.out.println("Error : DENIED/"+error);
  }
  public void process_playerleft(String name){
    System.out.println("playerleft : "+name);
    player_list.remove(name);

  }
  public void process_session(String coords,String coord){
    parse_target(coord);
    parse_coords(coords);
    isPlaying = true;
    refreshTask = new RefreshClientTask(myself);
    timer.scheduleAtFixedRate(refreshTask, 0,(int)(1/refresh_tickrate));
    //System.out.println("session : "+coords+" "+coord);
    //démarrer la task de clientrefresh ici
  }
  public void process_winner(String scores){
    parse_scores(scores);
    System.out.println("Fin de Session -> RESULTATS :");
    player_list.forEach((k,v) -> System.out.println("player "+(k+" -> "+v.getScore()+" points.")));
    isPlaying = false;
    refreshTask.cancel();
  }
  public void process_tick(String coords){
    parse_coords(coords);
  }
  public void process_newobj(String coord,String scores){
    parse_target(coord);
    parse_scores(scores);
    System.out.println("new_obj : " + coord);
  }
  public void communicate(String[] server_split) throws IOException {
    process_welcome(server_split);//met a jour les données avec le server_input du welcome
    r = new Receive(this,inchan);//thread d'écoute de requetes serveur
    Thread ihm = new Thread() {@Override public void run() {javafx.application.Application.launch(SpaceRun.class);}};
    ihm.start();
    r.start();
    String client_input;
    while(true){
      System.out.print("?"); System.out.flush();
      client_input = input.readLine(); //lecture commande du client
      String[] client_split = client_input.split("/");
      if(client_split[0].equals("EXIT")){ // à ce stade, la seule commande qu'il peut émettre est exit de lui meme
        if(client_split[1].equals(my_name)){
          r.setRunning(false);
          outchan.println(client_input);
          outchan.flush();
          inchan.close();
          outchan.close();
          if(refreshTask != null) refreshTask.cancel();
          return;
        }else{
          System.out.println("Don't try to cheat! ;)");continue;
        }
      }else{
        if(isPlaying){
          outchan.println(client_input);
          outchan.flush();
        } else {
            System.out.println("Only EXIT option is available while the session is not started.");
        }
      }
    }
  }
  public void start_connection() throws IOException{
    String client_input,server_input;
      while (true) {
        //Entry client
        System.out.print("?"); System.out.flush();
        client_input = input.readLine();
        String[] client_split = client_input.split("/");
        if(client_split[0].equals("CONNECT")) my_name = client_split[1];
        outchan.println(client_input);
        outchan.flush();
        //Response server
        server_input=inchan.readLine();
        System.out.println("! "+server_input);
        String[] server_split = server_input.split("/");
        switch(server_split[0]){
          case "WELCOME" :communicate(server_split);System.out.println("End of connection");return;
          case "DENIED" : System.out.println("Error : "+server_split[1]);break;
          default : System.out.println("Error : Server side");
        }
      }
  }

  /****************************MAIN***************************/
  public static void main(String[] args) {
    Socket sock = null;
    BufferedReader inchan,input;
    PrintStream outchan;
    String client_input;
    try {
      sock = new Socket (InetAddress.getByName("127.0.0.1"),PORT);
      inchan = new BufferedReader(new InputStreamReader(sock.getInputStream()));
      outchan = new PrintStream(sock.getOutputStream());
      System.out.println("Connection established : "+sock.getInetAddress()+" port : "+sock.getPort());
      Client c  = new Client(inchan,outchan);
      c.start_connection();
    }catch (IOException e) {
      System.err.println(e);
    }finally {
      try{ if (sock != null) sock.close();}catch (IOException e2){System.err.println(e2);}
    }
  }

}
