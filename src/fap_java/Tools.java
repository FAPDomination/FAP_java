package fap_java;

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
}
