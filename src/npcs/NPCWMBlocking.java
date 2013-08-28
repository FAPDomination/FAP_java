package npcs;

import fap_java.Cell;
import fap_java.Game;
import fap_java.Graph;
import fap_java.NPC;

import fap_java.Tools;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class NPCWMBlocking extends NPC{
    public NPCWMBlocking(Cell pos) {
        super(pos,false,false,null,null,(int)Graph.getOffsetsCells().get(0).getWidth()+1,(int)(Graph.getOffsetsCells().get(0).getWidth()-8));
        // Update image and override paintComponent
        img = Graph.getBasicCellImage();
    }
    
    public void paintComponent(Graphics g){
        double width = (this.img.getWidth(game) * Graph.getFacW());
        double height = (this.img.getHeight(game) * Graph.getFacH());
        double scaleX = width/img.getWidth(game);
        double scaleY = height/img.getWidth(game);
        float[] offsets = new float[4];
        float[] scales = {0.1f,0.1f,0.1f,(float)0.6};
        Tools.drawFilteredImage((BufferedImage)img, scales, offsets, g, x, y, scaleX, scaleY);
    }
}
