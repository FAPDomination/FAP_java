package npcs.actions;

import animations.NPCMessage;

import fap_java.NPC;

public class ADisplayMessage implements Action {
    private String message;
    private int iterator = 0;
    private NPCMessage npcMessage;
    
    public ADisplayMessage(String message) {
        super();
        this.message = message;
        this.iterator =0;
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
            whoLaunches.setIterator(whoLaunches.getIterator()-1);
        }
        else{
            this.reinit();
            //Loop
            if(whoLaunches != null && whoLaunches.getIterator() <= whoLaunches.getActions().size()){
                whoLaunches.execute();
            }
        }
    }

    public void setIterator(int iterator) {
        this.iterator = iterator;
    }

    public int getIterator() {
        return iterator;
    }

    public void reinit() {
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
}
