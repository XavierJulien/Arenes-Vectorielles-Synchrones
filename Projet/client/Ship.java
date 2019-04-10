import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;

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
	private Polygon p;

	public Ship(double x,double y){
		this.p = new Polygon(new double[]{x,x+20,x+20,y,y+10,y-10});
		position = new Point(x,y);
		angle = 0.0;
		vect_vitesse = new Point(0.0,0.0);
	}

	//GETTERS/SETTERS
	public double get_posX(){return position.getX();}
	public double get_posY(){return position.getY();}
	public double getAngle(){return angle;}
	public Polygon getShape() {return p;}
	public double get_speedX(){return vect_vitesse.getX();}
	public double get_speedY(){return vect_vitesse.getY();}
	public void set_posX(double x){position.setX(x);}
	public void set_posY(double y){position.setY(y);}
	public void setAngle(double angle){this.angle = angle;}
	public void set_speedXY(double vx,double vy){vect_vitesse.setX(vx);vect_vitesse.setY(vy);}
	public String toString () {return "position : ("+get_posX()+","+get_posY()+")\nangle : ("+getAngle()+")\nvect_vitesse : ("+get_speedX()+","+get_speedY()+")";}

	//update
	public void refresh_pos(double height,double width) {
		position.setX(position.getX()+vect_vitesse.getX());
		position.setY(position.getY()+vect_vitesse.getY());
		//p.getTransforms().add(new Translate(p.getTranslateX()-vect_vitesse.getX(), p.getTranslateY()-vect_vitesse.getY()));
		p.setRotate(angle);
		p.setTranslateX(vect_vitesse.getX());
		p.setTranslateY(vect_vitesse.getY());
		//System.out.println(p.getTranslateX());
		//System.out.println(p.getTranslateY());
		for(int i = 0;i<p.getPoints().size()/2;i++) {
			p.getPoints().set(i, (p.getPoints().get(i)+vect_vitesse.getX())%width);
			
		}
		for(int i = p.getPoints().size()/2;i<p.getPoints().size();i++) {
			p.getPoints().set(i, (p.getPoints().get(i)+vect_vitesse.getX())%height);
		}
		System.out.println("avant rotation ;"+p.getPoints().toString());
		p.getTransforms().add(new Rotate(angle));
		System.out.println("avant rotation ;"+p.getPoints().toString());
	}
	
	public void move(double height,double width){//simule le monde thorique, a revoir avec -demih et -demil
		
		for(int i = 0;i<p.getPoints().size()/2;i++) {
			if(p.get_posX() > getWidth()) {p.set_posX(0);/*System.out.println("newx = "+myself.getShip().get_posX());*/}
				
			
			p.getPoints().set(i, p.getPoints().get(i)+p.getTranslateX());
		}
		for(int i = p.getPoints().size()/2;i<p.getPoints().size();i++) {
			p.getPoints().set(i, p.getPoints().get(i)+p.getTranslateY());
		}
		if(p.get_posX() > canvas.getWidth()) {p.set_posX(0);/*System.out.println("newx = "+myself.getShip().get_posX());*/}
		if(p.get_posY() > canvas.getHeight()) {p.set_posY(0);/*System.out.println("newy = "+myself.getShip().get_posY());*/}
		if(p.get_posX() < 0) {p.set_posX(canvas.getWidth());/*System.out.println("newx = "+myself.getShip().get_posX());*/}
		if(p.get_posY() < 0) {p.set_posY(canvas.getHeight());/*System.out.println("newy = "+myself.getShip().get_posY());*/}
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
