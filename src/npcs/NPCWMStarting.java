package npcs;

import fap_java.Cell;
import fap_java.Game;
import fap_java.NPC;

public class NPCWMStarting extends NPC{
    private boolean conquered;
    public NPCWMStarting(Cell pos, boolean conquered, Game game) {
        super(pos,true,false,null,game);
        this.conquered = conquered;
        // Update image and override paintComponent
    }

    public void setConquered(boolean conquered) {
        this.conquered = conquered;
    }

    public boolean isConquered() {
        return conquered;
    }
}
