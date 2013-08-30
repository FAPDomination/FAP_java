package fap_java;

import java.awt.event.*;
import java.util.ArrayList;

public class KListener implements KeyListener {

    /**
     * The players in this game
     */
    private ArrayList<Player> players;
    /**
     * The keys that are currently pressed
     */
    private ArrayList<Integer> pressed = new ArrayList<Integer>();
    /**
     * The game where all is played
     */
    private Game game;

    /**
     * Initializes a Key Listener for the game. It will get all information about the keys that are pressed and trigger 
     * handling for the related players
     * @param game the game where it happens
     */
    public KListener(Game game) {
        super();
        players = game.getPlayers();
        this.game = game;
    }

    /**
     * Not used
     */
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    /**
     * Triggers when a key is pressed. Computes if the key is an interresting one and calls the associated player or pause
     * @param e The key event containing the code of the key
     */
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        //System.out.println(code);
        // check if game is paused
        if (game.getThread().getRunning()) {
            // Check for each player
            for (int i = 0; i < players.size(); i++) {
                Player p = players.get(i);
                int[][] keys = p.getKeys();
                for (int j = 0; j < keys.length; j++) {
                    if (code == keys[j][0] && !pressed.contains(code)) {
                        // If the key is pressed, store it
                        pressed.add(code);
                    }

                    if (pressed.contains(keys[j][0])) {
                        // Tells the player to react
                        p.keyHigh(j);
                    }
                }
            }
        }
        // Triggers pause
        if (code == Params.pauseKey) {
            game.pauseGame();
        }
    }

    /**
     * Triggers when a key is released
     * @param e The key
     */
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        while (pressed.contains(code)) {
            pressed.remove((Integer)code);
        }

        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            int[][] keys = p.getKeys();
            for (int j = 0; j < keys.length; j++) {
                if (code == keys[j][0]) {
                    // tells the player about it
                    p.keyLow(j);
                }
            }
        }

    }

    public void setPressed(ArrayList<Integer> pressed) {
        this.pressed = pressed;
    }

    public ArrayList<Integer> getPressed() {
        return pressed;
    }
}
