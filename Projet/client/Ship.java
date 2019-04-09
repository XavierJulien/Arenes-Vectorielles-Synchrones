
import javafx.scene.shape.*;

public class Ship{
	//constant(modifier selon la latence)
	public final double turnit = 90.0;
	public final double thrustit = 1.0;
	public final double maxSpeed = 5.0;

	//position
	private Point position;
	//direction
	private double angle;
	//vecteur vitesse
	private Point vect_vitesse;
	//forme
	private Shape p;

	public Ship(double x,double y){
		this.p = new Polygon(new double[]{
					x, y,
					x+20, y+10,
					x+15, y,
					x+20, y-10
			 });
		position = new Point(x,y);
		angle = 0.0;
		vect_vitesse = new Point(0.0,0.0);
	}

	//GETTERS/SETTERS
	public double get_posX(){return position.getX();}
	public double get_posY(){return position.getY();}
	public double getAngle(){return angle;}
	public Shape getShape() {return p;}
	public double get_speedX(){return vect_vitesse.getX();}
	public double get_speedY(){return vect_vitesse.getY();}
	public void set_posX(double x){position.setX(x);}
	public void set_posY(double y){position.setY(y);}
	public void setAngle(double angle){this.angle = angle;}
	public void set_speedXY(double vx,double vy){vect_vitesse.setX(vx);vect_vitesse.setY(vy);}
	public String toString () {return "position : ("+get_posX()+","+get_posY()+")\nangle : ("+getAngle()+")\nvect_vitesse : ("+get_speedX()+","+get_speedY()+")";}

	//update
	public void tick() {
		position.setX(position.getX()+vect_vitesse.getX());
		position.setY(position.getY()+vect_vitesse.getY());
		p.setTranslateX(p.getTranslateX()-vect_vitesse.getX());
		p.setTranslateY(p.getTranslateY()-vect_vitesse.getY());
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
		double newvx = vect_vitesse.getX()+(thrustit*Math.cos(Math.toRadians(angle)));
		double newvy = vect_vitesse.getY()+(thrustit*Math.sin(Math.toRadians(angle)));
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
		vect_vitesse.setX(newvx);
		vect_vitesse.setY(newvy);
	}
}
