package gui;

import java.awt.BorderLayout;
import java.awt.Graphics;

public class OptionMenu extends FAPanel {
    
    public OptionMenu(TheFrame fr) {
        super(fr);
        
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

    public void goBack() {
        parent.changePanel(new MainMenu(parent,true), BorderLayout.CENTER);
    }
}
