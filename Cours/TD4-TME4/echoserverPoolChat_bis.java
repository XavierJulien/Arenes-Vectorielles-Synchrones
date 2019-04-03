import java.util.Vector;
import java.io.*;
import java.net.*;

public class echoserverPoolChat_bis {
    public static void main (String args[]) {
	int port          = Integer.parseInt(args[0]);
	int capacity      = Integer.parseInt(args[1]);
	echoServer server = new echoServer(port, capacity);
	server.run();
    }
}

class echoServer {
    ServerSocket             serv;
    int                      port, capacity;
    Vector<echoClient>       service_clients;
    Vector<Socket>           clients;
    int                      nbConnectedClients, nbWaitingSocks;
    Vector<DataOutputStream> streams;

	
    echoServer (int p, int c) {
	capacity        = c;
	port            = p;
	service_clients = new Vector<echoClient>(c);
	clients         = new Vector<Socket>();
	streams         = new Vector<DataOutputStream>();
	for (int i = 0; i < c; i++) {
	    echoClient service = new echoClient(this);
	    service_clients.add(service);
	    service.start();
	}
	nbConnectedClients = 0;
	nbWaitingSocks = 0;
    }
	
    public Socket firstClient () {
	Socket client = clients.get(0);
	clients.removeElementAt(0);
	return client;
    }
	
    public void newConnect (DataOutputStream out) {
	nbConnectedClients++;
	nbWaitingSocks--;
	System.out.println(" Thread handled connection.");
	System.out.println("   * " + nbConnectedClients + " connected.");
	System.out.println("   * " + nbWaitingSocks + " waiting.");
	streams.add(out);
	writeAllButMe("*** New user on chat ***", out);
    }

    public void clientLeft (DataOutputStream out) {
	nbConnectedClients--;
	System.out.println(" Client left.");
	System.out.println("   * " + nbConnectedClients + " connected.");
	System.out.println("   * " + nbWaitingSocks + " waiting.");
	writeAllButMe("*** A user has left ***", out);
	streams.remove(out);
    }
	
    public void writeAllButMe (String s, DataOutputStream out) {
	try {
	    for (int i = 0; i < nbConnectedClients; i++)
		if (streams.elementAt(i) != out)
		    streams.elementAt(i).writeChars(s);
	} catch (IOException e) {}
    }
	
    public int stillWaiting() { return nbWaitingSocks; }
	
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
	    }
	    try {
		inchan  = new BufferedReader(
				new InputStreamReader(s.getInputStream()));
		outchan = new DataOutputStream(s.getOutputStream());
		socket  = s;
		synchronized (server) {
		    server.newConnect(outchan);
		}
		while (true) {
		    String command = inchan.readLine();
		    if (command == null || command.equals("")) {
			System.out.println("Fin de connexion."); break;
		    }
		    synchronized (server) {
			server.writeAllButMe(command + "\n", outchan);
		    }
		}
		synchronized (server) { server.clientLeft(outchan); }
		socket.close();
	    } catch (IOException e) { e.printStackTrace(); System.exit(1); }
	}
    }	  
}
