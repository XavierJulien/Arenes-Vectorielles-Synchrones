
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.*;
import javafx.stage.*;

//import javafx.scene.canvas.Canvas;
//import javafx.scene.canvas.GraphicsContext;
//import javafx.geometry.Rectangle2D;

public class SpaceRun extends Application implements Runnable{
	// private double cx,cy;//coordonnées centre
	private double demih;//demihauteur
	private double demil;//demilargeur

	private Pane root;
	//private Canvas canvas;
	private Player player;

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
	public void start(Stage stage) throws Exception {

		// Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		// demil = primaryScreenBounds.getWidth();
		// demih = primaryScreenBounds.getHeight();
		demil = 500;
		demih = 500;
		//Screen Config
		root = new Pane();
		Scene scene = new Scene(root,500,500);
    stage.setScene(scene);

    // //canvas = new Canvas(demil,demih);
    // root.getChildren().add(canvas);
    // GraphicsContext gc = canvas.getGraphicsContext2D();

		//************ADD PLAYERS****************
		
		player = new Player("juju",0);
		root.getChildren().add(player.getVehicule().getShip());
		//***************************************
		//************UPDATE HANDLER*************
    new AnimationTimer(){//peut etre inutile si on peut directement appeler update dans la partie EVENT HANDLER
        public void handle(long currentNanoTime){onUpdate();}
    }.start();
		//***************************************
		//*************EVENT HANDLER*************
		stage.getScene().setOnKeyPressed(e -> {
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
		//***************************************
	  stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void run() {
		launch();


	}
}

//Image earth = new Image( "ihm/images/ship.png" );
//Image sun   = new Image( "ihm/images/target.png" );
//Image space = new Image( "ihm/images/space.png" );
