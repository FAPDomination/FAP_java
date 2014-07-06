package npcs.actions;

import fap_java.Game;
import fap_java.NPC;
import fap_java.Tools;

import gui.GameSave;

public class ASetVictory implements Action {
    private Action next;
    private Action origNext;
    public ASetVictory() {
        super();
        next = null;
        origNext = null;
    }
    
    public void execute(NPC whoLaunches) {
        //Load datas
        Game game = whoLaunches.getGame();
        game.pauseGame(true);
        game.endGame(game.getTeams().get(0));
        //Loop
        /*whoLaunches.gotoNextAction();
        whoLaunches.execute();*/
        
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

    public void setTransientValues(Game g) {
    }
}
