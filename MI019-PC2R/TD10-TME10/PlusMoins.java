/*****
      PlusMoins.java

      fonctionne avec la page TD10.html qui contient un tag vers cet applet

*****/

import javax.swing.*;
import java.awt.event.*;
import java.awt.FlowLayout;

class Action implements ActionListener
{
    JButton    a_plus, a_moins;
    JTextField a_texte;


    void lier_plus (JButton x) {
	a_plus = x;
	x.addActionListener(this);
    }


    void lier_moins (JButton x) {
	a_moins = x;
	x.addActionListener(this);
    }


    void lier_texte (JTextField x) {
	a_texte = x;
    }


    public void actionPerformed (ActionEvent e) {
	String s = a_texte.getText();
	int    n = (new Integer(a_texte.getText())).intValue();

	if (e.getSource() == a_plus)
	    n++;
	else n--;
	a_texte.setText(""+n);
    }
}


public class PlusMoins extends JApplet
{
    JButton    plus, moins;
    JTextField texte;
    Action     action;

    public void init () {
	plus  = new JButton("+");
	moins = new JButton("-");
	texte = new JTextField("0", 10);
	texte.setHorizontalAlignment(JTextField.CENTER);

	getContentPane().setLayout(new FlowLayout());
	getContentPane().add(plus);
	getContentPane().add(texte);
	getContentPane().add(moins);
	action = new Action();
	action.lier_plus(plus);
	action.lier_moins(moins);
	action.lier_texte(texte);
    }
}

