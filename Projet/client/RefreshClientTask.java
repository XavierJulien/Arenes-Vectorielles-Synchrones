
import java.util.TimerTask;

public class RefreshClientTask extends TimerTask {

	private SpaceRun s;
	
	public RefreshClientTask(SpaceRun s) {
		this.s = s;
	}


	@Override
	public void run() {
		s.sendNewCom();
	}
}
