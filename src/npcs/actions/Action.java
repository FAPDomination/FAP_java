package npcs.actions;

import fap_java.Game;
import fap_java.NPC;

import java.io.Serializable;


public abstract class Action implements Serializable {
    @SuppressWarnings("compatibility:-5422651929675780241")
    private static final long serialVersionUID = -1445337731870990924L;

    private Action next;
    private Action origNext;
    
    public Action(Action next){
        this.next = next;
        this.origNext = next;
    }

    /**
     * Executes the action and then goes on to the next one in the list
     * @param whoLaunches the NPC object that executes the action
     */
    public abstract void execute(NPC whoLaunches);

    /**
     * Will reinitialize the action by reseting all modified fields to their original value
     */
    public void reinit(){
        next = origNext;
    }

    /**
     * Set the transient values extracted from the given game. NPC are serialized, thus separated from some session-related
     * field values. Hence the dynamic setting of these variables when loading the serialized file.
     * @param g the game to extract values from
     */
    public abstract void setTransientValues(Game g);
    
    /**
     * Set the next action in list
     * @param next the action that will replace/rebranch the current next one in the list
     */
    public void setNext(Action next) {
        this.next = next;
    }
    
    /**
       * Returns the next action in the list
       * @return
       */
    public Action getNext() {
        return next;
    }
}
