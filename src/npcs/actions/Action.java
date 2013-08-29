package npcs.actions;

import fap_java.Game;
import fap_java.NPC;

public interface Action {
    public void execute(NPC whoLaunches);
}
