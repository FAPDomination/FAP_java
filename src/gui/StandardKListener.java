package gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.ArrayList;


public class StandardKListener implements KeyListener {

    private ArrayList<Integer> pressed = new ArrayList<Integer>();

    public StandardKListener() {
        super();
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (!pressed.contains(code)) {
            pressed.add(code);
        }
    }

    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        while (pressed.contains(code)) {
            pressed.remove((Object)code);
        }
    }

    public void setPressed(ArrayList<Integer> pressed) {
        this.pressed = pressed;
    }

    public ArrayList<Integer> getPressed() {
        return pressed;
    }
}
