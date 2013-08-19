package animations;

import fap_java.Graph;
import fap_java.TheThread;

import fap_java.Tools;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import java.io.File;

import java.io.IOException;

import javax.imageio.ImageIO;

public class AnimWarp extends Animation {
    private int[] ys;
    private int miny = -21;
    private BufferedImage img;
    public AnimWarp(int x, int y, TheThread theThread) {
        super(x, y, (int)(((double)(14)/24)*1000), theThread);
        ys = new int[3];
        ys[0] = 1;
        ys[1] = 1;
        ys[2] = 1;
        File location = new File("warp.png");
        try {
            img = Tools.getImageToFilter(ImageIO.read(location));
        } catch (IOException e) {
        }
    }

    public String toString() {
        return "Warp at "+x+"-"+y;
    }

    public void paintComponent(Graphics g) {
        System.out.println("paint");
        double nFrame = ((double)position)/duration;
        Tools.drawFilteredImage(img, 1f,1f,1f,(float)(1-(nFrame)), g,x,y);
    }
}
