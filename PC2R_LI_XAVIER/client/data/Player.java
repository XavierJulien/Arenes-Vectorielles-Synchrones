package data;



public class Player{
	private final String name;
	private int score;
	private Ship ship;
	private int nb_pieges;
	private int nb_laser;

	public Player(String name,int score){
		this.name = name;
		this.score = score;
		this.ship = new Ship(100, 100,5.0);
		this.nb_pieges = 15;
	}


	public String getName(){return name;}
	public int getScore(){return score;}
	public Ship getShip(){return ship;}
	public int getNb_pieges() {return nb_pieges;}
	public int getNb_laser() {return nb_laser;}

	public void setScore(int score){this.score = score;}
	public void setShip(Ship ship){this.ship = ship;}
	public void setNb_pieges(int nb_pieges) {this.nb_pieges = nb_pieges;}
}
