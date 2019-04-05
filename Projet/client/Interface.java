import java.awt.Color;

import javax.swing.JFrame;

 
public class Interface {
  public static void main(String[] args){       
    JFrame fenetre = new JFrame();
    fenetre.setVisible(true);
    fenetre.setExtendedState(JFrame.MAXIMIZED_BOTH); 
    Color bgColor = new Color(10,10,10);
	fenetre.getContentPane().setBackground(bgColor);
    
  }       
}