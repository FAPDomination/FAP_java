package fapLan;

import fap_java.Player;

import gui.Displayer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.ArrayList;

public class LListener implements KeyListener {
    /**
     * The keys that are currently pressed
     */
    private ArrayList<Integer> pressed = new ArrayList<Integer>();
    private Displayer disp;
    public LListener(Displayer disp) {
        super();
        this.disp = disp;
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if(!pressed.contains(code)){
            Player p = disp.getGame().getPlayers().get(disp.getClient().getPlayerID());
            int[][] keys = p.getKeys();
                for (int j = 0; j < keys.length; j++) {
                    if (code == keys[j][0] && !pressed.contains(code)) {
                        // If the key is pressed, store it
                        pressed.add(code);
                    }

                    if (pressed.contains(keys[j][0])) {
                        // Tells the server to react
                        disp.getClient().send("k"+disp.getClient().getPlayerID()+(""+j)+"1");
                    }
                }
        }
    }

    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        //System.out.println(code);
        while (pressed.contains(code)) {
            //disp.getClient().send("k"+disp.getClient().getPlayerID()+"");
            Player p = disp.getGame().getPlayers().get(disp.getClient().getPlayerID());
            int[][] keys = p.getKeys();
                for (int j = 0; j < keys.length; j++) {
                    if (code == keys[j][0]) {
                        // Tells the server to react
                        disp.getClient().send("k"+disp.getClient().getPlayerID()+(""+j)+"0");
                    }
                }
            pressed.remove((Integer)code);
        }
    }

    public void setDisp(Displayer disp) {
        this.disp = disp;
    }

    public Displayer getDisp() {
        return disp;
    }
}
