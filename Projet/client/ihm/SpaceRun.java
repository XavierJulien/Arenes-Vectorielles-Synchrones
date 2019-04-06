package ihm;


import coms.Player;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
//import javafx.scene.image.Image;
import javafx.stage.Stage;

public class SpaceRun extends Application implements Runnable{
	@SuppressWarnings("unused")
	private double cx,cy;//coordonnées centre
	private final double demih = 500;
	private final double demil = 500;//demihauteur/demilargeur
	
	private Pane root;
	private Canvas canvas; 
	private Ship p;
	
	/*public Arene(double demih,double demil){
		this.cx = 0;
		this.cy = 0;
		this.demih = demih;
		this.demil = demil;
	}
	*/
	public void move(Shape p){
		if(p.getTranslateX() > demil*2){
		  if(p.getTranslateY() > demih*2){
			System.out.println("cas 1 = x : "+p.getTranslateX()+", y : "+p.getTranslateY()+" angle : "+p.getRotate());
		    p.setTranslateX(p.getTranslateX()%(2*demil));
		    p.setTranslateY(p.getTranslateY()%(2*demih));
		  }else{
			System.out.println("cas 2 = x : "+p.getTranslateX()+", y : "+p.getTranslateY()+" angle : "+p.getRotate());
		    p.setTranslateX(p.getTranslateX()%(2*demil));
		  }
		}else{
		  if(p.getTranslateY() < 0){
			System.out.println("cas 3 = x : "+p.getTranslateX()+", y : "+p.getTranslateY()+" angle : "+p.getRotate());
		    p.setTranslateY(p.getTranslateY()+(2*demih));
		  }else{
			//System.out.println("cas 4 = x : "+p.getTranslateX()+", y : "+p.getTranslateY()+" angle : "+p.getRotate());
			p.setTranslateX(p.getTranslateX());
		    p.setTranslateY(p.getTranslateY());
		  }
		}
	}
	
	public void onUpdate() {
		 p.tick();
		 move(p.getShip());
		 //System.out.println("x : "+p.getX()+", y : "+p.getY()+" angle : "+p.getAngle());
	}

	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		//Screen Config
		root = new Pane();
		Scene scene = new Scene(root);
	    stage.setScene(scene);
	    canvas = new Canvas(demil*2,demih*2);
	    root.getChildren().add(canvas);
	    GraphicsContext gc = canvas.getGraphicsContext2D();
	    
	    //Player
	    p = new Ship(new Circle(demil,demih,20),demil,demih);
	    root.getChildren().add(p.getShip());
	    
	    new AnimationTimer(){
	        public void handle(long currentNanoTime){onUpdate();}
	    }.start();
	 
	    stage.getScene().setOnKeyPressed(e -> {
	    	if (e.getCode() == KeyCode.UP) {
				p.thrust();
				System.out.println("up");
			}
			if (e.getCode() == KeyCode.LEFT) {
				p.clock();
				System.out.println("left");
			}
			if (e.getCode() == KeyCode.RIGHT) {
				p.anticlock();
				System.out.println("right");
			}
		});
	    
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