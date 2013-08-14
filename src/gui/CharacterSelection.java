package gui;

import java.awt.Graphics;

import javax.swing.JPanel;

public class CharacterSelection extends FAPanel {
    
    public CharacterSelection(TheFrame theFrame, JPanel jPanel) {
        super(theFrame, jPanel);
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawString("lolilol", 200, 200);
    }
}
