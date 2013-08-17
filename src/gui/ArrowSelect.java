package gui;

import fap_java.Params;

import gui.PlayerSelect;

import java.awt.Color;
import java.awt.Graphics;

public class ArrowSelect {
    
    private int x;
    private int y;
    
    private int wantedX;
    private int wantedY;
    
    private Color color;
    
    private PlayerSelect ps;
    private CharacterSelection cs;
    
    public ArrowSelect(PlayerSelect ps, CharacterSelection cs) {
        this.ps = ps;
        this.color = Params.colorList[ps.getControler()];
        this.cs = cs;
        computeWantedPosition();
            
        wantedX = x;
        wantedY = y;
    }
    
    public void paintComponent(Graphics g){
        g.setColor(color);
        g.fillRect(x, y, 4, 4);
    }
    
    public void computeWantedPosition(){
        int pc = ps.getPc();
        if(pc>7){
            pc--;
        }
        if(pc>2){
            pc--;
        }
        CharacterDisplay cd = cs.getCharList().get(pc-1);
        x = cd.getX()+cd.getW()/2;
        y = cd.getY()-40;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public void setWantedX(int wantedX) {
        this.wantedX = wantedX;
    }

    public int getWantedX() {
        return wantedX;
    }

    public void setWantedY(int wantedY) {
        this.wantedY = wantedY;
    }

    public int getWantedY() {
        return wantedY;
    }

    public void setPs(PlayerSelect ps) {
        this.ps = ps;
    }

    public PlayerSelect getPs() {
        return ps;
    }
}
