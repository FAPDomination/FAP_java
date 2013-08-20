package fap_java;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import java.util.ArrayList;

/**
 * This class contains several tools that can be used wherever.
 * They are all set to public static to be available everywhere, and are as generic as possible so it would be easier
 * to use them in several conditions
 */
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

    /**
     * Picks a random int between two values, excluding the given int
     * Warning : can lead to an infinite loop if too much values are excluded
     * @param min : min boundary
     * @param max : max boundary
     * @exclude The ints that shall be excluded of the random picking
     * @return : a random integer between min and max
     */
    public static int randRange(int min, int max, int[] exclude){
        int rand;
        boolean b = true;
        do{
            b=true;
            // Pick a random number
            rand = randRange(min,max);
            // Check if it's supposed to be excluded
            for(int i=0;i<exclude.length;i++){
                int a = exclude[i];
                if(a==rand){
                    // If yes, start again
                    b = false;
                }
            }
        }
        while(!b);
        return rand;
    }
    
    /**
     * Removes the null values from an ArrayList, and their slots
     * @param al The designated ArrayList to purge
     * @return The purged arrayList
     */
    public static ArrayList removeNull(ArrayList al){
        // Loop on
        while(al.contains(null)){
            // Read the AL
            for(int i=0;i<al.size();i++){
                // Get the object
                Object o = al.get(i);
                if(o==null){
                    // If it's null, remove it
                    al.remove(o);
                }
            }
        }
        return al;
    }
}
