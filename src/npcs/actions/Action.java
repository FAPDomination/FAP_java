package npcs.actions;

import fap_java.Game;
import fap_java.NPC;

import java.io.Serializable;

public interface Action extends Serializable{
    public void execute(NPC whoLaunches);
    public void reinit();
    public void setNext(Action next);
    public Action getNext();
}
