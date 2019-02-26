class TestFilPingPong {

    public static void main (String args[]) {
	FilPingPong fping, fpong;
	Thread ping, pong;
	int k, nb;

	Bascule b = new Bascule();//true
	fping = new FilPingPong("PING",b);//t : true, b : false
	fpong = new FilPingPong("PONG",b);//t : false, b : true
	ping  = new Thread(fping);
	pong  = new Thread(fpong);

	ping.start();
	pong.start();

	try {
	    ping.join();
	    pong.join();
	} catch (InterruptedException e) {};

	System.out.println("fin PING et PONG");
    }
}
