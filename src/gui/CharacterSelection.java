package gui;

import java.awt.Graphics;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

public class CharacterSelection extends FAPanel {
    private JButton btnNext = new JButton();
    private ArrayList<PlayerSelect> players;
    
    public CharacterSelection(TheFrame theFrame, JPanel jPanel) {
        super(theFrame, jPanel);
        
        swordX = minxS;
        cloudsX = minxC;
        
        this.setLayout(null);
        this.setSize(Constants.frameDimension);

        btnGoBack.setText("Retour");
        btnGoBack.setSize(120, 40);
        btnGoBack.setLocation(20, 20);
        
        btnNext.setText("Suivant");
        btnNext.setSize(120, 40);
        btnNext.setLocation(this.getWidth()-30-btnNext.getWidth(), 20);
        
        this.add(btnGoBack);
        this.add(btnNext);
        this.validate();
        this.repaint();
        
        players = ((PlayerSelection)prevPanel).getPlayers();
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
    }
}
