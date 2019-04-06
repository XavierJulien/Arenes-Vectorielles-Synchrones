import java.util.TimerTask;

public class RefreshClientTask extends TimerTask {
	
	private Player myself;
	
	public RefreshClientTask(Player myself) {
		this.myself = myself;
	}

	@Override
	public void run() {
		System.out.println(myself.getVehicule().get_speedX());
		System.out.println(myself.getVehicule().get_speedY());
		myself.getVehicule().set_posX(myself.getVehicule().get_posX()+myself.getVehicule().get_speedX());
		myself.getVehicule().set_posY(myself.getVehicule().get_posY()+myself.getVehicule().get_speedY());
		System.out.println(myself.getVehicule().toString());
	}
}
