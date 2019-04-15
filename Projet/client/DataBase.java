


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DataBase {
	private final String name;
	private Player myself;
	private Map<String,Player> player_list = new HashMap<>(); // le client courant fait partie de la liste
	private Point target;
	private boolean isPlaying;
	private ArrayList<Commands> cumulCmds;
	private ArrayList<Point> obstacles_list,pieges_list;
	private ArrayList<Ship> lasers_list;

	public DataBase(String name) {
		this.name  = name;
		isPlaying = false;
		cumulCmds = new ArrayList<>();
		obstacles_list = new ArrayList<>();
		pieges_list = new ArrayList<>();
		lasers_list = new ArrayList<>();
		player_list = new HashMap<>();
	}
	public ArrayList<Commands> getCumulCmds() {
		return cumulCmds;
	}
	public ArrayList<Ship> getLasers_list() {
		return lasers_list;
	}
	public Player getMyself() {
		return myself;
	}
	public String getName() {
		return name;
	}
	public ArrayList<Point> getObstacles_list() {
		return obstacles_list;
	}
	public ArrayList<Point> getPieges_list() {
		return pieges_list;
	}
	public Map<String, Player> getPlayer_list() {
		return player_list;
	}
	public Point getTarget() {
		return target;
	}
	public boolean getIsPlaying() {
		return isPlaying;
	}
	public void setIsPlaying(boolean value) {this.isPlaying = value;}
	public void setTarget(Point target) {
		this.target = target;
	}
	public void setCumulCmds(ArrayList<Commands> cumulCmds) {
		this.cumulCmds = cumulCmds;
	}
	public void setLasers_list(ArrayList<Ship> lasers_list) {
		this.lasers_list = lasers_list;
	}
	public void setMyself(Player myself) {
		this.myself = myself;
	}
	public void setObstacles_list(ArrayList<Point> obstacles_list) {
		this.obstacles_list = obstacles_list;
	}
	public void setPieges_list(ArrayList<Point> pieges_list) {
		this.pieges_list = pieges_list;
	}
	public void setPlayer_list(Map<String, Player> player_list) {
		this.player_list = player_list;
	}
	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}
}
