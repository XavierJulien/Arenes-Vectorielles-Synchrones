
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage; 

public class SpaceRun extends Application{
//------------------------------------------------------------------------//
//																		  //
//								CONSTANTES								  //
//	  																	  //	
//------------------------------------------------------------------------//
	protected static final int PORT=2019;
	protected static final int ve_radius = 30;
	protected static final int ob_radius = 20;
	protected static final int server_tickrate = 100;
	private static final double demih = 350;
	private static final double demil = 450;
	
//------------------------------------------------------------------------//
//																		  //
//								DATA CLIENT								  //
//																		  //	
//------------------------------------------------------------------------//

	private String name;
	private int score;
	private Player myself;
	private Map<String,Player> player_list; // le client courant fait partie de la liste
	private Point target;
	private boolean isPlaying;
	private ArrayList<Commands> cumulCmds;
	
//------------------------------------------------------------------------//
//																		  //
//							JAVAFX VARIABLES							  //qqqq
//	  																	  //	
//------------------------------------------------------------------------//


	//JAVAFX
	private Stage primaryStage;
	//JavaFX Lobby
	private GridPane lobbyPane;
	private Scene lobbyScene;
	//JavaFX Main
	private HBox mainPane;
	private Scene playScene;
	//Canvas
	private Canvas canvas;
	private GraphicsContext ctx;
	private Drawer drawer;
	//Others
	private VBox right;
	//desc
	private Text main_score;
	private ListView<String> list_scores;
	//listplayers

	//chatbox


//------------------------------------------------------------------------//
//	 																	  //
//						VARIABLES COMMUNICATION	C/S						  //
//	  																	  //	
//------------------------------------------------------------------------//

	private Client c;
	private BufferedReader inchan;
	private PrintStream outchan;
	private Socket sock;
	private Receive r;
	private Image ship = new Image("images/ship.png");
	private Timer serverTickrateTimer;
	private RefreshClientTask serverTickrateTask;


	
//------------------------------------------------------------------------//
//	 																	  //
//							GETTERS/SETTERS								  //
//	  																	  //	
//------------------------------------------------------------------------//
	public GraphicsContext getGraphicsContext() {return ctx;}
	public Map<String,Player> getPlayer_list() {return  player_list;}
	public Player getMyself() {return myself;}
	public Point getTarget() {return target;}
	public double getDemih() {return demih;}
	public double getDemil() {return demil;}
	public void init() {
		this.score = 0;
		this.player_list = new HashMap<>();
		this.target = null;
		this.isPlaying = false;
		this.cumulCmds = new ArrayList<>();
	}

	
	
//------------------------------------------------------------------------//
//	 																   	  //
//							MAJ PLAYERS									  //
//	 																	  //	
//------------------------------------------------------------------------//
	
	public void move(Ship p){//simule le monde thorique, a revoir avec -demih et -demil
		if(p.get_posX() > demil) p.set_posX(-demil+p.get_posX()%demil);
		if(p.get_posY() > demih) p.set_posY(-demih+p.get_posY()%demih);
		if(p.get_posX() < -demil) p.set_posX(demil-p.get_posX()%demil);
		if(p.get_posY() < -demih) p.set_posY(demih-p.get_posY()%demih);
	}

	public void onUpdate() {//met à jour les positions des joueurs à chaque
		//for(Player p : player_list.values()) p.getShip().refresh_pos();
		updateListPlayer();
		updateScore();
		ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		ctx.drawImage(new Image("images/space.png"), 0, 0, canvas.getWidth(),canvas.getHeight());
		drawer.drawTarget();
		drawer.drawPlayers();
		//move(myself.getShip());
	}
	
	public boolean collisionTargetShip(Ship s,Point t){
	   double dist = (s.get_posX()-t.getX())*(s.get_posX()-t.getX()) + 
			   				   (s.get_posY()-t.getY())*(s.get_posY()-t.getY());
	   if (dist <= (SpaceRun.ve_radius+SpaceRun.ob_radius)*
			   	  (SpaceRun.ve_radius+SpaceRun.ob_radius)) {
	      return true;
	   }else {
	      return false;}
	}
	
