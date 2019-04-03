import pc2r.Registry; 
import service.implementation.RemoteCalculette;

public class Server 
{     
	public static void main(String[] argv)
	{ 
		Registry pc2rReg = new Registry();
		pc2rReg.register("Add", new RemoteCalculette());
		pc2rReg.publish(10000);     
	} 
} 
