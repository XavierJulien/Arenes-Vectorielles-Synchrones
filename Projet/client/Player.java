public class Player{
	private String name;
	private int score;
	private Ship ship;

	public Player(String name,int score){
		this.name = name;
		this.score = score;
		this.ship = new Ship(100, 100);
	}


	public String getName(){return name;}
	public int getScore(){return score;}
	public Ship getShip(){return ship;}
	public void setScore(int score){this.score = score;}
	public void setShip(Ship ship){this.ship = ship;}
}