	private void updateScore() {
		for(Player p : player_list.values()) {
			Ship s = p.getShip();
			if(collisionTargetShip(s, target)) {
				p.setScore(p.getScore()+1);	
			}
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private void updateListPlayer() {
		ListView<String> aff_list_players = (ListView<String>)right.getChildren().get(1);
		List<String> list = new ArrayList<>();
		player_list.forEach((k,v) -> {
			String player_desc = "Player : "+k+" | Score : "+v.getScore();
			list.add(player_desc);	
		});
		ObservableList<String> newItems = FXCollections.observableList(list);
		aff_list_players.setItems(newItems);
	}
	
	
	
//------------------------------------------------------------------------//
//																		  //
//							AFFICHAGE JAVAFX							  //
//	 																	  //	
//------------------------------------------------------------------------//
	@Override
	public void start(Stage primaryStage) throws Exception { //CE QUI EST LANCE PAR LAUNCH
		this.primaryStage = primaryStage;
		try {
			sock = new Socket (InetAddress.getByName("127.0.0.1"),PORT);
			inchan = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			outchan = new PrintStream(sock.getOutputStream());
			System.out.println("Connection established : "+sock.getInetAddress()+" port : "+sock.getPort());
			initializeLobby();
			primaryStage.show();
		}catch (IOException e) {
			System.err.println(e);
			sock.close();
		}
	}
	public void initializeMain() throws IOException {

		mainPane = (HBox) FXMLLoader.load(getClass().getResource("main.fxml"));
		playScene = new Scene(mainPane, 1200, 700);
		//-----------------DESSIN---------------------------------
		canvas = (Canvas)mainPane.getChildren().get(0);
		ctx = canvas.getGraphicsContext2D();
		drawer = new Drawer(this);
		drawer.drawPlayers();
		drawer.drawTarget();
		//--------------RIGHTPANEL-----------------------------
		//*******Description*********
		right = (VBox)mainPane.getChildren().get(1);
		Pane descJoueur = (Pane)right.getChildren().get(0);
		Text main_username = (Text)descJoueur.getChildren().get(0);
		main_username.setText("User : "+c.getMy_name());
		main_score = (Text)descJoueur.getChildren().get(1);
		main_score.setText("Score : "+String.valueOf(c.getScore()));
		Button exit = (Button)descJoueur.getChildren().get(2);
		exit.setOnAction(e -> {
			r.setRunning(false);
			sendExit(name);
			try {
				if(serverTickrateTimer != null) serverTickrateTimer.cancel();
				inchan.close();
				outchan.close();
				sock.close();
				System.exit(0);
			} catch (IOException e1) {
				System.out.println("Error : EXIT");
			}});
		updateListPlayer();
		

		//------------------FIX POSITION---------------------------
		new AnimationTimer(){//peut etre inutile si on peut directement appeler update dans la partie EVENT HANDLER
			public void handle(long currentNanoTime){onUpdate();}
		}.start();
		//*************EVENT HANDLER*************
		playScene.setOnKeyPressed(e -> {
			if (e.getText().equals("z")) {
				cumulCmds.add(Commands.thrust);
				player_list.get(name).getShip().thrust();
			}
			if (e.getText().equals("d")) {
				cumulCmds.add(Commands.anticlock);
				player_list.get(name).getShip().clock();
			}
			if (e.getText().equals("q")) {
				cumulCmds.add(Commands.clock);
				player_list.get(name).getShip().anticlock();
			}
		});

	}
	public void initializeLobby() {
		//label username
		Text lobby_username_label = new Text("Username");
		//Text Filed for username
		TextField lobby_username_field = new TextField();
		//Buttons
		Button connect = new Button("Connect");
		connect.setOnAction(e -> {
			name = lobby_username_field.getText();
			sendConnect(name);
			try {
				String server_input = inchan.readLine();
				String[] server_split = server_input.split("/");
				if(server_split != null) {
					switch(server_split[0]){
					case "WELCOME" :
						init();
						serverTickrateTimer = new Timer();
						c = new Client(name);
						process_welcome(server_split);
						initializeMain();
						r = new Receive(this,inchan);
						r.start();
						primaryStage.setScene(playScene);
						break;
					case "DENIED" : 
						Text t = new Text("Nickname already taken.");
						t.setFill(Color.RED);
						lobbyPane.add(t, 1, 1);
						System.out.println("Error : "+server_split[1]);break;
					}
				}else {throw new IOException();}
			}catch(IOException e2) {
				e2.printStackTrace();
			}});
		//Grid Pane
		lobbyPane = new GridPane();
		lobbyPane.setMinSize(400, 200);
		lobbyPane.setPadding(new Insets(10, 10, 10, 10));
		lobbyPane.setVgap(5);
		lobbyPane.setHgap(10);
		lobbyPane.setAlignment(Pos.CENTER);
		lobbyPane.add(lobby_username_label, 0, 0);
		lobbyPane.add(lobby_username_field, 1, 0);
		lobbyPane.add(connect, 0, 1);

		//Scene
		lobbyScene = new Scene(lobbyPane);
		//Stage
		primaryStage.setTitle("SpaceRun");
		primaryStage.setScene(lobbyScene);
	}
	//Run
	public static void main(String[] args) {launch(args);}

//------------------------------------------------------------------------//
//																		  //
//							COMMUNICATIONS								  //
//																		  //	
//------------------------------------------------------------------------//
	//**************************AUX***************************************
	
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
				if (player_score[0].equals(name)) {
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
			String[] xy = p.split(":")[1].split("[XY]|VX|VY|T");
			double x = Double.parseDouble(xy[1]);
			double y = Double.parseDouble(xy[2]);
			String name = p.split(":")[0];
			player_list.get(name).getShip().set_posX(x);
			player_list.get(name).getShip().set_posY(y);
		}
	}
	public void parse_vcoords(String player_coord_string){
		String[] player_coord_string_split = player_coord_string.split("\\|");
		for(String p : player_coord_string_split) {
			String[] xy = p.split(":")[1].split("[XY]|VX|VY|T");
			double x = Double.parseDouble(xy[1]);
			double y = Double.parseDouble(xy[2]);
			double vx = Double.parseDouble(xy[3]);
			double vy = Double.parseDouble(xy[4]);
			double t = Double.parseDouble(xy[5]);
			String name = p.split(":")[0];
			player_list.get(name).getShip().set_posX(x);
			player_list.get(name).getShip().set_posY(y);
			player_list.get(name).getShip().set_speedXY(vx, vy);
			player_list.get(name).getShip().setAngle(Math.toDegrees(t));
		}
	}
	public void parse_target(String coord_string){
		String[] pos_target = coord_string.split("[XY]");
		target = new Point(Double.parseDouble(pos_target[1]),Double.parseDouble(pos_target[2]));
	}

//****************************PROCESS PROTOCOLES*********************
	//RECEIVE
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
		serverTickrateTask = new RefreshClientTask(this);
		serverTickrateTimer.scheduleAtFixedRate(new RefreshClientTask(this),new Date(),server_tickrate);
	}
	public void process_winner(String scores){
		parse_scores(scores);
		System.out.println("Fin de Session -> RESULTATS :");
		player_list.forEach((k,v) -> System.out.println("player "+(k+" -> "+v.getScore()+" points.")));
		isPlaying = false;
		serverTickrateTimer.cancel();
	}
	public void process_tick(String vcoords){
		System.out.println("tick : "+vcoords);
		parse_vcoords(vcoords);
		
	}
	public void process_newobj(String coord,String scores){
		parse_target(coord);
		parse_scores(scores);
		System.out.println("new_obj : " + coord);
	}
	//SEND
	/**************************SEND FUNCTIONS**************************/
	public void sendConnect (String username) {
		outchan.println("CONNECT/"+name+"/");
		outchan.flush();
	}
	public void sendExit (String username) {
		outchan.println("EXIT/"+name+"/");
		outchan.flush();
	}
	public void sendNewpos (double x, double y) {
		outchan.println("NEWPOS/X"+x+"Y"+y+"/");
		outchan.flush();
	}
	public void sendNewCom () {
		double a = 0.;//angle en radian
		int t = 0;//poussée
		ArrayList<Commands> temp = (ArrayList<Commands>) cumulCmds.clone();
		cumulCmds.clear();
		for(Commands c : temp) {
			if(c == Commands.thrust) {
				t +=1;
			}
			if(c == Commands.clock) {
				a = (a-myself.getShip().turnit)%360;
			}
			if(c == Commands.anticlock) {
				a = (a+myself.getShip().turnit)%360;
			}
		}
		System.out.println("NEWCOM/A"+a+"T"+t+"/");
		outchan.println("NEWCOM/A"+a+"T"+t+"/");
		outchan.flush();
	}


}
