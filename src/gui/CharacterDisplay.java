package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class CharacterDisplay {
    
    private int x;
    private int y;
    private Image img;
    private JPanel panel;
    private int pc;
    
    private static int W = 60;
    private static int H = 120;
    
    public CharacterDisplay(int x, int y, int pc, CharacterSelection panel) {
        this.x = x;
        this.y = y;
        this.panel = panel;
        this.pc = pc;
        // Get Image from graph
        // get text from params
    }
    
    public void paintComponent(Graphics g){
        //g.drawImage(img, x, y, panel);
        g.setColor(Color.GRAY);
        g.fillRect(x, y, W, H);
    }

    public static void setW(int W) {
        CharacterDisplay.W = W;
    }

    public static int getW() {
        return W;
    }

    public static void setH(int H) {
        CharacterDisplay.H = H;
    }

    public static int getH() {
        return H;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }
}
