import java.util.Vector;
import java.io.*;
import java.net.*;

public class echoserverPool_bis {
    public static void main (String args[]) {
	int port          = Integer.parseInt(args[0]);
	int capacity      = Integer.parseInt(args[1]);
	echoServer server = new echoServer(port, capacity);
	server.run();
    }
}

class echoServer {
    ServerSocket       serv;
    int                port, capacity;
    Vector<echoClient> service_clients;
    Vector<Socket>     clients;
    int                nbConnectedClients, nbWaitingSocks;
	
    echoServer (int p, int c) {
	capacity         = c;
	port             = p;
	service_clients  = new Vector<echoClient>(c);
	clients          = new Vector<Socket>();
	for (int i = 0; i < c; i++) {
	    echoClient service = new echoClient(this);
	    service_clients.add(service);
	    service.start();
	}
	nbConnectedClients = 0;
	nbWaitingSocks     = 0;
    }
	
    public Socket firstClient () {
	Socket client = clients.get(0);
	clients.removeElementAt(0);
	return client;
    }
	
    public void newConnect () {
	nbConnectedClients++;
	nbWaitingSocks--;
	System.out.println(" Thread handled connection.");
	System.out.println("   * " + nbConnectedClients + " connected.");
	System.out.println("   * " + nbWaitingSocks + " waiting.");
    }

    public void clientLeft () {
	nbConnectedClients--;
	System.out.println(" Client left.");
	System.out.println("   * " + nbConnectedClients + " connected.");
	System.out.println("   * " + nbWaitingSocks + " waiting.");
    }
	
    public int stillWaiting () { return nbWaitingSocks; }
	
    public void run () {
	Socket             client;

	try {
	    serv = new ServerSocket(port); 
	    while (true) {
		client = serv.accept();
		System.out.println("New connexion at server.");  
		synchronized (this) {
		    clients.add(client);
		    nbWaitingSocks++;
		    this.notify();
		}
	    }
	} catch (Throwable t) { t.printStackTrace(System.err); }
    }
}

class echoClient extends Thread {
    BufferedReader   inchan;
    DataOutputStream outchan;
    echoServer       server;
    Socket           socket;
	
    echoClient (echoServer s) { server = s; }
	
    public void run () {
	Socket s;
	  
	while (true) {
	    synchronized (server) {
		if (server.stillWaiting() == 0)
		    try {
			server.wait();
		    } catch (InterruptedException e) { e.printStackTrace(); }
		s = server.firstClient();
		server.newConnect();
	    }
	    try {
		inchan  = new BufferedReader(
			        new InputStreamReader(s.getInputStream()));
		outchan = new DataOutputStream(s.getOutputStream());
		socket  = s;
		while (true) {
		    String command = inchan.readLine();
		    if (command == null || command.equals("")) {
			System.out.println("Fin de connexion.");
			break;
		    }
		    outchan.writeChars(command + "\n");
		}
		socket.close();
		synchronized (server) {
		    server.clientLeft();
		}
	    } catch (IOException e) { e.printStackTrace(); System.exit(1); }
	}
    }	  
}
