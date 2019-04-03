import java.net.*;
import java.rmi.*;
import java.rmi.registry.*;
public class ClientPourServAutonomeCalc { //classe ordinaire, cote serveur
    public static void main (String args[]) {
        String URL="";
        int port;
 
        try {
            if (args.length>0){port=Integer.valueOf(args[0]).intValue();}
            else {port =1234;}

            Registry reg= LocateRegistry.getRegistry(port); // localhost
	    // sinon getRegistry(host, port)


            IRemoteCalculette calc = (IRemoteCalculette)reg.lookup("/calc");
            System.out.println("apres lookup");
            System.out.println("apres eval " +
                               calc.eval(new Integer(5),new Integer(5)));

            //les groupes de threads

            //consultation du ThreadGroup
            // et du gpe parent
            int k,nb;Thread[] tt=new Thread[50];
            System.out.println("\tle groupe de thread courant");
            ThreadGroup g= Thread.currentThread().getThreadGroup();
            nb = g.enumerate(tt,false);
            for (k=0;k<nb;k++) System.out.println("\t" + tt[k].toString());
            System.out.println("\tle groupe de thread parent du groupe precedent");
            ThreadGroup gp = g.getParent();

            nb = gp.enumerate(tt,false);//pour eviter les precedents
            for (k=0;k<nb;k++) System.out.println("\t" + tt[k].toString());


        }
        catch(RemoteException e) {
            System.err.println(URL+":  serveur rmi indisponible"+"("+e.getMessage()+")");
            System.exit(1);
        }
 
        catch(Exception e) {
            System.err.println(URL+":  serveur rmi indisponible"+"("+e.getMessage()+")");
            System.exit(1);
        }
    }
}
