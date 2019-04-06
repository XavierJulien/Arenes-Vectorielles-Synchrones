package ihm;

import javafx.scene.shape.Shape;

public class Ship{
	//constant(modifier selon la latence)
	public final double turnit = 10.0;
	public final double thrustit = 1.0;
	public final double maxSpeed = 5.0;
	
	//position
	private Point position;
	//direction
	private double angle;
	//vitesse vitesse
	private Point vitesse;
	//forme
	private Shape p;
	
	public Ship(Shape p,double x,double y){
		this.p = p;
		position = new Point(x,y);
		angle = 0.0;
		vitesse = new Point(0.0,0.0);
	}
	
	//GETTERS
	public double getX(){return position.getX();}
	public double getY(){return position.getY();}
	public double getAngle(){return angle;}
	public Shape getShip() {return p;}
	public void setX(double x){position.setX(x);}
	public void setY(double y){position.setY(y);}
	
	public void tick() {
		//position.setX(position.getX()+vitesse.getX());
		//position.setY(position.getY()+vitesse.getY());
		p.setTranslateX(p.getTranslateX()+vitesse.getX());
		p.setTranslateY(p.getTranslateY()+vitesse.getY());
		p.setRotate(angle);
	}
	
	//controls
	public void clock() {
		angle = (angle-turnit)%360;//à modifier selon si c'est assez fluide ou pas
	}
	public void anticlock() {
		angle = (angle+turnit)%360;//à modifier selon si c'est assez fluide ou pas
	}
	public void thrust() {
		System.out.println("cos : "+Math.cos(Math.toRadians(angle)));
		System.out.println("sin : "+Math.sin(Math.toRadians(angle)));
		double newvx = vitesse.getX()+(thrustit*Math.cos(Math.toRadians(angle)));
		double newvy = vitesse.getY()+(thrustit*Math.sin(Math.toRadians(angle)));
		if(newvx>=maxSpeed) {
			newvx = maxSpeed;
		}else {
			if(newvx<=(-maxSpeed)) newvx = -maxSpeed;
		}
		if(newvy>=maxSpeed) {
			newvy = maxSpeed;
		}else {
			if(newvy<=(-maxSpeed)) newvy = -maxSpeed;
		}
		vitesse.setX(newvx);
		vitesse.setY(newvx);
	}

}
