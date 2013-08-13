package gui;

import java.awt.Graphics;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

public class PlayerSelection extends FAPanel {
    private JButton btnNext = new JButton();
    
    private ArrayList<PlayerSelect> players;
    
    public PlayerSelection(TheFrame theFrame, JPanel jPanel) {
        super(theFrame, jPanel);
        
        players = new ArrayList<PlayerSelect>();
        
        // testing
        players.add(new PlayerSelect(this));
        players.add(new PlayerSelect(this));
        //---------
        
        swordX = minxS;
        cloudsX = minxC;
        
        this.setLayout(null);
        this.setSize(Constants.frameDimension);

        btnGoBack.setText("Retour");
        btnGoBack.setSize(120, 40);
        btnGoBack.setLocation(20, 20);
        this.add(btnGoBack);
        
        btnNext.setText("Suivant");
        btnNext.setSize(120, 40);
        btnNext.setLocation(this.getWidth()-30-btnNext.getWidth(), 20);
        this.add(btnNext);
        
        this.repaint();
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
    }

    public void setPlayers(ArrayList<PlayerSelect> players) {
        this.players = players;
    }

    public ArrayList<PlayerSelect> getPlayers() {
        return players;
    }
}
