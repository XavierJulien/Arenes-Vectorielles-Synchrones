package packageClient;

public class Player{
	private String name;
	private int score;
	private Ship ship;
	private int nb_pieges;

	public Player(String name,int score){
		this.name = name;
		this.score = score;
		this.ship = new Ship(100, 100);
		this.nb_pieges = 4;
	}


	public String getName(){return name;}
	public int getScore(){return score;}
	public Ship getShip(){return ship;}
	public int getNb_pieges() {return nb_pieges;}
	public void setScore(int score){this.score = score;}
	public void setShip(Ship ship){this.ship = ship;}
	public void setNb_pieges(int nb_pieges) {this.nb_pieges = nb_pieges;}
}
