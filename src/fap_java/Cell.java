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
    private String addParam;
    
    public Cell(int i, int j, int type, int did) {
        this(i, j, type,"", did);
    }
    
    public Cell(int i, int j, int type, String param, int did) {
        super();
        
        this.setI(i);
        this.setJ(j);
        this.walkable = true;
        this.height = false;
        this.hp=0;
        this.addParam = param;
        this.did = did;
        this.setType(type);
        
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
        if(type == 10){
            g.setColor(Color.GREEN);
        }
        if(!walkable){
            g.setColor(Color.pink);
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
        if (owner!=p && type ==1) {
            // if not, tests if the tale is empty
            if (hp<=0) {
                    // The tale is empty, sets it as the property of the player, gives HP and draw the according map
                if(walkable == false){
                    p.kickBack(); 
                }
                else{
                    owner = p;
                    hp = p.getInitHP();
                }
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
            boolean recovB = (type == 1) && owner != null && map.countNeighbours(this)>=Params.nNeighboursConwell && type != 8;
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
                                        hp -= Params.decLifeCountDown;
                }
                // If a cell dies, it goes back to normal un-owned tale  
                if (hp<=0) {
                        // if it's a timer-cell
                        if (type == 2) {
                            type = 1;
                            walkable = false;
                            //Change did
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

    public void setType(int type) {
        if(type ==20){
            this.type = 1;
            walkable = false;
            this.height = true;
        }
        else if(type == 19){
            this.type = 1;
            walkable = false;
            this.height = false;
        }
        else if(type == 2){
            this.type = type;
            hp = Integer.parseInt(addParam);
        }
        else{
            this.type = type;
        }
    }

    public int getType() {
        return type;
    }

    public void setAddParam(String addParam) {
        this.addParam = addParam;
    }

    public String getAddParam() {
        return addParam;
    }

    public void setMap(CMap map) {
        this.map = map;
    }

    public CMap getMap() {
        return map;
    }
}
