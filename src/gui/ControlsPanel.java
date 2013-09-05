package gui;

import java.awt.Graphics;

import javax.swing.JPanel;

public class ControlsPanel extends FAPanel {
    public ControlsPanel(TheFrame theFrame, JPanel jPanel) {
        super(theFrame, jPanel);
        
        swordX = minxS;
        cloudsX = minxC;
        
        this.setLayout(null);
        this.setSize(Constants.frameDimension);
        
        btnGoBack.setText("Retour");
        btnGoBack.setSize(120, 40);
        btnGoBack.setLocation(20, 20);
        this.add(btnGoBack);
        
        this.validate();
        this.repaint();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
