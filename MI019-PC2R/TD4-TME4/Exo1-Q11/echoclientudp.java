import java.io.*;
import java.net.*;

public class echoclientudp {
    public static void main (String args[]) {
	try {
	    String adresse = args[0];             
	    int port       = Integer.parseInt(args[1]);            
	    String message = args[2];
	    DatagramSocket sock =
		new DatagramSocket();
	    byte [] bytes = message.getBytes();

	    sock.connect(InetAddress.getByName(adresse), port);
	    sock.send(new DatagramPacket(bytes, bytes.length));

	    DatagramPacket retour = new DatagramPacket(bytes, bytes.length);
	    sock.receive(retour);
	    System.out.println("Echo : " + new String(retour.getData()));
	}
	catch (Throwable t) { t.printStackTrace(System.err); }
    }
}
