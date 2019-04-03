public class TCalculCumul extends Thread { //s'execute cote serveur
    IRappelCalculette calculette;
    IRappelResRMI     res;

    public TCalculCumul (IRappelCalculette rc, IRappelResRMI r) {
	//recoit l'objet local calculette et 
	//l'objet distant attendant le resultat
	calculette = rc; 
	res = r;
    }

    public void run () {
	try {
	    //appel methode de l'objet local
	    Integer r = calculette.getCumul();//supposee "longue"...
	    //appel de l'objet distant pour definir le resultat
	    res.put(r);
	} catch (Exception e) { e.printStackTrace(); }
    }
}
