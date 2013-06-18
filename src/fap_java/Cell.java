package fap_java;

import java.awt.Color;
import java.awt.Graphics;

import java.util.Map;

public class Cell extends Element{
    private int did;
    private int type;
    private Player owner;
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
        this.hp=0;
    }
    
    public void paintComponent(Graphics g) {
        int x = CMap.giveTalePosition(this.getI(), this.getJ())[0];
        int y = CMap.giveTalePosition(this.getI(), this.getJ())[1]+CMap.OFFMAP;
        
        if(owner == null){
            g.setColor(Color.BLACK);
        }
        else{
            g.setColor(owner.getColor());
        }
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

    public void activateCell(Player p){
        if (owner!=p) {
            // if not, tests if the tale is empty
            if (hp<=0) {
                    // The tale is empty, sets it as the property of the player, gives HP and draw the according map
                    owner = p;
                    hp = p.getInitHP();
            } else {
                    // Else forces the healthpoints of the tale to decrease (Attack)
                    hp -= p.getDecLifeForced();
            }
            
            owner.getGame().repaint();
        }
    }
}
