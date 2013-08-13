package gui;

import java.awt.Graphics;

public class PlayerSelect{
    
    private PlayerSelection panel;
    private boolean isFSM;
    private int pc;
    private int team;
    
    public PlayerSelect(PlayerSelection panel) {
        this.panel = panel;
        isFSM = false;
        pc = 0;
        team = -1;
    }


    public void setIsFSM(boolean isFSM) {
        this.isFSM = isFSM;
    }

    public boolean isIsFSM() {
        return isFSM;
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
}
