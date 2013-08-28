package npcs;

import fap_java.Cell;
import fap_java.Game;
import fap_java.NPC;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

public class NPCExit extends NPC {
    public NPCExit(Cell cell, Game game) {
        super(cell, true, false, null, game, 0,0,null);
        //TODO Update list of actions
    }
    public void paintComponent(Graphics g){}
}
