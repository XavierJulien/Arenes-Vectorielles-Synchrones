public class Car{
  //position
  private Point position;
  //direction en radian 
  private double angle;
  //vecteur vitesse
  //reutilisation de la classe Point mais ce n'est pas un point, juste la meme structure
  private Point vect_vitesse;

  public Car(double x,double y){
    position = new Point(x,y);
    angle = 0;
    vect_vitesse = new Point(0,0);
  }

  //GETTERS
  public double get_posX(){return position.getX();}
  public double get_posY(){return position.getY();}
  public double getAngle(){return angle;}
  public double get_speedX(){return vect_vitesse.getX();}
  public double get_speedY(){return vect_vitesse.getY();}
  //SETTERS
  public void set_posX(double x){position.setX(x);}
  public void set_posY(double y){position.setY(y);}
  public void setAngle(double angle){this.angle = angle;}
  public void set_speedXY(double vx,double vy){vect_vitesse.setX(vx);vect_vitesse.setY(vy);}

  public String toString () {
	return "position : ("+get_posX()+","+get_posY()+")\nangle : ("+getAngle()+")\nvitesse : ("+get_speedX()+","+get_speedY()+")";	  
  }
}
