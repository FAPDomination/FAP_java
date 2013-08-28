package npcs;

import fap_java.Cell;
import fap_java.Game;
import fap_java.Graph;
import fap_java.NPC;

import fap_java.Tools;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;

import javax.imageio.ImageIO;

import npcs.actions.AStartGame;
import npcs.actions.Action;

public class NPCWMStarting extends NPC{
    private boolean conquered;
    private BufferedImage img;
    
    public NPCWMStarting(Cell pos, boolean conquered, Game game, Game gameToLaunch) {
        super(pos,true,false,null,game, Graph.getOffsetsCells().get(0),null);
        this.conquered = conquered;
        //TODO Update image and override paintComponent
        actions = new ArrayList<Action>();
        actions.add(new AStartGame(gameToLaunch));
    }

    public void paintComponent(Graphics g){
       // Tools.drawFilteredImage((BufferedImage)img, 1f,1f,1f, (float)0.5, g,x,y);
    }

    public void setConquered(boolean conquered) {
        this.conquered = conquered;
    }

    public boolean isConquered() {
        return conquered;
    }
}
