package fap_java;

import java.awt.Color;
import java.awt.Graphics;

public class Cell extends Element{
    private int did;
    private int type;
    //private Player owner;
    private boolean walkable;
    private int hp;
    private boolean height;
    
    public Cell(int i, int j, int type) {
        super();
        
        this.setI(i);
        this.setJ(j);
        this.type = type;
    }
    
    public void paintComponent(Graphics g) {
        int x = MapTools.giveTalePosition(this.getI(), this.getJ())[0];
        int y = MapTools.giveTalePosition(this.getI(), this.getJ())[1];
        g.setColor(Color.BLACK);
        g.drawRect(x, y, MapTools.TH, MapTools.TW);
    }
}
