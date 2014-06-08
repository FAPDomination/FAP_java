package animations;

import fap_java.CMap;
import fap_java.Cell;
import fap_java.Graph;
import fap_java.TheComputingThread;

import fap_java.Tools;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import java.io.File;

import java.io.IOException;

import javax.imageio.ImageIO;

public class AnimWarp extends Animation {
    private int[] ys;
    private int miny = -15;
    private BufferedImage img;
    private int offX = 1;
    private int offY = 4;
    
    private double startSecond;
    private double startThird;
    private double flashDuration;
    
    public AnimWarp(int x, int y, TheComputingThread theThread) {
        super(x, y, (int)(((double)(14)/24)*1000), theThread);
        startSecond = ((double)4/14)*duration;
        startThird = ((double)7/14)*duration;
        flashDuration = ((double)7/14)*duration;
        this.x+=offX;
        this.y+=offY;
        ys = new int[3];
        ys[0] = 1;
        ys[1] = 1;
        ys[2] = 1;
        //File location = new File("resources/images/animations/teleport.png");
        img = Tools.getImageToFilter(Graph.getList().get("warp"));
    }
    
    public AnimWarp(Cell c, TheComputingThread theThread) {
        this(CMap.giveTalePosition(c)[0],CMap.giveTalePosition(c)[1],theThread);
    }

    public String toString() {
        return "Warp at "+x+"-"+y;
    }

    public void paintComponent(Graphics g) {

        //TODO alpha
        float alpha;
        alpha = (float)(((double)(flashDuration-position))/duration);
        Tools.drawFilteredImage(img, 1f,1f,1f,alpha, g,x, (int)(y+miny*(((double)position)/flashDuration)));
        if(position>startSecond){
            alpha = (float)((flashDuration-(position-startSecond))/duration);
            Tools.drawFilteredImage(img, 1f,1f,1f,alpha, g,x, (int)(y+miny*(((double)(position-startSecond))/flashDuration)));
        }
        if(position>startThird){
            alpha = (float)((flashDuration-(position-startThird))/duration);
            Tools.drawFilteredImage(img, 1f,1f,1f,alpha, g,x, (int)(y+miny*(((double)(position-startThird))/flashDuration)));
        }
    }
}
