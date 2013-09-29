package npcs;

import fap_java.Cell;
import fap_java.Game;
import fap_java.Graph;
import fap_java.NPC;

import fap_java.Tools;

import gui.Fapplication;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class NPCWMBlocking extends NPC{
    public NPCWMBlocking(Cell pos) {
        super(pos,false,false,null,Fapplication.getWorldMap(),(int)Graph.getOffsetsCells().get(0).getWidth()+1,(int)(Graph.getOffsetsCells().get(0).getWidth()-8),null);
        //TODO Update image and override paintComponent
        img = Graph.getBasicCellImage();
    }
    
    public void paintComponent(Graphics g){
        double width = (this.img.getWidth(game.getDisplayer()) * Graph.getFacW());
        double height = (this.img.getHeight(game.getDisplayer()) * Graph.getFacH());
        double scaleX = width/img.getWidth(game.getDisplayer());
        double scaleY = height/img.getWidth(game.getDisplayer());
        float[] offsets = new float[4];
        float[] scales = {0.1f,0.1f,0.1f,(float)0.6};
        Tools.drawFilteredImage((BufferedImage)img, scales, offsets, g, x, y, scaleX, scaleY);
    }
    
    public void execute(){};
}
