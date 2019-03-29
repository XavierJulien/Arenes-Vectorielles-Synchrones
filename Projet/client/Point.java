public class Point{
  private float x;
  private float y;

  public Point(float x,float y){
    this.x = x;
    this.y = y;
  }
  public Point(){
    this.x = (float)0;
    this.y = (float)0;
  }

  public float getX(){return x;}
  public float getY(){return y;}
  public float setX(float x){this.x = x;}
  public float setY(float y){this.y = y;}
}
