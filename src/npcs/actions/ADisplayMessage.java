package npcs.actions;


import animations.NPCMessage;

import fap_java.Game;
import fap_java.NPC;


public class ADisplayMessage extends Action {
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = -7274855256989454061L;
    private String message;
    private transient NPCMessage npcMessage;

    /**
     * Creates an NPC that will display a message on screen that lasts until the SKILL key is pressed
     * @param message the message to display
     * @param next the next action to be executed by the NPC
     */
    public ADisplayMessage(String message, Action next) {
        super(next);
        this.message = message;

    }

    public void execute(NPC whoLaunches) {
        // First passage : display the message
        if (this.npcMessage == null) {
            this.npcMessage = new NPCMessage(message, whoLaunches.getGame().getThread());
        }
        // Second : remove the message and go on
        else {
            if (npcMessage != null) {
                npcMessage.endAnimation();
            }
            whoLaunches.gotoNextAction();
            whoLaunches.execute();

        }
    }

    public void reinit() {
        super.reinit();

        if (npcMessage != null) {
            npcMessage.endAnimation();
        }
        npcMessage = null;
    }

    public void setTransientValues(Game g) {
    }
}
