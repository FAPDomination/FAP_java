package npcs.actions;

import animations.NPCMessage;

import fap_java.NPC;

import java.io.Serializable;

public class ADisplayMessage implements Action, Serializable {
    private String message;
    private int iterator = 0;
    private NPCMessage npcMessage;
    
    private Action next;
    private Action origNext;
    
    public ADisplayMessage(String message, Action next) {
        super();
        this.message = message;
        this.iterator =0;
        
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
        if(iterator == 0){
            this.npcMessage = new NPCMessage(message,whoLaunches.getGame().getThread());
            iterator++;
            
        }
        else{
            this.reinit();
            whoLaunches.gotoNextAction();
            whoLaunches.execute();
        }
    }

    public void setIterator(int iterator) {
        this.iterator = iterator;
    }

    public int getIterator() {
        return iterator;
    }

    public void reinit() {
        next = origNext;
        iterator = 0;
        if(npcMessage != null){
            npcMessage.endAnimation();
        }
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
