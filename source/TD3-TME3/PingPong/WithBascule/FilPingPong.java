import java.util.Random;

class FilPingPong implements Runnable {
    String affich;
    Random alea;
    Bascule b;
	Boolean t;
    public FilPingPong (String a_affich,Bascule b) {
		affich = a_affich;
		alea   = new Random();
		this.b = b;
		t = b.getVal();
		b.setVal(!t);
    }

    public void run () {
    	int k = 0;
    	while (k < 20) {
			synchronized(b){
				if(t == b.getVal()){
					System.out.println(affich);
					b.setVal(!t);
					b.notify();
					k++;
				}else{
					try{
						b.wait();
					}catch(InterruptedException e){}
				}
			}
		}
    }
}


