package fap_java;

import java.awt.Graphics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CMap {
    public static final int FAC = 2;
    public static final int TW = 60 / FAC;
    public static final int TH = 35 / FAC;
    public static final int OFFMAP = 0;

    public CMap() {

    }

    //private Map<int[], Cell> myMap = new HashMap<int[], Cell>();
    private ArrayList<Cell> myMap = new ArrayList<Cell>();
    private ArrayList<Cell> startCells = new ArrayList<Cell>();

    public static int[] giveTalePosition(int i, int j) {
        int[] arr = new int[2];
        // calculate the corresponding position
        arr[0] = j * TW + (TW / 2) * (i % 2);
        arr[1] = i * (TH) * (1 - 1 / 4) + OFFMAP;
        return arr;
    };

    public static int[] givePositionTale(int x, int y) {
        int[] arr = new int[2];
        // Undo the calculus of the position
        arr[0] = Math.round(((y - OFFMAP) / TH) * (4 / 3));
        if (arr[0] % 2 == 0) {
            arr[1] = (x / TW);
        } else {
            arr[1] = ((x / TW) - 1 / 2);
        }
        return arr;
    };

    public void paintComponent(Graphics g) {
        //TODO with a maxI and maxJ methods ??
        //Emergency solution with a collection
        for (int i = 0; i < myMap.size(); i++) {
            Cell c = myMap.get(i);
            c.paintComponent(g);
        }
    }

    public ArrayList<Cell> getMyMap() {
        return myMap;
    }

    public void addElement(Cell c) {
        if (containsCell(c) != -1) {
            myMap.remove(containsCell(c));
        }
        c.setMap(this);
        myMap.add(c);
    }

    public void removeElement(Cell c) {
        myMap.remove(c);
    }

    public int containsCell(Cell c) {
        int b = (-1);
        for (int k = 0; k < myMap.size(); k++) {
            Cell o = myMap.get(k);
            if (o.equals(c)) {
                b = k;
                break;
            }
        }
        return b;
    }

    public Cell getCell(int[] tab) {
        Cell c;
        Cell o = new Cell(tab[0], tab[1], 1, 1);
        if (tab.length == 2 && containsCell(o) != (-1)) {
            c = myMap.get(containsCell(o));
        } else {
            c = null;
        }
        return c;
    }

    public Cell getCell(int i, int j) {
        int[] tab = new int[2];
        tab[0] = i;
        tab[1] = j;
        return getCell(tab);
    }

    public int countNeighbours(Cell c) {
        int n = 0;
        int i = c.getI();
        int j = c.getJ();
        Team owns = c.getOwner();
        Cell o;
        // cells from the superior line
        if (i != 0) {
            // not the first line
            if (i % 2 == 0) {
                // line with even index
                if (j != 0) {
                    // not the first column
                    if (getCell(i - 1, j - 1) != null && getCell(i - 1, j - 1).getOwner() == owns) {
                        n++;
                    }
                }
                if (getCell(i - 1, j) != null && getCell(i - 1, j).getOwner() == owns) {
                    n++;
                }
            } else {
                // line with odd index
                // if (j != map[i].length-1) {
                // not the last column
                if (getCell(i - 1, j + 1) != null && getCell(i - 1, j + 1).getOwner() == owns) {
                    n++;
                }
                //}
                if (getCell(i - 1, j) != null && getCell(i - 1, j).getOwner() == owns) {
                    n++;
                }
            }
        }
        // cells from the same line
        if (j != 0) {
            if (getCell(i, j - 1) != null && getCell(i, j - 1).getOwner() == owns) {
                n++;
            }
        }
        // if (j != map[i].length-1) {
        if (getCell(i, j + 1) != null && getCell(i, j + 1).getOwner() == owns) {
            n++;
        }
        // }
        // cells from the inferior line
        //if (i != map.length-1) {
        if (i % 2 == 0) {
            if (j != 0) {
                if (getCell(i + 1, j - 1) != null && getCell(i + 1, j - 1).getOwner() == owns) {
                    n++;
                }
            }
            if (getCell(i + 1, j) != null && getCell(i + 1, j).getOwner() == owns) {
                n++;
            }
        } else {
            // if (j != map[i].length-1) {
            if (getCell(i + 1, j + 1) != null && getCell(i + 1, j + 1).getOwner() == owns) {
                n++;
            }
            // }
            if (getCell(i + 1, j) != null && getCell(i + 1, j).getOwner() == owns) {
                n++;
            }
        }
        //}
        // You just lost the game.
        return n;
    }


    public ArrayList<Cell> surroundingCells(Cell c) {
        ArrayList<Cell> surroundingCells = new ArrayList<Cell>(6);
        int i = c.getI();
        int j = c.getJ();
        Cell o;
        // top cells
        // not the first line
        if (i % 2 == 0) {
            o = this.getCell(i - 1, j - 1);
            surroundingCells.add(o);
            //surroundingCells['tr'] = [i-1, j];
            o = this.getCell(i - 1, j);
            surroundingCells.add(o);
        } else {
            
            //surroundingCells['tl'] = [i-1, j];
            o = this.getCell(i - 1, j);
            surroundingCells.add(o);
            
            //surroundingCells['tr'] = [i-1, j+1];
            o = this.getCell(i - 1, j + 1);
            surroundingCells.add(o);
        }
        // cells from the same line
        //surroundingCells['l'] = [i, j-1];
        o = this.getCell(i, j - 1);
        surroundingCells.add(o);
        //surroundingCells['r'] = [i, j+1];
        o = this.getCell(i, j + 1);
        surroundingCells.add(o);
        // bottom cells (see top cells)
        if (i % 2 == 0) {
            //surroundingCells['bl'] = [i+1, j-1];
            o = this.getCell(i + 1, j - 1);
            surroundingCells.add(o);
            //surroundingCells['br'] = [i+1, j];
            o = this.getCell(i + 1, j);
            surroundingCells.add(o);
        } else {
            //surroundingCells['br'] = [i+1, j+1];
            o = this.getCell(i + 1, j + 1);
            surroundingCells.add(o);
            //surroundingCells['bl'] = [i+1, j];
            o = this.getCell(i + 1, j);
            surroundingCells.add(o);
        }
        return surroundingCells;
    };

    public ArrayList<Cell> getTakableCells() {
        ArrayList<Cell> cells = new ArrayList<Cell>();
        for (int i = 0; i < myMap.size(); i++) {
            Cell c = myMap.get(i);
            if (c.getType() == 1) {
                cells.add(c);
            }
        }
        return cells;
    }

    public String toString() {
        return "Map with " + myMap.size() + " cells";
    }


    public void addStartCell(Cell c, int index) {
        this.startCells.add(index, c);
    }

    public void removeStartCell(int index) {
        this.startCells.remove(index);
    }

    public void setStartCells(ArrayList<Cell> startCells) {
        this.startCells = startCells;
    }

    public ArrayList<Cell> getStartCells() {
        return startCells;
    }
    
    public Map<Integer, ArrayList<Cell>> ringsSurrounding(Cell c, int numberOfRings){
        Map<Integer, ArrayList<Cell>> ringsOfCells = new HashMap<Integer, ArrayList<Cell>>();
        //ArrayList<Cell>[] ringsOfCells = new ArrayList<Cell>[numberOfRings+1];
        for(int k=0;k<numberOfRings;k++){
            ringsOfCells.put(k, new ArrayList<Cell>());
        }
        ringsOfCells.get(0).add(c);
        
        int ring = 1;
        boolean continueRingLoop = true;        // For stopping the loop after a certain number of rings
        while(continueRingLoop){
           ringsOfCells.put(ring, new ArrayList<Cell>());
            int indexRing = 0;            // Index of the array for the current ring
             
            // Loop : gets the cells of the inferior ring and checks their neighbour cells
            ArrayList<Cell> previousRing = ringsOfCells.get(ring-1);
            for(int i = 0; i<previousRing.size(); i++){
                Cell cell = previousRing.get(i);        // Inferior ring cell
                ArrayList<Cell> aroundCurrentCell = surroundingCells(cell);
                 
                // Loop : checking if the cells must be added to the current ring array
                for(int j =0; j<aroundCurrentCell.size();j++){
                    Cell tempCell = aroundCurrentCell.get(j);
                    boolean rejectCell = false;
                    // Loop : is this cell already in the returned array ?
                    for(int k = ring-2 ; k <= ring ; k++){
                        if(k < 0){ k = 0; } // Prevents error if ring = 1
                        // use of other function arrayContainsCoords(array, coords)
                        if((tempCell == null) || ringsOfCells.get(k).contains(tempCell)){
                            rejectCell = true;
                        }
                    }
                    if(!rejectCell){
                        // Update of returned array and current ring index
                        ringsOfCells.get(ring).add(tempCell);
                        indexRing++;
                    }
                }
            }
            ring++;
             
            // Stop condition
            if(ring > numberOfRings){
                continueRingLoop = false;
            }
        }

        return ringsOfCells;

    }
}
