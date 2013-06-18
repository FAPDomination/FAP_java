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
        int code = e.getKeyCode();
        System.out.println(code);
        for(int i = 0;i<players.size();i++){
            Player p = players.get(i);
            int[][] keys = p.getKeys();
            for(int j=0; j<keys.length;j++){
                if(code == keys[j][0]){
                    p.keyHigh(j);
                }
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        
        for(int i = 0;i<players.size();i++){
            Player p = players.get(i);
            int[][] keys = p.getKeys();
            for(int j=0; j<keys.length;j++){
                if(code == keys[j][0]){
                    p.keyLow(j);
                }
            }
        }
    }
}
