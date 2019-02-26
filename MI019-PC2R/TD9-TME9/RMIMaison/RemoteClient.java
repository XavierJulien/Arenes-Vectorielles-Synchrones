import pc2r.Registry; 
import service.interf.IRemoteCalculette;

public class Client 
{     
	public static void main(String[] argv) 
	{         
		Registry pc2rReg = new Registry().connect("localhost", 10000);         
		IRemoteCalculette cs = r.get("Add", IRemoteCalculette.class);            
		System.out.println(cs.add(2, 3));     
	} 
} 

