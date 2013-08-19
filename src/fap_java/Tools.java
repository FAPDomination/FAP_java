package fap_java;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import java.awt.image.RescaleOp;

import java.util.ArrayList;

public class Tools {
    /**
     * Picks a random int between two value
     * @param min : min boundary
     * @param max : max boundary
     * @return : a random integer between min and max
     */
    public static int randRange(int min, int max) {
        int randomNum = (int)(Math.random() * (max - min + 1) + min);
        return randomNum;
    }

    public static int randRange(int min, int max, int[] exclude){
        int rand;
        boolean b = true;
        do{
            b=true;
            rand = randRange(min,max);
            for(int i=0;i<exclude.length;i++){
                int a = exclude[i];
                if(a==rand){
                    b = false;
                }
            }
        }
        while(!b);
        return rand;
    }
    
    public static ArrayList removeNull(ArrayList al){
        while(al.contains(null)){
            for(int i=0;i<al.size();i++){
                Object o = al.get(i);
                if(o==null){
                    al.remove(o);
                }
            }
        }
        return al;
    }
    
    // Graphical tools
    
    public static BufferedImage getImageToFilter(Image image){
        BufferedImage img = (BufferedImage)image;
        
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        
        Graphics gBi = bi.getGraphics();
        gBi.drawImage(img, 0, 0, null);
        
        return bi;
    }
    
    public static void drawFilteredImage(BufferedImage img, float[] scales, float[] offsets, Graphics g, int x, int y){
        RescaleOp rop = new RescaleOp(scales, offsets, null);
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(img, rop, x, y);
    }
    
    public static void drawFilteredImage(BufferedImage img, float Rfactor, float Gfactor, float Bfactor, float alpha, Graphics g, int x, int y){
        float[] offsets = new float[4];
        float[] scales = {Rfactor,Gfactor,Bfactor,alpha};
        drawFilteredImage(img, scales, offsets, g,x,y);
    }
}
