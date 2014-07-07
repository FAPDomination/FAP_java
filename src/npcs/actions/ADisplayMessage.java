package npcs.actions;


import animations.NPCMessage;

import fap_java.Game;
import fap_java.NPC;


public class ADisplayMessage implements Action {
    @SuppressWarnings("compatibility:-6029349740440906725")
    private static final long serialVersionUID = -7274855256989454061L;
    private String message;
    private transient NPCMessage npcMessage;
    
    private Action next;
    private Action origNext;
    
    public ADisplayMessage(String message, Action next) {
        super();
        this.message = message;
        
        this.next = next;
        this.origNext = next;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void execute(NPC whoLaunches) {
        if(this.npcMessage == null){
            this.npcMessage = new NPCMessage(message,whoLaunches.getGame().getThread());
        }
        else{
            if(npcMessage != null){
                npcMessage.endAnimation();
            }
            whoLaunches.gotoNextAction();
            whoLaunches.execute();
            
        }
    }


    public void reinit() {
        next = origNext;
        
        if(npcMessage != null){
            npcMessage.endAnimation();
        }
        npcMessage = null;
    }

    public void setNpcMessage(NPCMessage npcMessage) {
        this.npcMessage = npcMessage;
    }

    public NPCMessage getNpcMessage() {
        return npcMessage;
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
