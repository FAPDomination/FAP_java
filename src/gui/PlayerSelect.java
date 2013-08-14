package gui;

import java.awt.Graphics;

public class PlayerSelect{
    
    private PlayerSelection panel;
    private int controler;
    private int pc;
    private int team;
    private int isFSM;

    public PlayerSelect(PlayerSelection panel) {
        this.panel = panel;
        pc = 0;
        controler = 0;
        team = 0;
        isFSM = 0;
    }


    public void setPc(int pc) {
        this.pc = pc;
    }

    public int getPc() {
        return pc;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public int getTeam() {
        return team;
    }

    public void setControler(int controler) {
        this.controler = controler;
    }

    public int getControler() {
        return controler;
    }
    
    public String toString(){
        return ""+controler+"-"+team;
    }

    public void setIsFSM(int isFSM) {
        this.isFSM = isFSM;
    }

    public int getIsFSM() {
        return isFSM;
    }
}
