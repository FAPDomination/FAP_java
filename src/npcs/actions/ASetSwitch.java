package npcs.actions;

import fap_java.Game;
import fap_java.NPC;
import fap_java.Tools;

import gui.GameSave;

import java.util.ArrayList;


public class ASetSwitch extends Action {
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1692640727158343575L;
    private int switchID;
    private boolean newValue;
    
    /**
     * Creates an NPC action that will set the state of a switch (when an NPC gives the player an object, for example)
     * @param switchID the ID of the switch that will be changed
     * @param newValue the new value of the switch
     * @param next the next action to be executed by the NPC
     */
    public ASetSwitch(int switchID, boolean newValue, Action next){
        super(next);
        this.switchID = switchID;
        this.newValue = newValue;
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

    public void setTransientValues(Game g) {
    }
}
