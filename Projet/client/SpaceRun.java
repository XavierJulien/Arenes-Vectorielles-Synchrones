import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage; 

public class SpaceRun extends Application implements Runnable{
	private double demih;
	private double demil;
	protected static final int PORT=2019;
	private String name;
	
	//Start
	private Stage primaryStage;
	private Scene mainScene, lobbyScene, playScene;

	//JavaFX Lobby
	private GridPane lobbyPane;

	//JavaFX Main
	private BorderPane mainPane;
		//right
		private VBox right;
			//desc
			private Text main_score;
			
			//list
			
		//left
		private Canvas left;

	//Communication Client/Server
	private Client c;
	private BufferedReader inchan;
  	private PrintStream outchan;
  	private Socket sock;
    private Receive r;

    //*********************MAJ PLAYERS*************************
	public void move(Shape p){//simule le monde thorique, a revoir avec -demih et -demil
		if(p.getTranslateX() > demil) {p.setTranslateX(0);/*System.out.println("newx = "+p.getTranslateX());*/}
		if(p.getTranslateY() > demih) {p.setTranslateY(0);/*System.out.println("newy = "+p.getTranslateY());*/}
		if(p.getTranslateX() < 0) {p.setTranslateX(demil);/*System.out.println("newx = "+p.getTranslateX());*/}
		if(p.getTranslateY() < 0) {p.setTranslateY(demih);/*System.out.println("newy = "+p.getTranslateY());*/}
	}

	public void onUpdate() {//met à jour les positions des joueurs à chaque
		for(Player c : c.getPlayers_list().values()) {
			//draw(c);
			c.getShip().tick();
			move(c.getShip().getShape());
  	  	}	 
	}

	private void updateListPlayer() {
		Map<String,Player> map_players = c.getPlayers_list();
		ListView<String> aff_list_players = (ListView<String>)right.getChildren().get(1);
		List<String> list = new ArrayList<>();
		map_players.forEach((k,v) -> {
			String player_desc = "Player : "+k+" | Score : "+v.getScore();
			list.add(player_desc);	
		});
		ObservableList<String> newItems = FXCollections.observableList(list);
		aff_list_players.setItems(newItems);
	}

	
	//**************************AFFICHAGE**********************
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
		//mainPane = new Pane();
		mainScene = new Scene(mainPane,800,500,Color.BLACK);
		primaryStage.setScene(mainScene);
		
		//************ADD PLAYERS****************
		for(Player c : c.getPlayers_list().values()) {
			System.out.println("x = "+c.getShip().get_posX()+", y = "+c.getShip().get_posY());
			c.setShip(new Ship(c.getShip().get_posX(),c.getShip().get_posY()));
			mainPane.getChildren().add(c.getShip().getShape());
  	  	}
		
		//************UPDATE HANDLER*************
		new AnimationTimer(){//peut etre inutile si on peut directement appeler update dans la partie EVENT HANDLER
				public void handle(long currentNanoTime){onUpdate();}
		}.start();
		//*************EVENT HANDLER*************
		mainScene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.UP) {
				c.getPlayers_list().get(name).getShip().thrust();
			}
			if (e.getCode() == KeyCode.LEFT) {
				c.getPlayers_list().get(name).getShip().clock();
			}
			if (e.getCode() == KeyCode.RIGHT) {
				c.getPlayers_list().get(name).getShip().anticlock();
			}
		});
		
	}

	public void initializeMain2() throws IOException {
		mainPane = (BorderPane) FXMLLoader.load(getClass().getResource("main.fxml"));
		playScene = new Scene(mainPane, 1000, 1000);
		
		//-----------------DESSIN---------------------------------
		left = (Canvas)mainPane.getLeft();
		left.getGraphicsContext2D().fillRect(0, 0, left.getHeight(), left.getWidth());

		
		
		
		//--------------RIGHTPANEL-----------------------------
		//*******Description*********
		right = (VBox)mainPane.getRight();
		Pane descJoueur = (Pane)right.getChildren().get(0);
		
		Text main_username = (Text)descJoueur.getChildren().get(0);
		main_username.setText("User : "+c.getMy_name());
		main_score = (Text)descJoueur.getChildren().get(1);
		main_score.setText("Score : "+String.valueOf(c.getScore()));
	    Button exit = (Button)descJoueur.getChildren().get(2);
	    exit.setOnAction(e -> {
	    	r.setRunning(false);
	    	outchan.println("EXIT/"+name+"/");
	    	outchan.flush();
	    	try {
	    		//if(refreshTask != null) refreshTask.cancel();
	    		inchan.close();
	    		outchan.close();
				sock.close();
				System.exit(0);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println("blabla");
			}});
		
	    
		//listjoueur
	    updateListPlayer();
	    
	    //------------------FIX POSITION---------------------------
		new AnimationTimer(){//peut etre inutile si on peut directement appeler update dans la partie EVENT HANDLER
			public void handle(long currentNanoTime){onUpdate();}
		}.start();
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
			outchan.println("CONNECT/"+name+"/");
			outchan.flush();
			try {
				String server_input = inchan.readLine();
				String[] server_split = server_input.split("/");
				if(server_split != null) {
			        switch(server_split[0]){
			          case "WELCOME" :
			        	  c = new Client(name);
			        	  c.process_welcome(server_split);
			        	  initializeMain2();
			        	  r = new Receive(c,inchan);
			        	  r.start();
			  			  primaryStage.setScene(playScene);
			        	  break;
			          case "DENIED" : 
			        	  Text t = new Text("Nickname already took.");
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
	@Override public void run() {launch();}
}

/*for(Player c : c.getPlayers_list().values()) {
System.out.println("x = "+c.getShip().get_posX()+", y = "+c.getShip().get_posY());
c.setShip(new Ship(c.getShip().get_posX(),c.getShip().get_posY()));
playPane.getChildren().add(c.getShip().getShip());
}

//************UPDATE HANDLER*************
new AnimationTimer(){//peut etre inutile si on peut directement appeler update dans la partie EVENT HANDLER
	public void handle(long currentNanoTime){onUpdate();}
}.start();
//*************EVENT HANDLER*************
playScene.setOnKeyPressed(e -> {
if (e.getCode() == KeyCode.UP) {
	c.getPlayers_list().get(name).getShip().thrust();
}
if (e.getCode() == KeyCode.LEFT) {
	c.getPlayers_list().get(name).getShip().clock();
}
if (e.getCode() == KeyCode.RIGHT) {
	c.getPlayers_list().get(name).getShip().anticlock();
}
});*/

//private BufferedReader inchan,input;
	//private PrintStream outchan;
//playScene = new Scene(playPane,500,500);
//exit = new Button("EXIT");
//exit.setOnAction(e -> primaryStage.setScene(lobbyScene));
//playPane = new Pane(500,500);
//Client c
//
//main_username_label= new Text(username);
//descPane = new GridPane();
//descPane.setVgap(5);
//descPane.setHgap(10);
//descPane.setHgap(10);
//descPane.setAlignment(Pos.CENTER);
//descPane.add(exit, 2, 0);
//descPane.add(main_username_label, 2, 1);
//
//mainPane.getChildren().addAll(playPane,descPane);
//PlayBox
//playbox = new StackPane();
//playbox.getChildren().addAll(mainPane,descPane);
//
//Layout
//mainPane = new Pane();
//mainPane.setOrientation(Orientation.VERTICAL);
//mainPane.setMinSize(800, 600);
//mainPane.getItems().addAll(playbox, chatbox);
//Client c
//Scene
//mainScene = new Scene(playPane,800,600,Color.PINK);
//Stage
//primaryStage.setScene(mainScene);

