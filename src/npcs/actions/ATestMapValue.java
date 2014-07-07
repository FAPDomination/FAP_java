package npcs.actions;

import fap_java.Game;
import fap_java.NPC;
import fap_java.Tools;

import gui.GameSave;


public class ATestMapValue implements Action {
    @SuppressWarnings("compatibility:5999795561692079931")
    private static final long serialVersionUID = -5479495726056275598L;
    private String mapHash;
    private Action failAction;
    
    private Action next;
    private Action origNext;
    
    public ATestMapValue(String mapHash, Action failAction, Action next) {
        super();
        this.mapHash = mapHash;
        this.failAction = failAction;
        
        this.next = next;
        this.origNext = next;
    }

    public void execute(NPC whoLaunches) {
        //Load datas
        GameSave gameSave = Tools.loadGame();
        //Get datas
        int value = gameSave.getMapValues().get(mapHash);
        if(value == 2){ // conquered
            //Loop
            whoLaunches.gotoNextAction();
            whoLaunches.execute();
        }
        else{
            //End NPC
            whoLaunches.setCurrentAction(failAction);
            whoLaunches.execute();
            //this.reinit();
        }
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

    public void setTransientValues(Game g) {
    }
}
