package npcs.actions;

import fap_java.Game;
import fap_java.NPC;
import fap_java.Tools;

import gui.GameSave;


public class ASetMapValue extends Action {
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 8646952569855326525L;
    private String mapHash;
    private int newValue;
    
    /**
     * Creates an NPC action that will set the state of a map in the worldMap (0 : unavailable, 1 : available, 2 : conquered).
     * @param mapHash the hash of the designated cell in the worldMap. Ex : "18,5"
     * @param newValue the new value for this map : (0 : unavailable, 1 : available, 2 : conquered)
     * @param next the next action to be executed by the NPC
     */
    public ASetMapValue(String mapHash, int newValue, Action next){
        super(next);
        this.mapHash = mapHash;
        this.newValue = newValue;
    }

    public void execute(NPC whoLaunches) {
        //Load datas
        GameSave gameSave = Tools.loadGame();
        //Set datas
        gameSave.getMapValues().put(mapHash, newValue);
        
        //Save datas
        Tools.saveGame(gameSave);
        
        //Loop
        whoLaunches.gotoNextAction();
        whoLaunches.execute();
        
    }

    public void setTransientValues(Game g) {
    }
}
