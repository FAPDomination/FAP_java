package npcs.actions;

import fap_java.NPC;
import fap_java.Tools;

import gui.GameSave;

import java.io.Serializable;

import java.util.ArrayList;

public class ATestSwitch implements Action, Serializable {
    private int switchID;
    private Action failAction;
    
    private Action next;
    private Action origNext;
    
    public ATestSwitch(int switchID, Action failAction, Action next) {
        super();
        this.switchID = switchID;
        this.failAction = failAction;
        
        this.next = next;
        this.origNext = next;
    }

    public void execute(NPC whoLaunches) {
        //Load datas
        GameSave gameSave = Tools.loadGame();
        //Get datas
        ArrayList<Boolean> gameSwitches = gameSave.getGameSwitches();
        boolean value = gameSwitches.get(switchID);
        if(value){
            //Loop
            if(whoLaunches != null && whoLaunches.getIterator() <= whoLaunches.getActions().size()){
                whoLaunches.execute();
            }
        }
        else{
            //End NPC
            whoLaunches.setIterator(whoLaunches.getActions().size()+5);
            if(failAction != null){
                failAction.execute(whoLaunches);
            }
            else{
                whoLaunches.execute();
            }
        }
    }

    public void setSwitchID(int switchID) {
        this.switchID = switchID;
    }

    public int getSwitchID() {
        return switchID;
    }

    public void reinit() {
        next = origNext;
        if(failAction!=null){
            failAction.reinit();
        }
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
