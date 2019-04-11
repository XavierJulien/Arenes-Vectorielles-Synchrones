
import java.util.TimerTask;

public class RefreshClientTask extends TimerTask {

	private SpaceRun s;
	
	public RefreshClientTask(SpaceRun s) {
		this.s = s;
	}


	@Override
	public void run() {
		s.sendNewCom();
		//myself.getShip().set_posX(myself.getShip().get_posX()+myself.getShip().get_speedX());
		//myself.getShip().set_posY(myself.getShip().get_posY()+myself.getShip().get_speedY());
		
	}
}
