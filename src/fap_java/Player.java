package fap_java;

import java.awt.Color;
import java.awt.Graphics;

public class Player extends Human{
    
    private int id;
    private int pc;
    private int t;
    private int ts;
    private int tmax;
    private int skillTime;
    private String ori;
    private Cell current;
    private Cell parent;
    private int decLifeForced;
    private int recovLifeAuto;
    //Keys
    
    public Player() {
        super();
    }

    public void setCurrent(Cell current) {
        this.current = current;
    }

    public Cell getCurrent() {
        return current;
    }
}
