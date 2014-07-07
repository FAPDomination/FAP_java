package gui;

import java.awt.Graphics;

import javax.swing.JPanel;


public class CreditsPanel extends FAPanel {
    public CreditsPanel(TheFrame theFrame, JPanel jPanel) {
        super(theFrame, jPanel);
        
        swordX = minxS;
        cloudsX = minxC;
        
        this.setLayout(null);
        this.setSize(Constants.frameDimension);
        
        btnGoBack.setText("Retour");
        btnGoBack.setSize(120,60);
        btnGoBack.setLocation(origX-5, origY-5);
        this.add(btnGoBack);
        
        this.validate();
        this.repaint();
    }
    
    public void paintComponent(Graphics g){
	super.paintComponent(g);
        
        //TODO Display Credits
    }
}
