package npcs.actions;

import animations.NPCMessage;

import fap_java.NPC;

import java.io.Serializable;

public class AAsk implements Action, Serializable {
    private String message;
    private String yesOption;
    private String noOption;
    private boolean choice;
    private int iterator = 0;
    private Action failAction;
    private NPCMessage npcMessage;
    
    private Action next;
    private Action origNext;
    
    public AAsk(String message, String yes, String no, Action failAction, Action next) {
        super();
        this.message = message;
        this.yesOption = yes;
        this.noOption = no;
        choice = true;
        this.failAction = failAction;
        this.next = next;
        this.origNext = next;
    }

    public void execute(NPC whoLaunches) {
        if(iterator == 0){
            this.npcMessage = new NPCMessage(message,yesOption,noOption, whoLaunches.getGame().getThread(),this);
            iterator++;
            whoLaunches.setIterator(whoLaunches.getIterator()-1);
        }
        else{
            if(npcMessage != null){
                npcMessage.endAnimation();
            }
            if(choice){
                //Loop
                if(whoLaunches != null && whoLaunches.getIterator() <= whoLaunches.getActions().size()){
                    whoLaunches.execute();
                    this.reinit();
                }
            }
            else{
                //End NPC
                whoLaunches.setIterator(whoLaunches.getActions().size()+2);
                if(failAction != null){
                    
                    failAction.execute(whoLaunches);
                    this.reinit();
                }
                else{
                    whoLaunches.execute();
                    this.reinit();
                }
            }
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setYesOption(String yesOption) {
        this.yesOption = yesOption;
    }

    public String getYesOption() {
        return yesOption;
    }

    public void setNoOption(String noOption) {
        this.noOption = noOption;
    }

    public String getNoOption() {
        return noOption;
    }

    public void setIterator(int iterator) {
        this.iterator = iterator;
    }

    public int getIterator() {
        return iterator;
    }

    public void setChoice(boolean choice) {
        this.choice = choice;
        if(npcMessage != null){
            npcMessage.setChoice(choice);
        }
    }

    public boolean isChoice() {
        return choice;
    }

    public void reinit() {
        next = origNext;
        if(npcMessage != null){
            npcMessage.endAnimation();
        }
        iterator = 0;
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
}
