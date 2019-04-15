package packageClient;

import java.util.TimerTask;

public class SendNewComTask extends TimerTask {

	private SpaceRun s;
	
	public SendNewComTask(SpaceRun s) {
		this.s = s;
	}


	@Override
	public void run() {
		s.sendNewCom();
	}
}
