package coms;

import ihm.Ship;
import javafx.scene.shape.Circle;

public class Player{
  private String name;
  private int score;
  private Ship vehicule;

  public Player(String name,int score){
    this.name = name; //superflu
    this.score = score;
    this.vehicule = new Car(0, 0); // pour ne pas avoir à différencier le cas ou il est null du cas ou il est pas null
  //  this.vehicule = new Ship(new Circle(0,0,20),0,0);
  }

  public String getName(){return name;}
  public int getScore(){return score;}
  public Ship getVehicule(){return vehicule;}
  public void setScore(int score){this.score = score;}
  public void setVehicule(Ship vehicule){this.vehicule = vehicule;}
}
