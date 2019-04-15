

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import data.Commands;
import data.Constantes;
import data.DataBase;
import data.Player;
import data.Point;
import data.Ship;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class SpaceRun extends Application{

	//------------------------------------------------------------------------//
	//																		  //
	//							JAVAFX VARIABLES							  //
	//	  																	  //
	//------------------------------------------------------------------------//

	private Stage primaryStage;
	private GridPane lobbyPane;
	private Scene lobbyScene,playScene;
	private HBox mainPane;

	private Canvas canvas;
	private VBox right;
	private Text main_score;
	private Text main_pieges;
	private TextFlow received;

	//------------------------------------------------------------------------//
	//	 																	  //
	//						VARIABLES COMMUNICATION	C/S						  //
	//	  																	  //
	//------------------------------------------------------------------------//

	//Client/server
	private Socket sock;
	private BufferedReader inchan;
	private PrintStream outchan;

	//Classes
	private Timer serverTickrateTimer;
	private SendNewComTask serverTickrateTask;
	private Receive receiver;
	private DataBase database;
	private Drawer drawer;


	//------------------------------------------------------------------------//
	//	 																   	  //
	//							MAJ PLAYERS									  //
	//	 																	  //
	//------------------------------------------------------------------------//

	public void onUpdate() {
		if (database.getIsPlaying()) {
			drawer.drawBackground();
			drawer.drawTargets();
			drawer.drawObstacles();
			drawer.drawPieges();
			drawer.drawLaser();
			drawer.drawPlayers();
			updateListPlayer();
		}else {
			drawer.drawBackground();
			drawer.drawObstacles();
			drawer.drawWaiting();
			updateListPlayer();
		}
	}
	private void updateListPlayer() {
		@SuppressWarnings("unchecked")
		ListView<String> aff_list_players = (ListView<String>)right.getChildren().get(1);
		List<String> list = new ArrayList<>();
		database.getPlayer_list().forEach((k,v) -> {
			String player_desc = "Player : "+k+" | Score : "+v.getScore();
			list.add(player_desc);
		});
		ObservableList<String> newItems = FXCollections.observableList(list);
		
		aff_list_players.setItems(newItems);
		main_pieges.setText("Pièges restants : "+database.getMyself().getNb_pieges());
	}
	private void updateMyscore() {
		main_score.setText("Score : "+ database.getPlayer_list().get(database.getName()).getScore());
		main_pieges.setText("Pièges restants : "+database.getMyself().getNb_pieges());
	}
	private String getWinner() {
		String winner="";
		int best_score = 0;
		for (Map.Entry<String, Player> entry : database.getPlayer_list().entrySet()) {
			if (entry.getValue().getScore()>best_score) {
				best_score = entry.getValue().getScore();
				winner = entry.getKey();
			}
		}
		return winner;
	}
	private void resetScores() {
		database.getPlayer_list().forEach((k,v) -> {v.setScore(0);});
		database.getPlayer_list().forEach((k,v) -> {v.setNb_pieges(4);});
		database.getPieges_list().clear();
		database.getLasers_list().clear();
	}




	//------------------------------------------------------------------------//
	//																		  //
	//							AFFICHAGE JAVAFX							  //
	//	 																	  //
	//------------------------------------------------------------------------//

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		try {
			sock = new Socket (InetAddress.getByName("127.0.0.1"),Constantes.PORT);
			inchan = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			outchan = new PrintStream(sock.getOutputStream());
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
		drawer = new Drawer(database,canvas.getGraphicsContext2D());

		//--------------RIGHTPANEL-----------------------------

		//****************DESC****************
		right = (VBox)mainPane.getChildren().get(1);
		Pane descJoueur = (Pane)right.getChildren().get(0);
		Text main_username = (Text)descJoueur.getChildren().get(0);
		main_username.setText("User : "+database.getName());
		main_score = (Text)descJoueur.getChildren().get(1);
		main_score.setText("Score : "+ database.getMyself().getScore());
		main_pieges = (Text)descJoueur.getChildren().get(2);
		main_pieges.setText("Pièges restants : "+database.getMyself().getNb_pieges());
		Button exit = (Button)descJoueur.getChildren().get(3);
		exit.setOnAction(e -> {
			receiver.setRunning(false);
			sendExit(database.getName());
			try {
				serverTickrateTask.cancel();
				inchan.close();
				outchan.close();
				sock.close();
				System.exit(0);
			} catch (IOException e1) {
				System.out.println("Error : EXIT");
			}});

		//************************CHAT************************
		ScrollPane scrollpane = (ScrollPane)right.getChildren().get(2);
		received = (TextFlow)scrollpane.getContent();
		HBox chatbox = (HBox)right.getChildren().get(3);
		TextField to_send = (TextField)chatbox.getChildren().get(0);
		to_send.setOnMouseClicked(e -> {
			to_send.clear();
			to_send.setStyle("-fx-text-inner-color: black;");
		});
		Button send = (Button)chatbox.getChildren().get(1);
		send.setOnAction(e -> {
			String mess_to_send = to_send.getText();
			to_send.clear();
			if(mess_to_send.length() > 0) {
				String[] message = mess_to_send.split("\\/");
				if(message[0].equals("dm")) {
					if(database.getPlayer_list().containsKey(message[1])) {
						if(message[1].equals(database.getName())){
							to_send.setStyle("-fx-text-inner-color: red;");
							to_send.setText("Cannot send dm to yourself");
						}else {
							Text t = new Text("to:"+message[1]+">"+message[2]+"\n");
							t.setFill(Color.DARKGREEN);
							Platform.runLater(new Runnable() {
								@Override public void run() {
									received.getChildren().add(t);
								}
							});
							sendPEnvoi(message[1], message[2]);
						}
					}else {
						to_send.setStyle("-fx-text-inner-color: red;");
						to_send.setText(message[1]+" n'existe pas");
					}
				}else {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							received.getChildren().add((new Text("you>"+message[0]+"\n")));
						}
					});
					sendEnvoi(message[0],database.getName());
				}
			}});
		updateListPlayer();


		//------------------FIX POSITION---------------------------
		new AnimationTimer(){
			public void handle(long currentNanoTime){onUpdate();}
		}.start();

		//------------------EVENT HANDLER--------------------------
		playScene.setOnKeyPressed(e -> {
			if (e.getText().equals("z")) {
				database.getCumulCmds().add(Commands.thrust);
			}
			if (e.getText().equals("d")) {
				database.getCumulCmds().add(Commands.clock);
			}
			if (e.getText().equals("q")) {
				database.getCumulCmds().add(Commands.anticlock);
			}
			if (e.getText().equals("p")) {
				if(database.getMyself().getNb_pieges() > 0) {
					database.getMyself().setNb_pieges(database.getMyself().getNb_pieges()-1);
					double x = database.getMyself().getShip().get_posX();
					double y = database.getMyself().getShip().get_posY();
					x = x + Constantes.ve_radius*2 * Math.cos((Math.toRadians(database.getMyself().getShip().getAngle()) - Math.PI));
					y = y + Constantes.ve_radius*2 * Math.sin((Math.toRadians(database.getMyself().getShip().getAngle()) - Math.PI));
					sendPiege(new Point(x,y));
				}else {System.out.println("No more bananas :) ");}
			}
			if (e.getText().equals("l")) {
				double x = database.getMyself().getShip().get_posX();
				double y = database.getMyself().getShip().get_posY();
				x = x + Constantes.ve_radius*2 * Math.cos((Math.toRadians(database.getMyself().getShip().getAngle())));
				y = y + Constantes.ve_radius*2 * Math.sin((Math.toRadians(database.getMyself().getShip().getAngle())));
				Ship s = new Ship(x,y,Constantes.maxSpeedLaser);
				s.setAngle(database.getMyself().getShip().getAngle());
				double angle = Math.toRadians(database.getMyself().getShip().getAngle())%(Math.PI*2);
				double cosangle = Math.cos(angle);
				double sinangle = Math.sin(angle);
				s.set_speedXY((Constantes.maxSpeedLaser*cosangle),s.get_speedY());
				s.set_speedXY(s.get_speedX(),(Constantes.maxSpeedLaser*sinangle));
				sendLaser(s);
			}
		});

	}
	public void initializeLobby() {
		Text lobby_username_label = new Text("Username");
		TextField lobby_username_field = new TextField();
		Button connect = new Button("Connect");
		connect.setOnAction(e -> {
			database = new DataBase(lobby_username_field.getText());
			sendConnect(database.getName());
			try {
				String server_input = inchan.readLine();
				String[] server_split = server_input.split("/");
				if(server_split != null) {
					switch(server_split[0]){
					case "WELCOME" :
						process_welcome(server_split);
						initializeMain();
						receiver = new Receive(this,inchan);
						receiver.start();
						serverTickrateTimer = new Timer();
						primaryStage.setScene(playScene);
						break;
					case "DENIED" :
						Text t;
						if (server_split.length > 1) {
							t = new Text(server_split[1]);
							t.setFill(Color.RED);
							lobbyPane.add(t, 1, 1);
						}else{
							t = new Text("Connection denied");
							t.setFill(Color.RED);
							lobbyPane.add(t, 1, 1);
						}
						break;
					default : System.out.println("Unknown protocol");
					}
				}else {throw new IOException();}
			}catch(IOException e2) {
				e2.printStackTrace();
			}});
		lobbyPane = new GridPane();
		lobbyPane.setMinSize(400, 200);
		lobbyPane.setPadding(new Insets(10, 10, 10, 10));
		lobbyPane.setVgap(5);
		lobbyPane.setHgap(10);
		lobbyPane.setAlignment(Pos.CENTER);
		lobbyPane.add(lobby_username_label, 0, 0);
		lobbyPane.add(lobby_username_field, 1, 0);
		lobbyPane.add(connect, 0, 1);

		lobbyScene = new Scene(lobbyPane);
		primaryStage.setTitle("SpaceRun");
		primaryStage.setScene(lobbyScene);
	}
	public static void main(String[] args) {launch(args);}

	//------------------------------------------------------------------------//
	//																      	  //
	//							COMMUNICATIONS								  //
	//																		  //
	//------------------------------------------------------------------------//

	//****************************PARSING****************************
	public void parse_status(String status) {
		if (status == "jeu") {
			database.setIsPlaying(true);
		}
		if (status == "attente") {
			database.setIsPlaying(false);
		}
	}
	public void parse_scores(String player_score_string){
		String[] player_score_string_split = player_score_string.split("\\|");
		if (database.getPlayer_list().isEmpty()) { // initialisation, le joueur vient de se connecter
			for(int i = 0;i<player_score_string_split.length;i++){
				String[] player_score = player_score_string_split[i].split("[:]");
				Player p = new Player(player_score[0],Integer.parseInt(player_score[1]));
				database.getPlayer_list().put(player_score[0], p);
				if (player_score[0].equals(database.getName())) {
					database.setMyself(p);
				}
			}
		}else{ // mise à jour des scores
			for (int i = 0; i<player_score_string_split.length; i++) {
				String[] player_score = player_score_string_split[i].split("[:]");
				int last_score = database.getPlayer_list().get(player_score[0]).getScore();
				int new_score = Integer.parseInt(player_score[1]);
				if (last_score < new_score) database.getPlayer_list().get(player_score[0]).setNb_pieges(database.getPlayer_list().get(player_score[0]).getNb_pieges()+1);
				database.getPlayer_list().get(player_score[0]).setScore(new_score);
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
			database.getPlayer_list().get(name).getShip().set_posX(x);
			database.getPlayer_list().get(name).getShip().set_posY(y);
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
			database.getPlayer_list().get(name).getShip().set_posX(x);
			database.getPlayer_list().get(name).getShip().set_posY(y);
			database.getPlayer_list().get(name).getShip().set_speedXY(vx, vy);
			database.getPlayer_list().get(name).getShip().setAngle(Math.toDegrees(t));
		}
	}
	public void parse_target(String coord_string){
		String[] pos_target = coord_string.split("[XY]");
		database.getTargets_list().add(new Point(Double.parseDouble(pos_target[1]),Double.parseDouble(pos_target[2])));
	}
	public void parse_co_targets(String co_targets) {
		String[] pos_targets = co_targets.split("\\|");
		for (String c : pos_targets) {
			String[] xy = c.split("[XY]");
			Point t = new Point(Double.parseDouble(xy[1]),Double.parseDouble(xy[2]));
			database.getTargets_list().add(t);
		}
		System.out.println("La taille de la liste targets = "+ database.getTargets_list().size());
	}

	public void parse_message_public(String reception) {
		Platform.runLater(new Runnable() {
			@Override public void run() {
				received.getChildren().add(new Text(reception+"\n"));
			}
		});
	}
	public void parse_message_public(String reception,String from) {
		Platform.runLater(new Runnable() {
			@Override public void run() {
				received.getChildren().add(new Text(from+">"+reception+"\n"));
			}
		});
	}
	public void parse_message_prive(String reception,String user) {
		Text t = new Text("DM:"+user+">"+reception+"\n");
		t.setFill(Color.DARKORANGE);
		Platform.runLater(()->received.getChildren().add(t));

	}
	public void parse_obstacles(String obstacles) {
		String[] stringListObs = obstacles.split("\\|");
		ArrayList<Point> tmp = new ArrayList<>();
		for (String s : stringListObs) {
			String[] xy = s.split("[XY]");
			double x = Double.parseDouble(xy[1]);
			double y = Double.parseDouble(xy[2]);
			tmp.add(new Point(x, y));
		}
		database.setObstacles_list(tmp);
	}
	public void parse_pieges(String pieges) {
		if(!pieges.isEmpty()) {
			String[] stringListPieges = pieges.split("\\|");
			database.getPieges_list().clear();
			for(String s : stringListPieges) {
				String[] xy = s.split("[XY]");
				double x = Double.parseDouble(xy[1]);
				double y = Double.parseDouble(xy[2]);
				database.getPieges_list().add(new Point(x, y));
			}
		}
	}
	public void parse_lasers(String lasers) {
		String[] stringListLasers = lasers.split("\\|");
		database.getLasers_list().clear();
		for(String s : stringListLasers) {
			String[] xy = s.split("X|Y|VX|VY|T");
			double x = Double.parseDouble(xy[1]);
			double y = Double.parseDouble(xy[2]);
			double a = Double.parseDouble(xy[3]);
			double vx = Double.parseDouble(xy[4]);
			double vy = Double.parseDouble(xy[5]);
			Ship p = new Ship(x,y,Constantes.maxSpeedLaser);
			p.setAngle(a);
			p.set_speedXY(vx, vy);
			Platform.runLater(new Runnable() {
				@Override public void run() {
					database.getLasers_list().add(p);
				}
			});

		}
	}

	//****************************PROCESS PROTOCOLES****************************
	public void process_welcome(String[] server_input){

		parse_status(server_input[1]);
		parse_scores(server_input[2]);
		parse_target(server_input[3]);
		parse_obstacles(server_input[4]);
		Platform.runLater(new Runnable() {
			@Override public void run() {
				Text t = new Text("Welcome to you dear "+database.getName()+" ! :)\n");
				t.setFill(Color.TOMATO);
				received.getChildren().add(t);
			}
		});
	}

	public void process_newplayer(String new_user){
		System.out.println("newplayer : "+new_user);
		database.getPlayer_list().put(new_user,new Player(new_user,0));
		Platform.runLater(new Runnable() {
			@Override public void run() {
				Text t = new Text(new_user+" has joined the party !\n");
				t.setFill(Color.TOMATO);
				received.getChildren().add(t);
			}
		});
	}
	public void process_denied(String error){
		System.out.println("Error : DENIED/"+error);
	}
	public void process_playerleft(String name){
		System.out.println("playerleft : "+name);
		database.getPlayer_list().remove(name);
		Platform.runLater(new Runnable() {
			@Override public void run() {
				Text t = new Text(name+" has left the party !\n");
				t.setFill(Color.TOMATO);
				received.getChildren().add(t);
			}
		});
	}

	public void process_session(String coords,String coord,String coords_obs){
		System.out.println("session 1");
		database.getTargets_list().clear();
		resetScores();
		updateMyscore();
		parse_target(coord);
		parse_coords(coords);
		parse_obstacles(coords_obs);
		database.setIsPlaying(true);
		serverTickrateTask = new SendNewComTask(this);
		serverTickrateTimer.scheduleAtFixedRate(serverTickrateTask,new Date(),Constantes.server_tickrate);
	}
	public void process_session(String coords,String coord,String coords_obs,String co_targets) {
		System.out.println("session 2");
		database.getTargets_list().clear();
		database.getPieges_list().clear();
		updateMyscore();
		updateListPlayer();
		parse_target(coord);
		parse_coords(coords);
		parse_obstacles(coords_obs);
		parse_co_targets(co_targets);
		database.setIsPlaying(true);
		serverTickrateTask = new SendNewComTask(this);
		serverTickrateTimer.scheduleAtFixedRate(serverTickrateTask,new Date(),Constantes.server_tickrate);
	}

	public void process_winner(String scores){
		parse_scores(scores);
		System.out.println("Fin de Session -> RESULTATS :");
		database.getPlayer_list().forEach((k,v) -> System.out.println("player "+(k+" -> "+v.getScore()+" points.")));
		updateMyscore();
		Platform.runLater(new Runnable() {
			@Override public void run() {
				updateListPlayer();
			}
		});
		database.setNext(0);
		database.setIsPlaying(false);
		serverTickrateTask.cancel();
		Platform.runLater(new Runnable() {
			@Override public void run() {
				Text t = new Text("\n\n"
						+ "------------WINNER--------\n"
						+ "GG to "+getWinner()+"\n"
						+ "----------------------------\n\n");
				t.setFill(Color.TOMATO);
				received.getChildren().add(t);
			}
		});

	}

	public void process_tick(String vcoords){
		parse_vcoords(vcoords);
		database.getPieges_list().clear();
		database.getLasers_list().clear();
	}
	public void process_tick(String vcoords,String pieges){
		parse_vcoords(vcoords);
		parse_pieges(pieges);
		database.getLasers_list().clear();
	}
	public void process_tick(String vcoords,String pieges,String lasers){
		parse_vcoords(vcoords);
		parse_pieges(pieges);
		parse_lasers(lasers);
	}
	public void process_newobj(String coord,String scores){
		database.getTargets_list().clear();
		database.setNext(0);
		parse_target(coord);
		parse_scores(scores);
		updateMyscore();
		updateListPlayer();
		System.out.println("new_obj : " + coord);
	}
	public void process_newobj(String co_t, String scores, String co_targets) {
		database.getTargets_list().clear();
		database.setNext(0);
		parse_target(co_t);
		parse_scores(scores);
		parse_co_targets(co_targets);
		updateMyscore();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				updateListPlayer();
			}
			
		});
		
		System.out.println("new_obj multiple");
	}

	public void process_reception(String message) {
		System.out.println("reception : "+message);
		parse_message_public(message);

	}
	public void process_reception(String message,String from) {
		System.out.println("reception : "+message+" from"+from);
		parse_message_public(message,from);
	}
	public void process_preception(String message,String user) {
		System.out.println("reception privee : "+user+"/"+message);
		parse_message_prive(message,user);
	}

	public void process_next(String index) {
		System.out.println(index);
		database.setNext(Integer.parseInt(index));
	}
	//****************************SEND PROTOCOLES****************************
	public void sendConnect (String username) {
		outchan.println("CONNECT/"+database.getName()+"/");
		outchan.flush();
	}
	public void sendExit (String username) {
		outchan.println("EXIT/"+database.getName()+"/");
		outchan.flush();
	}
	public void sendNewpos (double x, double y) {
		outchan.println("NEWPOS/X"+x+"Y"+y+"/");
		outchan.flush();
	}
	public void sendNewCom () {
		double a = 0.;//angle en radian
		int t = 0;//poussée
		@SuppressWarnings("unchecked")
		ArrayList<Commands> temp = (ArrayList<Commands>) database.getCumulCmds().clone();
		database.getCumulCmds().clear();
		for(Commands c : temp) {
			if(c == Commands.thrust) {
				t +=1;
			}
			if(c == Commands.clock) {
				a = (a-Constantes.turnit)%360;
			}
			if(c == Commands.anticlock) {
				a = (a+Constantes.turnit)%360;
			}
		}
		outchan.println("NEWCOM/A"+a+"T"+t+"/");
		outchan.flush();
	}
	public void sendEnvoi(String message,String myself) {
		outchan.println("ENVOI/"+message+"/"+myself+"/");
		outchan.flush();
	}
	public void sendPEnvoi(String user,String message) {
		outchan.println("PENVOI/"+user+"/"+message+"/");
		outchan.flush();
	}
	public void sendPiege(Point pos) {
		outchan.println("NEWPIEGE/X"+pos.getX()+"Y"+pos.getY()+"/");
		outchan.flush();
	}
	public void sendLaser(Ship p) {
		outchan.println("NEWLASER/X"+p.get_posX()+"Y"+p.get_posY()+"A"+p.getAngle()+"VX"+p.get_speedX()+"VY"+p.get_speedY()+"/");
		outchan.flush();
	}
}
