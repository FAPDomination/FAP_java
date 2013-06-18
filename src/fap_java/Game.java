package fap_java;

import java.awt.Graphics;
import javax.swing.JPanel;

public class Game extends JPanel{
    
    private CMap map;
    
    public Game() {
        super();
        
        map = new CMap();
        Cell c1 = new Cell(5,3,1);
        Cell c2 = new Cell(5,4,1);
        Cell c3 = new Cell(6,3,1);
        Cell c4 = new Cell(6,4,1);
        Cell c5 = new Cell(6,5,1);
        
        map.addElement(c5);
        map.addElement(c1);
        map.addElement(c2);
        map.addElement(c3);
        map.addElement(c4);
    }
    
    public void paintComponent(Graphics g) {
        map.paintComponent(g);
    }
}
