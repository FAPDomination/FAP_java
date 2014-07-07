package npcs.actions;

import fap_java.Game;
import fap_java.NPC;
import fap_java.Tools;

import gui.GameSave;

import java.util.ArrayList;


public class ATestSwitch extends Action {
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 8332904318881077569L;
    private int switchID;
    private Action failAction;

    /**
     * Creates an NPC action that will test the state of a switch
     * @param switchID the ID of the switch to test
     * @param failAction the action to execute if the test fails
     * @param next the next action to be executed by the NPC
     */
    public ATestSwitch(int switchID, Action failAction, Action next) {
        super(next);
        this.switchID = switchID;
        this.failAction = failAction;

    }

    public void execute(NPC whoLaunches) {
        //Load datas
        GameSave gameSave = Tools.loadGame();
        //Get datas
        ArrayList<Boolean> gameSwitches = gameSave.getGameSwitches();
        boolean value = gameSwitches.get(switchID);
        if (value) {
            //Loop
            whoLaunches.gotoNextAction();
            whoLaunches.execute();
        } else {
            //End NPC
            whoLaunches.setCurrentAction(failAction);
            whoLaunches.execute();
            //this.reinit();
        }
    }

    public void setSwitchID(int switchID) {
        this.switchID = switchID;
    }

    public int getSwitchID() {
        return switchID;
    }

    public void reinit() {
        super.reinit();
        if (failAction != null) {
            failAction.reinit();
        }
    }

    public void setTransientValues(Game g) {
    }
}
