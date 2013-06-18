package fap_java;

import java.awt.Color;
import java.awt.Graphics;

import java.util.Map;

public class Cell extends Element{
    private int did;
    private int type;
    //private Player owner;
    private boolean walkable;
    private int hp;
    private boolean height;
    private boolean trap;
    
    public Cell(int i, int j, int type) {
        super();
        
        this.setI(i);
        this.setJ(j);
        this.type = type;
        this.walkable = true;
    }
    
    public void paintComponent(Graphics g) {
        int x = CMap.giveTalePosition(this.getI(), this.getJ())[0];
        int y = CMap.giveTalePosition(this.getI(), this.getJ())[1]+CMap.OFFMAP;
        g.setColor(Color.BLACK);
        g.drawRect(x, y, CMap.TW, CMap.TH);
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public boolean isWalkable() {
        return walkable;
    }
    
    public String toString(){
        return "lolilol"+this.getI()+"-"+this.getJ();
    }
    
    public boolean equals(Cell c){
        boolean b = false;
        if(this.getI() == c.getI() && this.getJ() == c.getJ()){
            b = true;
        }
        return b;
    }

}
