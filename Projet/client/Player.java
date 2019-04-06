public class Player{
  private String name;
  private int score;
  private Car vehicule;

  public Player(String name,int score){
    this.name = name; //superflu
    this.score = score;
    this.vehicule = new Car(0, 0); // pour ne pas avoir à différencier le cas ou il est null du cas ou il est pas null
  }

  public String getName(){return name;}
  public int getScore(){return score;}
  public Car getVehicule(){return vehicule;}
  public void setScore(int score){this.score = score;}
  public void setVehicule(Car vehicule){this.vehicule = vehicule;}
}
