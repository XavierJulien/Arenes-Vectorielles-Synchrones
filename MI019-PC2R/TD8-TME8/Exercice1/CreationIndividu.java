import java.io.*;

public class CreationIndividu{

    public static void main(String[] argv){
        try{
            BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));
            String nom = clavier.readLine();
            String prenom = clavier.readLine();
            int age = new Integer (clavier.readLine()).intValue();
                
            Individu ind = new Individu(nom,prenom,age);
            FileOutputStream fichier = new FileOutputStream("individu.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fichier);
            oos.writeObject(ind);
            oos.flush();
            oos.close();
        }
        catch (IOException ioe){ioe.printStackTrace();}
    } 
}
