package fap_java;

import java.awt.Graphics;

public class Player extends Human{
    
    private int id;
    private int pc;
    private int t;
    private int ts;
    private int tmax;
    private int skillTime;
    private String ori;
    private Cell parent;
    private int decLifeForced;
    private int recovLifeAuto;
    //Keys
    
    public Player() {
        super();
    }

    public void paintComponent(Graphics g) {
    }
}
