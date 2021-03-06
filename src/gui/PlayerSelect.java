package gui;

public class PlayerSelect {

    private PlayerSelection panel;
    private int controler;
    private int pc;
    private int team;
    private int isFSM;
    private int id;

    public PlayerSelect(PlayerSelection panel) {
        this.panel = panel;
        pc = 1;
        controler = 0;
        team = 0;
        isFSM = 0;
    }

    public PlayerSelect(PlayerSelection panel, int ctrl, int team) {
        this(panel);
        controler = ctrl;
        this.team = team;
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

    public String toString() {
        return "" + controler + "-" + team;
    }

    public void setIsFSM(int isFSM) {
        this.isFSM = isFSM;
    }

    public int getIsFSM() {
        return isFSM;
    }

    public boolean equals(PlayerSelect e) {
        boolean b = false;
        if (this.getControler() == e.getControler()) {
            b = true;
        }
        return b;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
