

public class Player{
  private String name;
  private int score;
  private Ship ship;

  public Player(String name,int score){
    this.name = name;
    this.score = score;
    this.ship = new Ship(100, 100); // pour ne pas avoir à différencier le cas ou il est null du cas ou il est pas null
  //  this.vehicule = new Ship(new Circle(0,0,20),0,0);
  }
  

  public String getName(){return name;}
  public int getScore(){return score;}
  public Ship getShip(){return ship;}
  public void setScore(int score){this.score = score;}
  public void setShip(Ship ship){this.ship = ship;}
}
