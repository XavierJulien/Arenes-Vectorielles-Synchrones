import java.net.*;
import java.rmi.*;

public class UtilRappelCalculette {
    static String umsg =
	"UtilRappelCalculette : usage host port object ";

    public static void main (String args[]) {
	String URL;

	if (args.length < 3) {
	    URL = "rmi://localhost:1234/calculette";
	    System.err.println(umsg + "val defaut : " + URL);
	} else {
	    URL = "rmi://" + args[0] + ":" + args[1] + "/" + args[2];}

	try {
	    //obtenir un representant local de l'objet distant
	    IRappelCalculette calc = (IRappelCalculette)Naming.lookup(URL);

	    Integer res = calc.eval(new Integer(3), new Integer(3));
	    System.out.println("resultat 3+3 calcule par l'objet distant : " +
			       res.intValue());

	    for (int k = 1; k <= 100; k++) {
		calc.cumuler(new Integer(k));
	    }

	    RappelResRMI resrappel = new RappelResRMI();
	    System.out.println("appel cote client de getCumul ");

	    res = calc.getCumulR(resrappel); //retour immediat
	    System.out.println("retour immediat : " + res.intValue());

	    Thread.yield();
	    while (!resrappel.isFinished()) {
 		synchronized (resrappel) { resrappel.wait(); }
	    }
	    System.out.println("resultat vrai de getCumul : " +
			       resrappel.getResult().intValue()); 

	    calc.resetCumul();

	    res = calc.getCumulR(resrappel);

	    while (!resrappel.isFinished()) {
		synchronized (resrappel) { resrappel.wait(); }
	    }
	    System.out.println(
	      "resultat apres reinitialisation de l'objet distant : " +
	      resrappel.getResult().intValue());
	} catch (RemoteException e) {
	    System.err.println(URL +
	      " : serveur rmi indisponible " + "(" + e.getMessage() + ")");
	    System.exit(1);
	} catch (NotBoundException e) {
	    System.err.println( URL +
	      " : nom inconnu sur serveur rmi" + "(" + e.getMessage() + ")");
	    System.exit(1);
	} catch (MalformedURLException e) {
	    System.err.println(URL + "  URL syntaxiquement incorrecte");
	    System.exit(1);
	} catch (Exception e) {
	    System.out.println(e.getMessage());
	    e.printStackTrace();
	}
    }
}
