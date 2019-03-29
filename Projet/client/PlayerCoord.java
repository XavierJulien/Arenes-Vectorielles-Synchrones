public class PlayerCoord{
  private String user;
  private Point coord;

  public PlayerCoord(String user,Point coord){
    this.user = user;
    this.coord = coord;
  }

  public String getUser(){return user;}
  public Point getCoord(){return coord;}
}
