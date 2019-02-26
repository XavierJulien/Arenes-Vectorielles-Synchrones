import java.io.*;

public class Individu implements java.io.Serializable{
        
    private String nom, prenom;
    private int age;
        
    public Individu(String nom, String prenom, int age){
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
    }
        
    public void setNom(String nom){this.nom = nom;}
    public void setPrenom(String prenom){this.prenom = prenom;}
    public void setAge(int age){this.age = age;}
        
    public String getNom(){return(this.nom);}
    public String getPrenom(){return(this.prenom);}
    public int getAge(){return(this.age);}
        
    public String toString(){return(nom + " " + prenom + ", " + age);}

}
