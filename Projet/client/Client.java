import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class Client {

  protected static final int PORT=2019;
  private Receive r;
  private BufferedReader inchan,input;
  private PrintStream outchan;
  private String server_input;

  /***************************DATA****************************/
  private static String user;
  private String session_status;
  private ArrayList<String> player_list;
  private ArrayList<PlayerScore> player_score_list;
  private Point target;
  private ArrayList<PlayerCoord> player_coord_list;

  /****************************AUX****************************/

  public Client(BufferedReader inchan,
                PrintStream outchan){
    this.inchan = inchan;
    this.outchan = outchan;
    input = new BufferedReader(new InputStreamReader(System.in));

  }
  public ArrayList<PlayerScore> parse_scores(String player_score_string){
    ArrayList<PlayerScore> res = new ArrayList<PlayerScore>();
    String[] player_score_string_split = player_score_string.split("|");
    for(int i = 0;i<player_score_string_split.length;i++){
      String[] player_score = player_score_string_split[i].split(":");
      PlayerScore temp = new PlayerScore(player_score[0],player_score[1]);
      res.add(temp);
    }
    return res;
  }
  public ArrayList<PlayerCoord> parse_coords(String player_coord_string){
    ArrayList<PlayerCoord> res = new ArrayList<PlayerCoord>();
    String[] player_coord_string_split = player_coord_string.split("|");
    for(int i = 0;i<player_coord_string_split.length;i++){
      String[] player_coord = player_coord_string_split[i].split(":");
      Point p_temp = parse_coord(player_coord[1]);
      PlayerCoord temp = new PlayerCoord(player_coord[0],p_temp);
      res.add(temp);
    }
    return res;
  }
  public Point parse_coord(String coord_string){
    Point p = new Point();
    String[] pos_target = coord_string.split("[X,Y]+");
    p.setX(Float.parseFloat(pos_target[0]));
    p.setY(Float.parseFloat(pos_target[1]));
    return p;
  }
  /******************PROCESS_SERVER_REQUESTS******************/
  public void process_welcome(String[] server_input){
    //PARSE PHASE
    session_status = server_input[1];
    //PARSE PLAYER_SCORE
    player_score_list = parse_scores(server_input[2]);
    //PARSE COORD
    target = parse_coord(server_input[3]);
  }
  public void process_newplayer(String new_user){
    player_list.add(new_user);
  }
  public void process_playerleft(String user){
    player_list.remove(user);
    for(int i = 0;i<player_score_list.size();i++){
      if ((player_score_list.get(i)).getUser() == user) player_score_list.remove(user);
    }
    for(int i = 0;i<player_coord_list.size();i++){
      if ((player_coord_list.get(i)).getUser() == user) player_coord_list.remove(user);
    }
  }
  public void process_session(String coords,String coord){
    target = parse_coord(coord);
    player_coord_list = parse_coords(coords);
  }
  public void process_winner(String scores){
    player_score_list = parse_scores(scores);
    System.out.println("Fin de Session -> RESULTATS :");
    for(int i = 0;i<player_score_list.size();i++){
      System.out.println("player "+(player_score_list.get(i)).getUser()+" -> "+(player_score_list.get(i)).getScore()+" points.");
    }
  }
  public void process_tick(String coords){
    player_coord_list = parse_coords(coords);
  }
  public void process_newobj(String coord,String scores){
    target = parse_coord(coord);
    player_score_list = parse_scores(scores);
  }
  public void communicate(String[] server_split) throws IOException {
    process_welcome(server_split);//met a jour les données avec le server_input du welcome
    r = new Receive(this,inchan);//thread d'écoute de requetes serveur
    //Send s = new Send(outchan);
    r.run();
    String client_input;
    while(true){
      System.out.print("?"); System.out.flush();
      client_input = input.readLine();
      String[] client_split = client_input.split("/");
      if(client_split[0] == "EXIT"){
        if(client_split[0] == user){
          outchan.println(client_input);
          outchan.flush();
          return;
        }else{
          System.out.println("c'est pas bien de tricher");continue;
        }
      }else{
        outchan.println(client_input);
        outchan.flush();
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
        if(client_split[0] == "CONNECT") user = client_split[1];
        outchan.println(client_input);
        outchan.flush();
        //Response server
        server_input=inchan.readLine();
        System.out.println("! "+server_input);
        String[] server_split = server_input.split("/");
        switch(server_split[0]){
          case "WELCOME" : communicate(server_split);System.out.println("End of connection");return;
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
      try{ if (sock != null) sock.close(); }catch (IOException e2){System.err.println(e2);}
    }
  }

}
