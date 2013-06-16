package fap_java;

import java.awt.Graphics;
import javax.swing.JPanel;

public class Game extends JPanel{
    public Game() {
        super();
    }
    
    public void paintComponent(Graphics g) {
        Cell c1 = new Cell(5,3,1);
        c1.paintComponent(g);
    }
}
