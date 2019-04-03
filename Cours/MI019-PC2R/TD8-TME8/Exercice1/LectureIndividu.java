import java.io.*;

public class LectureIndividu{

    public static void main(String argv[]) {
        try {
            FileInputStream fichier = new FileInputStream("individu.ser");
            ObjectInputStream ois = new ObjectInputStream(fichier);
            Individu ind = (Individu) ois.readObject();
            System.out.println(ind.toString());
        } 
        catch (java.io.IOException ioe) {ioe.printStackTrace();}
        catch (ClassNotFoundException cnfe) {cnfe.printStackTrace();}
    }
}

