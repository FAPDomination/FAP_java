package fap_java;

import java.awt.Graphics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CMap {
    public static final int FAC = 2;
    public static final int TW = 60/FAC;
    public static final int TH = 35/FAC;
    public static final int OFFMAP = 0;
    
    public CMap(){
        
    }
    
    //private Map<int[], Cell> myMap = new HashMap<int[], Cell>();
    private ArrayList<Cell> myMap = new ArrayList<Cell>();
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
        //TODO with a maxI and maxJ methods ??
        //Emergency solution with a collection
        for(int i=0; i<myMap.size();i++){
            Cell c = myMap.get(i);
            c.paintComponent(g);
        }
    }

    public ArrayList<Cell> getMyMap() {
        return myMap;
    }
    
    public void addElement(Cell c){
        if(containsCell(c)!=-1){
            myMap.remove(containsCell(c));
        }
        c.setMap(this);
        myMap.add(c);
    }
    
    public void removeElement(Cell c){
        myMap.remove(c);
    }
    
    public int containsCell(Cell c){
        int b = (-1);
        for(int k=0;k<myMap.size();k++){
            Cell o = myMap.get(k);
            if(o.equals(c)){
                b = k;
                break;
            }
        }
        return b;
    }
    
    public Cell getCell(int[] tab){
        Cell c;
        Cell o = new Cell(tab[0],tab[1],1);
        if(tab.length == 2 && containsCell(o)!=(-1)){
            c = myMap.get(containsCell(o));
        }
        else{
            c=null;
        }
        return c;
    }
    
    public Cell getCell(int i, int j){
        int[] tab = new int[2];
        tab[0] = i;
        tab[1] = j;
        return getCell(tab);
    }
    
    public int countNeighbours(Cell c){
        int n=0;
        int i=c.getI();
        int j = c.getJ();
        Player owns = c.getOwner();
        Cell o;
        // cells from the superior line
                if (i != 0) {
                        // not the first line
                        if (i%2 == 0) {
                                // line with even index
                                if (j != 0) {
                                        // not the first column
                                        if (getCell(i-1,j-1) != null && getCell(i-1,j-1).getOwner() == owns) {
                                                n++;
                                        }
                                }
                                if (getCell(i-1,j) != null && getCell(i-1,j).getOwner() == owns) {
                                        n++;
                                }
                        } else {
                                // line with odd index
                               // if (j != map[i].length-1) {
                                        // not the last column
                                        if (getCell(i-1,j+1) != null && getCell(i-1,j+1).getOwner() == owns) {
                                                n++;
                                        }
                                //}
                                if (getCell(i-1,j) != null && getCell(i-1,j).getOwner() == owns) {
                                        n++;
                                }
                        }
                }
                // cells from the same line     
                if (j != 0) {
                        if (getCell(i,j-1) != null && getCell(i,j-1).getOwner() == owns) {
                                n++;
                        }
                }
               // if (j != map[i].length-1) {
                        if (getCell(i,j+1) != null && getCell(i,j+1).getOwner() == owns) {
                                n++;
                        }
               // }
                // cells from the inferior line     
                //if (i != map.length-1) {
                        if (i%2 == 0) {
                                if (j != 0) {
                                        if (getCell(i+1,j-1) != null && getCell(i+1,j-1).getOwner() == owns) {
                                                n++;
                                        }
                                }
                                if (getCell(i+1,j) != null && getCell(i+1,j).getOwner() == owns) {
                                        n++;
                                }
                        } else {
                               // if (j != map[i].length-1) {
                                        if (getCell(i+1,j+1) != null && getCell(i+1,j+1).getOwner() == owns) {
                                                n++;
                                        }
                               // }
                                if (getCell(i+1,j) != null && getCell(i+1,j).getOwner() == owns) {
                                        n++;
                                }
                        }
                //}
        // You just lost the game.
        return n;
    }
 
}
