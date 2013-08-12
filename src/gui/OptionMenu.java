package gui;

import java.awt.BorderLayout;
import java.awt.Graphics;

import javax.swing.JPanel;

public class OptionMenu extends FAPanel {
    
    public OptionMenu(TheFrame fr, JPanel prevPanel) {
        super(fr,prevPanel);
        
        swordX = minxS;
        cloudsX = minxC;
        
        this.setLayout(null);
        this.setSize(Constants.frameDimension);
        
        btnGoBack.setText("Retour");
        btnGoBack.setSize(120, 40);
        btnGoBack.setLocation(240, 225);
        this.add(btnGoBack);
        
        this.validate();
        this.repaint();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
