package fap_java;

import java.awt.Graphics;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CMap {
    public static final int FAC = 2;
    public static final int TW = 60/FAC;
    public static final int TH = 35/FAC;
    public static final int OFFMAP = 0;
    
    private static Map<int[], Cell> myMap = new HashMap<int[], Cell>();
    
   public static int[] giveTalePosition(int i, int j) {
            int[] arr = new int[2];
            // calculate the corresponding position
            arr[0] = j*TW+(TW/2)*(i%2);
            arr[1] = i*(TH)*(1-1/4)+OFFMAP;
            return arr;
    };
    
   public static int[] givePositionTale(int x, int y) {
        int[] arr = new int[2];
            // Undo the calculus of the position
            arr[0] = Math.round(((y-OFFMAP)/TH)*(4/3));
            if (arr[0]%2 == 0) {
                    arr[1] = (x/TW);
            } else {
                    arr[1] = ((x/TW)-1/2);
            }
            return arr;
    };
   
    public void paintComponent(Graphics g) {
        //TODO with a maxI and maxJ methods
        //Emergency solution with a collection
        Collection col = myMap.values();
        Object[] colA = col.toArray();
        for(int i=0; i<col.size();i++){
            Cell c = (Cell)colA[i];
            c.paintComponent(g);
        }
    }

    public static Map<int[], Cell> getMyMap() {
        return myMap;
    }
    
    public static void addElement(Cell c){
        int[] tab = new int[2];
        tab[0] = c.getI();
        tab[1] = c.getJ();
        myMap.put(tab, c);
    }
    
    public static void removeElement(Cell c){
        myMap.remove(c);
    }
    
    public static boolean containsCell(Cell c){
        return myMap.containsValue(c);
    }
    
    public static Cell getCell(int[] tab){
        Cell c;
        if(tab.length == 2 && myMap.containsKey(tab)){
            c = myMap.get(tab);
        }
        else{
            c=null;
        }
        return c;
    }
}
