package gui;

import fap_java.Params;

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
    
    private int distanceTrigger = 3;
    private int speedX;
    private int speedY;
    
    private boolean needAnim;
    
    public ArrowSelect(PlayerSelect ps, CharacterSelection cs) {
        this.ps = ps;
        this.color = Params.colorList[ps.getControler()];
        this.cs = cs;
        computeWantedPosition();
            
        x = wantedX;
        y = wantedY;
        needAnim = false;
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
        wantedX = cd.getX()+cd.getW()/2;
        wantedY = cd.getY()+cs.getArroSelectOrigY();
    }
    
    public void computeSpeed(){
        speedX = (int)((wantedX-x)/5.2);
        speedY = (int)((wantedY-y)/5.2);
    }
    
    public void executeAnim(){
        int diffX = (wantedX-x);
        if(Math.abs(diffX) > Math.sqrt(distanceTrigger)){
            x+=speedX;
            if(speedX/diffX < 0){
                this.computeSpeed();
            }
        }
        int diffY = (wantedY-y);
        if(Math.abs(diffY) > Math.sqrt(distanceTrigger)){
            y+=speedY;
            if(speedY/diffY < 0){
                this.computeSpeed();
            }
        }
        
        if(Math.sqrt(x*x+y*y)<=distanceTrigger){
            needAnim = false;
        }
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

    public void setNeedAnim(boolean needAnim) {
        this.needAnim = needAnim;
    }

    public boolean isNeedAnim() {
        return needAnim;
    }
}
