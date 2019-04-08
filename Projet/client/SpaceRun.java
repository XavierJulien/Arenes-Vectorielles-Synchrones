
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import javafx.event.*;

public class SpaceRun extends Application implements Runnable{
	// private double cx,cy;//coordonnées centre
	private double demih;//demihauteur
	private double demil;//demilargeur
	private String username = "Julien";

	//Start
	private Stage primaryStage;
	private Scene mainScene, lobbyScene, playScene;

	//JavaFX Lobby
	private Text lobby_username_label;
	private TextField lobby_username_field;
	private Button connect;
	private GridPane lobbyPane;

	//JavaFX Main
	private Text main_username_label;
	private Pane playPane;
	private Player player;
	private SplitPane mainPane;
	private Button exit;
	private HBox playbox, chatbox;
	private GridPane descPane;


	public void move(Shape p){//simule le monde thorique, a revoir avec -demih et -demil
		if(p.getTranslateX() > demil) {p.setTranslateX(0);System.out.println("newx = "+p.getTranslateX());}
		if(p.getTranslateY() > demih) {p.setTranslateY(0);System.out.println("newy = "+p.getTranslateY());}
		if(p.getTranslateX() < 0) {p.setTranslateX(demil);System.out.println("newx = "+p.getTranslateX());}
		if(p.getTranslateY() < 0) {p.setTranslateY(demih);System.out.println("newy = "+p.getTranslateY());}
	}

	public void onUpdate() {//met à jour les positions des joueurs à chaque
		 player.getVehicule().tick();
		 move(player.getVehicule().getShip());
		 //System.out.println("x = "+player.getVehicule().getShip().getTranslateX()+",y = "+player.getVehicule().getShip().getTranslateY());
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		Scene temp = initializeMain();
		initializeLobby();
		primaryStage.show();
		primaryStage.setScene(temp);
		System.out.println("temp mis");
		temp.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.UP) {
				player.getVehicule().thrust();
				System.out.println("salut");
			}
			if (e.getCode() == KeyCode.LEFT) {
				player.getVehicule().clock();
				System.out.println("salut");
			}
			if (e.getCode() == KeyCode.RIGHT) {
				player.getVehicule().anticlock();
				System.out.println("salut");
			}
		});
	}

	public Scene initializeMain(){
		playPane = new Pane();
		playPane.setMinWidth(500);
		playPane.setMinHeight(500);
		playScene = new Scene(playPane,500,500);
  	//************ADD PLAYERS****************
		player = new Player("juju",0);
		playPane.getChildren().addAll(player.getVehicule().getShip());
		playPane.setOnMousePressed(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
					System.out.println("mouse click detected! "+event.getSource());
					playPane.setOnKeyPressed(e -> {
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
        }
    });
		main_username_label= new Text(username);
		exit = new Button("EXIT");
		exit.setOnAction(e -> primaryStage.setScene(lobbyScene));
		descPane = new GridPane();
		descPane.setVgap(5);
		descPane.setHgap(10);
		descPane.setHgap(10);
		descPane.setAlignment(Pos.CENTER);
		descPane.add(exit, 2, 0);
		descPane.add(main_username_label, 2, 1);

		//PlayBox
		playbox = new HBox();
		playbox.getChildren().addAll(playPane,descPane);

		//ChatBox
		chatbox = new HBox();
		chatbox.setMaxHeight(200);
		chatbox.setMinHeight(100);
		//Layout
		mainPane = new SplitPane();
		mainPane.setOrientation(Orientation.VERTICAL);
		mainPane.setMinSize(800, 600);
		mainPane.getItems().addAll(playbox, chatbox);

		//Scene
		mainScene = new Scene(mainPane,800,600);
		//Stage
		primaryStage.setScene(mainScene);
		//************UPDATE HANDLER*************
		new AnimationTimer(){//peut etre inutile si on peut directement appeler update dans la partie EVENT HANDLER
				public void handle(long currentNanoTime){
					onUpdate();
				}
		}.start();
		//*************EVENT HANDLER*************
		return mainScene;
	}

	public void initializeLobby(){
			//label username
      lobby_username_label = new Text("Username");
      //Text Filed for username
      lobby_username_field = new TextField();
      //Buttons
      connect = new Button("Connect");
			connect.setOnAction(e -> primaryStage.setScene(mainScene));
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

//Image earth = new Image( "ihm/images/ship.png" );
//Image sun   = new Image( "ihm/images/target.png" );
//Image space = new Image( "ihm/images/space.png" );
// demil = 500;
// demih = 500;
// //Screen Config


//
// //************ADD PLAYERS****************
//
// player = new Player("juju",0);
//root.getChildren().add(player.getVehicule().getShip());
// //***************************************
// //************UPDATE HANDLER*************
// new AnimationTimer(){//peut etre inutile si on peut directement appeler update dans la partie EVENT HANDLER
//     public void handle(long currentNanoTime){onUpdate();}
// }.start();
// //***************************************
// //*************EVENT HANDLER*************
// mainScene.setOnKeyPressed(e -> {
// 	if (e.getCode() == KeyCode.UP) {
// 		player.getVehicule().thrust();
// 	}
// 	if (e.getCode() == KeyCode.LEFT) {
// 		player.getVehicule().clock();
// 	}
// 	if (e.getCode() == KeyCode.RIGHT) {
// 		player.getVehicule().anticlock();
// 	}
// });
// //***************************************
//
