package fap_java;

import java.awt.Color;
import java.awt.Graphics;

import java.util.Map;

public class Cell extends Element{
    private int did;
    private int type;
    private Player owner;
    private boolean walkable;
    private double hp;
    private boolean height;
    private boolean trap;
    private CMap map;
    
    public Cell(int i, int j, int type, CMap map) {
        super();
        
        this.setI(i);
        this.setJ(j);
        this.type = type;
        this.walkable = true;
        this.hp=0;
        this.map = map;
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
        if(hp>0){
        g.drawString(""+(int)hp, x+5, y+10);
        }
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
        }
    }
    
    public void refreshHealthPoints(){
        //trace(_root.kco);
                // Counts the neighbours of the same type of the tale
                //Note : MyDMap != 8 is for lava floor and unstable cells
                //var recovB:Boolean = myDMap[vi][vj] != 8 && healthPoints[i][0] !=1 && countNeighbours(myMap, vi, vj, healthPoints[i][0])>=nNeighboursConwell;
        if(owner != null){
            boolean recovB = (type == 1) && owner != null && map.countNeighbours(this)>=Params.nNeighboursConwell;
                if (recovB) {
                        // If the cell is wounded (under initHP HPs)
                        if (hp<owner.getInitHP()) {
                                        // The HP will recover slowly up to initHP
                                        hp += owner.getRecovLifeAuto();
                                // between initHP and maxHP
                        } else if (hp<owner.getMaxHP() || (hp<Params.higherMaxHP && type == 13)) {
                                //_root["t"+i].onEnterFrame = function() {
                                        // The HP will very slowly increase up to the max limit
                                        double gainLifeFactor;
                                        if(type == 13){
                                                gainLifeFactor = Params.gainLifeFactorMultiplier;
                                        }
                                        else{
                                                gainLifeFactor = 1;
                                        }
                                        hp += owner.getGainLife()*gainLifeFactor;
                                //};
                        } else {
                                // If the tale isn't lonely or anything, do nothing
                                //delete _root["t"+i].onEnterFrame;
                        }
                } else {
                        /* Only enabled when "GameOfLife" level 1 or more is on :
                        the goal here is to decrease the HP of the tale because it's alone.
                        Cells need to be in groups to survive
                        */
                        if (Params.gameOfLife==true && type != 2) {
                                        //_root["t"+i].onEnterFrame = function() {
                                                // The HP will decrease until the cell is dead OR not alone anymore
                                                hp -= owner.getDecLifeAuto();
                                        //};
                        }
                }
                }
                // Testing if the tale is a neutral tale with HP (Countdown Cell) :
                if (type == 2) {
                                        // The HP will decrease until the cell is dead
                                        hp -= owner.getDecLifeAuto();
                }
                // If a cell dies, it goes back to normal un-owned tale  
                if (hp<=0) {
                        // if it's a timer-cell
                        if (type == 2) {
                        /*
                                //test if it's an occupied- takable cell :
                                cvi = _root["t"+i].vi;
                                cvj = _root["t"+i].vj;
                                var talArr:Array = new Array(cvi,cvj);
                                if(myMap[cvi][cvj][2] == 0 && isOccupied(talArr)){
                                        myMap[cvi][cvj] = isOccupied(talArr);
                                        _root["t"+i].gotoAndStop(isOccupied(talArr));
                                }
                                else{
                                // it becomes blocking again
                                        m = 20;                         // v6-Point : Doesnt block v6.
                                        _root["d"+i].gotoAndStop(idBlockingHigh);
                                }
                        */
                        } else {
                                //else it goes back to neutral
                        }
                        // set the changes in the different variables
                       hp = 0;
                        owner = null;
                }
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }
}
