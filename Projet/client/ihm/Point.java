package ihm;
public class Point{
  //position
  private double x;
  private double y;

  public Point(double x,double y){
    this.x = x;
    this.y = y;
  }
//GETTERS
  public double getX(){return x;}
  public double getY(){return y;}
  //SETTERS
  public void setX(double x){this.x = x;}
  public void setY(double y){this.y = y;}
}
