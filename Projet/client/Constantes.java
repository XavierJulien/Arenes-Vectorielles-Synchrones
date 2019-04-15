


import javafx.scene.image.Image;

public abstract class Constantes {

	//Client/Server
	public static final int PORT=2019;
	public static final int server_tickrate = 100; // 100 ms = 0.1s -> frequence de 10
	
	//Ship
	public static final double turnit = 45.0;
	public static final double thrustit = 2.0;
	public static final int ve_radius = 30;
	public static final double maxSpeedShip = 5.0;
	public static final double maxSpeedLaser = 10.0;
	
	//Drawer
	public static final int ob_radius = 50;
	public static final int pi_radius = 20;
	public static final int ta_radius = 20;
	public static final int la_radius = 20;
	public static final double demih = 350;
	public static final double demil = 450;
	public static final Image coin = new Image("images/star.png");
	public static final Image asteroid = new Image("images/asteroidbis.png");
	public static final Image piege = new Image("images/banana.png");
	public static final Image laser = new Image("images/laser.png");
	public static final Image space = new Image("images/space.png");
}
