package npcs;

import fap_java.Cell;
import fap_java.Game;
import fap_java.NPC;

public class NPCWMBlocking extends NPC{
    public NPCWMBlocking(Cell pos) {
        super(pos,false,false,null,null);
        // Update image and override paintComponent
    }
}
