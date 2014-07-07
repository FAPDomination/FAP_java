package npcs.actions;

import fap_java.Game;
import fap_java.NPC;
import fap_java.Tools;

import gui.GameSave;


public class ATestMapValue extends Action {
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = -5479495726056275598L;
    private String mapHash;
    private Action failAction;

    /**
     * Creates an NPC action that will test the state of a map in the worldMap (true if conquered)
     * @param mapHash the hash of the designated cell in the worldMap. Ex : "18,5"
     * @param failAction the action to execute if the test fails
     * @param next the next action to be executed by the NPC
     */
    public ATestMapValue(String mapHash, Action failAction, Action next) {
        super(next);
        this.mapHash = mapHash;
        this.failAction = failAction;

    }

    public void execute(NPC whoLaunches) {
        //Load datas
        GameSave gameSave = Tools.loadGame();
        //Get datas
        int value = gameSave.getMapValues().get(mapHash);
        if (value == 2) { // conquered
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

    public void reinit() {
        super.reinit();
        if (failAction != null) {
            failAction.reinit();
        }
    }


    public void setTransientValues(Game g) {
    }
}
