import java.io.*;
import java.net.*;

class PongService extends Thread {         
    protected BufferedReader inchan;
    protected DataOutputStream outchan;
    protected Socket socket;
    PongService(Socket s) { 
	try {
	    inchan = new BufferedReader(
		     new InputStreamReader(s.getInputStream()));
	    outchan = new DataOutputStream(s.getOutputStream());
	} catch(IOException e) { e.printStackTrace(System.err); System.exit(1); }
	socket = s;
    }
    
    public void run() {     
	boolean cont = true;    
	while(cont) {
	    try {   
		String command = inchan.readLine();
		if(command.equals("Ping")) 
		    outchan.writeChars("Pong\n");
                else if(command.equals("Exit"))  
		    cont = false;                     
                else if(command.equals("Kill"))
		    System.exit(0); 
            } catch(IOException e) { System.err.println("Erreur d'entre'e/sortie"); }
	} // end while
    } // end run
} // end class

public class pongserveur {
    public static void main(String args[]) {
        try {
            int port = Integer.parseInt(args[0]);
	    ServerSocket serv = new ServerSocket(port); 
	    while(true) {
                Socket client = serv.accept();  
		PongService service = new PongService(client); 
		service.start();    
	    }
        } catch(Throwable t) {t.printStackTrace(System.err); }
    } // end main
} // end class

