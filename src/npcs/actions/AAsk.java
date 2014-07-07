package npcs.actions;


import animations.NPCMessage;

import fap_java.Game;
import fap_java.NPC;

/**
 * Will display a yes/no choice. The message and the "yes"/"no" options can be changed. selection is done using the SKILL key
 */
public class AAsk extends Action {
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 5191498145528206298L;

    private String message;
    private String yesOption;
    private String noOption;
    private boolean choice;
    private int iterator = 0;
    private Action failAction;
    private transient NPCMessage npcMessage;


    /**
     * Creates an NPC action that will display a yes/no choice. The message and the "yes"/"no" options can be changed. selection is done using the SKILL key
     * @param message the question message
     * @param yes the validation message
     * @param no the cancel message
     * @param failAction the action to follow if the player uses the "no" option
     * @param next the action to follow if the player uses the "yes" option
     */
    public AAsk(String message, String yes, String no, Action failAction, Action next) {
        super(next);
        this.message = message;
        this.yesOption = yes;
        this.noOption = no;
        choice = true;
        this.failAction = failAction;
    }

    public void execute(NPC whoLaunches) {
        // First passage, display the message
        if (iterator == 0) {
            this.npcMessage = new NPCMessage(message, yesOption, noOption, whoLaunches.getGame().getThread(), this);
            iterator++;
        }
        // When the choice is validated, proceed to next action
        else {
            if (npcMessage != null) {
                npcMessage.endAnimation();
            }
            if (choice) { // If the "yes" option was selected
                //Loop
                if (whoLaunches != null) {
                    whoLaunches.gotoNextAction();
                    whoLaunches.execute();
                    this.reinit();
                }
            } else {
                //End NPC
                whoLaunches.setCurrentAction(failAction);
                whoLaunches.execute();
                if (npcMessage != null) {
                    npcMessage.endAnimation();
                }

            }
        }
    }

    public void setChoice(boolean choice) {
        this.choice = choice;
        if (npcMessage != null) {
            npcMessage.setChoice(choice);
        }
    }

    public boolean isChoice() {
        return choice;
    }

    public void reinit() {
        super.reinit();
        if (npcMessage != null) {
            npcMessage.endAnimation();
        }
        iterator = 0;
        if (failAction != null) {
            failAction.reinit();
        }
    }

    public void setTransientValues(Game g) {
    }
}
