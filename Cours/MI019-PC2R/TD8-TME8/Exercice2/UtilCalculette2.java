import java.net.*;
import java.rmi.*;

public class UtilCalculette2{//classe ordinaire locale
    static String umsg =
        "UtilCalculette : usage host port object ";
    public static void main(String args[]) {
        String URL;
        if (args.length < 3) {
            URL="rmi://localhost:1234/calculette";
            System.err.println(umsg + "val defaut : " + URL);
            //               System.exit(1);

        } else {
            URL = "rmi://"+args[0]+":"+args[1]+"/"+args[2];
        }
        int k; Integer res;  IRemoteCalculette calc;
        try{
            //obtenir un representant local de l'objet distant
            calc = (IRemoteCalculette)Naming.lookup(URL);

            res = calc.eval ( new Integer(3),  new Integer(3));
            System.out.println("resultat 3+3 calcule par l'objet distant : " + 
                               res.intValue());

            for (k=1 ;k <= 10 ;k++)
                {calc.cumuler(new Integer(k));}
            res = calc.getCumul();
            System.out.println("resultat cumul 1 a 10 calcule par l'objet distant : " +
                               res.intValue());
            calc.resetCumul();res = calc.getCumul();
            System.out.println("resultat apres reinitialisation de l'objet distant : " +
                               res.intValue());
            System.out.println("\nCHANGEMENT d'objet : calc, autre instance distante\n");
            URL = "rmi://"+args[0]+":"+args[1]+"/calc";
            calc = (IRemoteCalculette)Naming.lookup(URL);

            res = calc.eval ( new Integer(3),  new Integer(3));
            System.out.println("resultat 3+3 calcule par l'objet distant : " + 
                               res.intValue());

            for (k=1 ;k <= 10 ;k++)
                {calc.cumuler(new Integer(k));}
            res = calc.getCumul();
            System.out.println("resultat cumul 1 a 10 calcule par l'objet distant : " +
                               res.intValue());
            calc.resetCumul();res = calc.getCumul();
            System.out.println("resultat apres reinitialisation de l'objet distant : " +
                               res.intValue());
        }
        catch(RemoteException e) {
            System.err.println(URL+" : serveur rmi indisponible "+"("+e.getMessage()+")");
            System.exit(1);
        }
        catch(NotBoundException e) {
            System.err.println(URL+" : nom inconnu sur serveur rmi"+"("+e.getMessage()+")");
            System.exit(1);
        }
        catch(MalformedURLException e) {
            System.err.println(URL+"  URL syntaxiquement incorrecte");
            System.exit(1);
        }
    }
}
