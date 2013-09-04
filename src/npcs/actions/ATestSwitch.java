package npcs.actions;

import fap_java.NPC;
import fap_java.Tools;

import gui.GameSave;

import java.util.ArrayList;

public class ATestSwitch implements Action {
    private int switchID;
    private Action failAction;
    public ATestSwitch(int switchID, Action failAction) {
        super();
        this.switchID = switchID;
        this.failAction = failAction;
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
        if(failAction!=null){
            failAction.reinit();
        }
    }
}
