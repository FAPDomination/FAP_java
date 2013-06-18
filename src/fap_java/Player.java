package fap_java;

import java.awt.Color;
import java.awt.Graphics;

public class Player extends Human{
    
    private int id;
    private int pc;
    private int t;
    private int ts;
    private int tmax;
    private int lastDisplacement;
    private int skillTime;
    private int lastSkill;
    private String ori;
    private Cell current;
    private Cell parent;
    private int decLifeForced;
    private int recovLifeAuto;
    //Keys
    
    
    public Player(int[] coord) {
        super();
        this.setI(coord[0]);
        this.setJ(coord[1]);
    }

    public void setCurrent(Cell current) {
        this.current = current;
    }

    public Cell getCurrent() {
        return current;
    }

    public void setLastDisplacement(int lastDisplacement) {
        this.lastDisplacement = lastDisplacement;
    }

    public int getLastDisplacement() {
        return lastDisplacement;
    }

    public void setLastSkill(int lastSkill) {
        this.lastSkill = lastSkill;
    }

    public int getLastSkill() {
        return lastSkill;
    }
}
