package npcs.actions;

import animations.NPCMessage;

import fap_java.NPC;

import java.io.Serializable;

public class ADisplayMessage implements Action, Serializable {
    private String message;
    private NPCMessage npcMessage;
    
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
        System.out.println("Exec");
        if(this.npcMessage == null){
            this.npcMessage = new NPCMessage(message,whoLaunches.getGame().getThread());
        }
        else{
            whoLaunches.gotoNextAction();
            whoLaunches.execute();
            this.reinit();
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
}
