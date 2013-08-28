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

import javax.imageio.ImageIO;

public class NPCWMStarting extends NPC{
    private boolean conquered;
    private BufferedImage img;
    
    public NPCWMStarting(Cell pos, boolean conquered, Game game) {
        super(pos,true,false,null,game, Graph.getOffsetsCells().get(0));
        this.conquered = conquered;
        // Update image and override paintComponent
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
