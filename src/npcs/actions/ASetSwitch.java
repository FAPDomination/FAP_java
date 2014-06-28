package npcs.actions;

import fap_java.NPC;
import fap_java.Tools;

import gui.GameSave;

import java.io.Serializable;

import java.util.ArrayList;

public class ASetSwitch implements Action, Serializable {
    private int switchID;
    private boolean newValue;
    
    private Action next;
    private Action origNext;
    public ASetSwitch(int switchID, boolean newValue, Action next){
        super();
        this.switchID = switchID;
        this.newValue = newValue;
        this.next = next;
        this.origNext = next;
    }

    public void execute(NPC whoLaunches) {
        //Load datas
        GameSave gameSave = Tools.loadGame();
        //Set datas
        ArrayList<Boolean> gameSwitches = gameSave.getGameSwitches();
        gameSwitches.set(switchID, newValue);
        
        //Save datas
        Tools.saveGame(gameSave);
        // Retrigger trigger NPCs (with this updated setting)
        whoLaunches.getGame().triggerTriggerNPCs();
        
        //Loop
        whoLaunches.gotoNextAction();
        whoLaunches.execute();
        
    }

    public void reinit() {
        next = origNext;
    }
    
    public void setNext(Action next) {
        this.next = next;
    }

    public Action getNext() {
        return next;
    }

    public void setOrigNext(Action origNext) {
        this.origNext = origNext;
    }

    public Action getOrigNext() {
        return origNext;
    }
}
