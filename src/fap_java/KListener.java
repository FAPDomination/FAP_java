package fap_java;
import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;

public class KListener implements KeyListener{
    
    private ArrayList<Player> players;
    
    public KListener(Game game) {
        super();
        players = game.getPlayers();
    }

    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        
    }

    public void keyReleased(KeyEvent e) {
        
    }
}
