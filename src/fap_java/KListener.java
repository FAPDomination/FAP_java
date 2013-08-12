package fap_java;

import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;

public class KListener implements KeyListener {

    private ArrayList<Player> players;
    private ArrayList<Integer> pressed = new ArrayList<Integer>();
    private Game game;

    public KListener(Game game) {
        super();
        players = game.getPlayers();
        this.game = game;
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        //System.out.println(code);
        // check if game is paused
        if (game.getThread().getRunning()) {
            for (int i = 0; i < players.size(); i++) {
                Player p = players.get(i);
                int[][] keys = p.getKeys();
                for (int j = 0; j < keys.length; j++) {
                    if (code == keys[j][0] && !pressed.contains(code)) {
                        pressed.add(code);
                        //p.keyHigh(j);
                        // break;
                    }

                    if (pressed.contains(keys[j][0])) {
                        p.keyHigh(j);
                    }
                }
            }
        }
        if (code == 80) {
            game.pauseGame();
        }
    }

    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        while (pressed.contains(code)) {
            pressed.remove((Object)code);
        }

        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            int[][] keys = p.getKeys();
            for (int j = 0; j < keys.length; j++) {
                if (code == keys[j][0]) {
                    p.keyLow(j);
                }
            }
        }

    }
}
