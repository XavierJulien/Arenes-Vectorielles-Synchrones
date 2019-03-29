public class Car{
  //position
  private float x;
  private float y;
  //direction
  private float angle;
  //vecteur vitesse
  private float vx;
  private float vy;

  public Car(float x,float y){
    this.x = x;
    this.y = y;
  }
  public Car(){
    this.x = (float)0;
    this.y = (float)0;
  }

  //GETTERS
  public float getX(){return x;}
  public float getY(){return y;}
  public float getAngle(){return angle;}
  public float getVx(){return vx;}
  public float getVy(){return vy;}
  //SETTERS
  public void setX(float x){this.x = x;}
  public void setY(float y){this.y = y;}
  public void setAngle(float angle){this.angle = angle;}
  public void setVx(float vx){this.vx = vx;}
  public void setVy(float vy){this.vy = vy;}

}
