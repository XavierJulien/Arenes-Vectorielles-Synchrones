public class Car{
  //position
  private Point position;
  //direction
  private float angle;
  //vecteur vitesse
  private Point vecteur;

  public Car(float x,float y){
    position = new Point(x,y);
    angle = 0;
    vecteur = null;
  }

  //GETTERS
  public float getX(){return position.getX();}
  public float getY(){return position.getY();}
  public float getAngle(){return angle;}
  public float getVx(){return vecteur.getX();}
  public float getVy(){return vecteur.getY();}
  //SETTERS
  public void setX(float x){position.setX(x);}
  public void setY(float y){position.setY(y);}
  public void setAngle(float angle){this.angle = angle;}
  public void setVx(float vx){vecteur.setX(vx);}
  public void setVy(float vy){vecteur.setY(vy);}

}
