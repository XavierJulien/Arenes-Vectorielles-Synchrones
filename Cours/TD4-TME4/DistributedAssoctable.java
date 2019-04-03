import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.HashMap;


public class DistributedAssoctable {
	public static void main(String args[]){
		BufferedReader 	 	   in;
		DataOutputStream	   out;
		ServerSocket 		   serv;
		Socket 			       client;
		HashMap<String,String> table;
		String key;
		try {
			int port = Integer.parseInt(args[0]);
			serv = new ServerSocket(port);
			table = new HashMap<String,String>();
			while(true){
				client = serv.accept();
				try{
					in = new BufferedReader(new InputStreamReader(client.getInputStream()));
					out = new DataOutputStream(client.getOutputStream());
		   			while (true) {
		   			System.out.println("START");
						Scanner s = new Scanner(in).useDelimiter(" ");
						if(!s.hasNext()){
							System.out.println("Fin de connexion");
							break;
						}
						switch(s.next()){
							case "START" :
								System.out.println("START");
								break;
							case "QUIT" :
								System.out.println("QUIT");
								client.close();
								return;
							case "PUT" : 
								
								System.out.println("PUT");
								key = s.next();
								String value = s.next();
								table.put(key,value);
								break;
							case "GET" : 
							
								System.out.println("GET");
								key = s.next();
								
								out.writeChars(table.get(key));
								break;
							default :
								out.writeChars("pas de commande reconue !\n");
								break;						
						}
					}
				}catch(IOException e){
					System.err.println("I/O Error");
		    		e.printStackTrace();
				}
				client.close();	
			}
	    }catch (Exception t) { t.printStackTrace(System.err); }
	}
}
