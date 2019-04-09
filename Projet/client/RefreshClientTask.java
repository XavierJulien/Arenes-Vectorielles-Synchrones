
import java.util.TimerTask;

public class RefreshClientTask extends TimerTask {

	private Player myself;
	
	public RefreshClientTask(Player myself) {
		this.myself = myself;
	}

	@Override
	public void run() {
		//System.out.println(myself.getShip().get_speedX());
		//System.out.println(myself.getShip().get_speedY());
		myself.getShip().set_posX(myself.getShip().get_posX()+myself.getShip().get_speedX());
		myself.getShip().set_posY(myself.getShip().get_posY()+myself.getShip().get_speedY());
		//System.out.println(myself.getShip().toString());
	}
}
