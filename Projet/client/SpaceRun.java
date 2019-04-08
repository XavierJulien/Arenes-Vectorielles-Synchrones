import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.scene.paint.*;
import javafx.geometry.*;
//import javafx.event.*;

public class SpaceRun extends Application implements Runnable{
	// private double cx,cy;//coordonnées centre
	private double demih;//demihauteur
	private double demil;//demilargeur
	protected static final int PORT=2019;
	
	//Start
	private Stage primaryStage;
	private Scene mainScene, lobbyScene, playScene;

	//JavaFX Lobby
	private GridPane lobbyPane;

	//JavaFX Main
	private Text main_username_label;
	private Pane playPane;
	private Player player;
	private Pane mainPane;
	private Button exit;
	private HBox chatbox;
	private StackPane playbox;
	private GridPane descPane;

	//Communication Client/Server
	
	private Client c;
	private BufferedReader inchan;
  	private PrintStream outchan;
  	private Socket sock;
    private Receive r;

	public void move(Shape p){//simule le monde thorique, a revoir avec -demih et -demil
		if(p.getTranslateX() > demil) {p.setTranslateX(0);/*System.out.println("newx = "+p.getTranslateX());*/}
		if(p.getTranslateY() > demih) {p.setTranslateY(0);/*System.out.println("newy = "+p.getTranslateY());*/}
		if(p.getTranslateX() < 0) {p.setTranslateX(demil);/*System.out.println("newx = "+p.getTranslateX());*/}
		if(p.getTranslateY() < 0) {p.setTranslateY(demih);/*System.out.println("newy = "+p.getTranslateY());*/}
	}

	public void onUpdate() {//met à jour les positions des joueurs à chaque
		 player.getVehicule().tick();
		 move(player.getVehicule().getShip());
		 //System.out.println("x = "+player.getVehicule().getShip().getTranslateX()+",y = "+player.getVehicule().getShip().getTranslateY());
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		try {
		  sock = new Socket (InetAddress.getByName("127.0.0.1"),PORT);
		  inchan = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		  outchan = new PrintStream(sock.getOutputStream());
		  System.out.println(outchan);
		  System.out.println(inchan);
		  System.out.println("Connection established : "+sock.getInetAddress()+" port : "+sock.getPort());
	      initializeLobby();
		  primaryStage.show();
		}catch (IOException e) {
		  System.err.println(e);
		  sock.close();
		}
	}

	public void initializeMain(){
		demil = 500;
		demih = 500;
		mainPane = new Pane();
		mainScene = new Scene(mainPane,800,500,Color.BLACK);
		primaryStage.setScene(mainScene);
		//************ADD PLAYERS****************
		player = new Player("juju",0);
		//  exit = new Button("EXIT");
		// exit.setOnAction(e -> primaryStage.setScene(lobbyScene));
		// // playPane = new Pane(500,500);
		mainPane.getChildren().addAll(player.getVehicule().getShip());
		//Client c
		//
		// main_username_label= new Text(username);
		// descPane = new GridPane();
		// descPane.setVgap(5);
		// descPane.setHgap(10);
		// descPane.setHgap(10);
		// descPane.setAlignment(Pos.CENTER);
		// descPane.add(exit, 2, 0);
		// descPane.add(main_username_label, 2, 1);
		//
		// mainPane.getChildren().addAll(playPane,descPane);
		// //PlayBox
		// playbox = new StackPane();
		// playbox.getChildren().addAll(mainPane,descPane);
		//************UPDATE HANDLER*************
		new AnimationTimer(){//peut etre inutile si on peut directement appeler update dans la partie EVENT HANDLER
				public void handle(long currentNanoTime){onUpdate();}
		}.start();
		//*************EVENT HANDLER*************
		mainScene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.UP) {
				player.getVehicule().thrust();
			}
			if (e.getCode() == KeyCode.LEFT) {
				player.getVehicule().clock();
			}
			if (e.getCode() == KeyCode.RIGHT) {
				player.getVehicule().anticlock();
			}
		});
		//****************************  private BufferedReader inchan,input;
  		//private PrintStream outchan;***********
		// 	// playScene = new Scene(playPane,500,500);

		// 	//
		// 	// //ChatBox
		// 	// chatbox = new HBox();
		// 	// chatbox.setMaxHeight(200);
		// 	// chatbox.setMinHeight(100);
		// 	// //Layout
		// //	mainPane = new Pane();
		// //	mainPane.setOrientation(Orientation.VERTICAL);
		// 	//mainPane.setMinSize(800, 600);

		//
		// 	// mainPane.getItems().addAll(playbox, chatbox);
		//Client c
		// 	//Scene
		// 	mainScene = new Scene(playPane,800,600,Color.PINK);
		// 	//Stage
		// 	primaryStage.setScene(mainScene);
	}

	public void initializeLobby() {
		//label username
		Text lobby_username_label = new Text("Username");
		 //Text Filed for username
		TextField lobby_username_field = new TextField();
		 //Buttons
		Button connect = new Button("Connect");
		connect.setOnAction(e -> {
			String name = lobby_username_field.getText();
			outchan.println("CONNECT/"+name+"/");
			outchan.flush();
			try {
				String server_input = inchan.readLine();
				String[] server_split = server_input.split("/");
				if(server_split != null) {
			        switch(server_split[0]){
			          case "WELCOME" :
			        	  c = new Client(name);
			        	  initializeMain();
			        	  r = new Receive(c,inchan);
			        	  r.start();
			  			  primaryStage.setScene(mainScene);
			        	  break;
			          case "DENIED" : 
			        	  lobbyPane.add(new Text("Nickname already taken."), 0, 2);
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
	@Override public void run() {launch();}
}