package fap_java;

import java.awt.Graphics;

import java.awt.List;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CMap {
    /**
     * A factor that multiplies the sizes of EEEEEEEverything
     */
    public static final int FAC = 2;

    /**
     * Tile Width
     */
    public static final int TW = 60 / FAC;

    /**
     * Tile Height
     */
    public static final int TH = 35 / FAC;

    /**
     * Offset on y that the map has
     */
    public static final int OFFMAP = 10;

    /**
     * Creates a map with all tools needed.
     */
    public CMap(Game game, int fileID) {
        this.game = game;
        this.fileID = fileID;
    }

    /**
     * The arrayList of cells that contains aaaall the cells of the grid
     */
    private ArrayList<Cell> myMap = new ArrayList<Cell>();
    
    private Map<String,Cell> hashMap = new HashMap<String,Cell>();

    /**
     * The starting points of this map (defined in the XML file)
     */
    private ArrayList<Cell> startCells = new ArrayList<Cell>();

    /**
     * The game where it's happening
     */
    private Game game;

    private int fileID;
    
    // Performances
    private int mapSize=0;
    private Cell painting;
    // One time calculations
    private ArrayList<Cell> takeableCells = null;
    private Cell firstCell;
    private int[] maxIJ = null;

    /**
     * Give the position in pixels of a couple a values
     * @param i : the line index
     * @param j : the column index
     * @return : a table [x,y]
     */
    public static int[] giveTalePosition(int i, int j) {
        int[] arr = new int[2];
        // calculate the corresponding position
        arr[0] = j * TW + (TW / 2) * (i % 2);
        arr[1] = i * (TH) * (1 - 1 / 4);
        return arr;
    };

    /**
     * Give the position in pixel of a cell
     * @param c
     * @return : a table [x,y]
     */
    public static int[] giveTalePosition(Cell c) {
        return giveTalePosition(c.getI(), c.getJ());
    }

    /**
     * Give the index of line and column in the grid of a point located at (x,y)
     * @param x
     * @param y
     * @return : a table [i,j]
     */
    public static int[] givePositionTale(int x, int y) {
        int[] arr = new int[2];
        // Undo the calculus of the position
        arr[0] = Math.round(((y) / TH) * (4 / 3));
        if (arr[0] % 2 == 0) {
            arr[1] = (x / TW);
        } else {
            arr[1] = ((x / TW) - 1 / 2);
        }
        return arr;
    };

    /**
     *  Repaints the map by reading le list of cells
     * @param g
     */
    public void paintComponent(Graphics g) {
        //Emergency solution with a collection
        for (int i = 0; i < mapSize; i++) {
            painting.paintComponent(g);
            NPC npc = Tools.checkNPCOnCell(game, painting);
            if (npc != null) {
                npc.paintComponent(g);
            }
            
            for (int j = 0; j < game.getPlayers().size(); j++) {
                Player q = game.getPlayers().get(j);
                // Check position
                if(q.getDrawn() == painting){
                    q.paintComponent(g);
                }
            }
            painting = painting.getNextInMap();
        }
        //Reset
        painting = firstCell;
        /*
        //TODO with a maxI and maxJ methods ??
        //Emergency solution with a collection
        for (int i = 0; i < myMap.size(); i++) {
            Cell c = myMap.get(i);
            c.paintComponent(g);
            NPC npc = Tools.checkNPCOnCell(game, c);
            if (npc != null) {
                npc.paintComponent(g);
            }
            
            for (int j = 0; j < game.getPlayers().size(); j++) {
                Player q = game.getPlayers().get(j);
                // Check position
                if(q.getDrawn() == c){
                    q.paintComponent(g);
                }
            }

        }
        */  
    }

    //TODO Improvement of this method including gaps

    public ArrayList<Cell> tileOnPath(Cell c, int ori) {
        ArrayList<Cell> path = new ArrayList<Cell>();
        int i = c.getI();
        int j = c.getJ();
        boolean bool = true;
        int[] maxIJ = this.getMaxIJ();
        int facI = 0;
        int facJ = 0;

        while (bool) {
            i += facI;
            j += facJ;
            c = this.getCell(i, j);
            //c = this.surroundingCells(c).get(ori);
            if ((c != null && c.isHeight()) || (i < 0 || j < 0) || (i > maxIJ[0] || j > maxIJ[1])) {
                bool = false;
            } else {
                if (c != null) {
                    path.add(c);
                }
                switch (ori) {
                case 0:
                    if (i % 2 == 0) {
                        facI = -1;
                        facJ = -1;
                    } else {
                        facI = -1;
                        facJ = 0;
                    }
                    break;
                case 1:
                    if (i % 2 == 0) {
                        facI = -1;
                        facJ = 0;
                    } else {
                        facI = -1;
                        facJ = +1;
                    }
                    break;
                case 2:
                    facI = 0;
                    facJ = 1;
                    break;
                case 3:
                    if (i % 2 == 0) {
                        facI = +1;
                        facJ = 0;
                    } else {
                        facI = +1;
                        facJ = +1;
                    }
                    break;
                case 4:
                    if (i % 2 == 0) {
                        facI = +1;
                        facJ = -1;
                    } else {
                        facI = +1;
                        facJ = 0;
                    }
                    break;
                case 5:
                    facI = 0;
                    facJ = -1;
                    break;
                }
            }
        }
        return path;
    }

    //------ Accessors for the map

    public int[] getMaxIJ() {
        if(maxIJ == null){
            maxIJ = new int[2];
            int i = 0;
            int j = 0;
            Cell c = firstCell;
            for (int k = 0; k < mapSize; k++) {
                if (c != null) {
                    if (c.getI() > i) {
                        i = c.getI();
                    }
                    if (c.getJ() > j) {
                        j = c.getJ();
                    }
                }
                c = c.getNextInMap();
            }
            maxIJ[0] = i;
            maxIJ[1] = j;
        }
            return maxIJ;
    }

    public ArrayList<Cell> getMyMap() {
        return myMap;
    }

    /**
     * Adds a cell to the map and replaces it if needed
     * @param c the cell to be added
     */
    public void addElement(Cell c) {
        Cell o = containsCell(c);
        if (o != null) {
            myMap.remove(o);
        }
        c.setMap(this);
        if(mapSize>=1){
            myMap.get(myMap.size()-1).setNextInMap(c);
        }
        else{
            painting = c;
            firstCell = c;
        }
        myMap.add(c);
        mapSize++;
        
        hashMap.put(""+c.getI()+","+c.getJ(), c);
    }

    public void removeElement(Cell c) {
        myMap.remove(c);
    }

    /**
     * Checks if the map contains a cell
     * @param c
     * @return : null if not, the object if yes
     */
    public Cell containsCell(Cell c) {
        return hashMap.get(""+c.getI()+","+c.getJ());
    }
    
    public Cell getCell(String hash) {
        return hashMap.get(hash);
    }

    /**
     * Gets the cell in the map  for a couple of values [i,j]
     * @param tab
     * @return : the cell
     */
    public Cell getCell(int[] tab) {
        Cell o = new Cell(tab[0], tab[1], 1, 1, null);
        if (tab.length == 2 && tab[0] >=0 && tab[1] >=0) {
            return(containsCell(o));
        }
        return null;
    }

    /**
     * Gets the cell in the map  for two values i and j
     * @param i : line index
     * @param j : column index
     * @return : the cell
     */
    public Cell getCell(int i, int j) {
        int[] tab = new int[2];
        tab[0] = i;
        tab[1] = j;
        return getCell(tab);
    }

    /**
     * Counts the neighbour (same owner) around the designated cell
     * @param c
     * @return : the number of friend cells around
     */
    public int countNeighbours(Cell c) {
        int n = 0;
        Team owns = c.getOwner();

        ArrayList<Cell> surround = surroundingCells(c);
        for (int i = 0; i < surround.size(); i++) {
            Cell k = surround.get(i);
            if (k != null && k.getOwner() == owns) {
                n++;
            }
        }

        // You just lost the game
        return n;
    }
    
    public boolean countNeighboursForConway(Cell c) {
        int n = 0;
        Team owns = c.getOwner();
        // Check all six cells around
        
        int i = c.getI();
        int j = c.getJ();
        Cell o;
        // top cells
        // not the first line
        if (i % 2 == 0) {
            o = this.getCell(i - 1, j - 1);
            if(o!=null && o.getOwner() != null && o.getOwner()==owns){
                n++;
                if(n>=Params.nNeighboursConway){
                    return true;
                }
            }
            //surroundingCells['tr'] = [i-1, j];
            o = this.getCell(i - 1, j);
            if(o!=null && o.getOwner() != null && o.getOwner()==owns){
                n++;
                if(n>=Params.nNeighboursConway){
                    return true;
                }
            }
        } else {

            //surroundingCells['tl'] = [i-1, j];
            o = this.getCell(i - 1, j);
            if(o!=null && o.getOwner() != null && o.getOwner()==owns){
                n++;
                if(n>=Params.nNeighboursConway){
                    return true;
                }
            }

            //surroundingCells['tr'] = [i-1, j+1];
            o = this.getCell(i - 1, j + 1);
            if(o!=null && o.getOwner() != null && o.getOwner()==owns){
                n++;
                if(n>=Params.nNeighboursConway){
                    return true;
                }
            }
        }
        // cells from the same line
        //surroundingCells['l'] = [i, j-1];
        o = this.getCell(i, j - 1);
        if(o!=null && o.getOwner() != null && o.getOwner()==owns){
            n++;
            if(n>=Params.nNeighboursConway){
                return true;
            }
        }
        //surroundingCells['r'] = [i, j+1];
        o = this.getCell(i, j + 1);
        if(o!=null && o.getOwner() != null && o.getOwner()==owns){
            n++;
            if(n>=Params.nNeighboursConway){
                return true;
            }
        }
        // bottom cells (see top cells)
        if (i % 2 == 0) {
            //surroundingCells['bl'] = [i+1, j-1];
            o = this.getCell(i + 1, j - 1);
            if(o!=null && o.getOwner() != null && o.getOwner()==owns){
                n++;
                if(n>=Params.nNeighboursConway){
                    return true;
                }
            }
            //surroundingCells['br'] = [i+1, j];
            o = this.getCell(i + 1, j);
            if(o!=null && o.getOwner() != null && o.getOwner()==owns){
                n++;
                if(n>=Params.nNeighboursConway){
                    return true;
                }
            }
        } else {
            //surroundingCells['br'] = [i+1, j+1];
            o = this.getCell(i + 1, j + 1);
            if(o!=null && o.getOwner() != null && o.getOwner()==owns){
                n++;
                if(n>=Params.nNeighboursConway){
                    return true;
                }
            }
            //surroundingCells['bl'] = [i+1, j];
            o = this.getCell(i + 1, j);
            if(o!=null && o.getOwner() != null && o.getOwner()==owns){
                n++;
                if(n>=Params.nNeighboursConway){
                    return true;
                }
            }
        }

        // You just lost the game
        return false;
    }

    /**
     * Returns the 6 cells surrounding the designated tile
     * @param c
     * @return
     */
    public ArrayList<Cell> surroundingCells(Cell c) {
        // Check all six cells around
        ArrayList<Cell> surroundingCells = new ArrayList<Cell>(6);
        
        for (int k = 0; k < 6; k++) {
            surroundingCells.add(null);
        }
        
        int i = c.getI();
        int j = c.getJ();
        Cell o;
        // top cells
        // not the first line
        if (i % 2 == 0) {
            o = this.getCell(i - 1, j - 1);
            surroundingCells.set(0, o);
            //surroundingCells['tr'] = [i-1, j];
            o = this.getCell(i - 1, j);
            surroundingCells.set(1, o);
        } else {

            //surroundingCells['tl'] = [i-1, j];
            o = this.getCell(i - 1, j);
            surroundingCells.set(0, o);

            //surroundingCells['tr'] = [i-1, j+1];
            o = this.getCell(i - 1, j + 1);
            surroundingCells.set(1, o);
        }
        // cells from the same line
        //surroundingCells['l'] = [i, j-1];
        o = this.getCell(i, j - 1);
        surroundingCells.set(5, o);
        //surroundingCells['r'] = [i, j+1];
        o = this.getCell(i, j + 1);
        surroundingCells.set(2, o);
        // bottom cells (see top cells)
        if (i % 2 == 0) {
            //surroundingCells['bl'] = [i+1, j-1];
            o = this.getCell(i + 1, j - 1);
            surroundingCells.set(4, o);
            //surroundingCells['br'] = [i+1, j];
            o = this.getCell(i + 1, j);
            surroundingCells.set(3, o);
        } else {
            //surroundingCells['br'] = [i+1, j+1];
            o = this.getCell(i + 1, j + 1);
            surroundingCells.set(3, o);
            //surroundingCells['bl'] = [i+1, j];
            o = this.getCell(i + 1, j);
            surroundingCells.set(4, o);
        }
        return surroundingCells;
    };

    /**
     * Scan the map to get all takable cell (type 1)
     * @return : an arrayList of all takable cells on the grid
     */
    public ArrayList<Cell> getTakableCells() {
        if(takeableCells == null){
           takeableCells = new ArrayList<Cell>();
            Cell c = firstCell;
            for (int i = 0; i < mapSize; i++) {
                //Check if type 1
                if (c.getType() == 1) {
                    //If yes add it to list
                    takeableCells.add(c);
                }
                c = c.getNextInMap();
            }
            
        }
        return takeableCells;
    }

    /**
     * Gets informations about the map, such as the number of cells
     * @return
     */
    public String toString() {
        return "Map with " + mapSize + " cells";
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

    /**
     * Get the cells that are located on rings around the designated cell
     * @param c : the cell
     * @param numberOfRings : the number of rings to check
     * @return : per ring a list of cells
     */
    public Map<Integer, ArrayList<Cell>> ringsSurrounding(Cell c, int numberOfRings) {
        Map<Integer, ArrayList<Cell>> ringsOfCells = new HashMap<Integer, ArrayList<Cell>>();
        //ArrayList<Cell>[] ringsOfCells = new ArrayList<Cell>[numberOfRings+1];
        for (int k = 0; k < numberOfRings; k++) {
            ringsOfCells.put(k, new ArrayList<Cell>());
        }
        ringsOfCells.get(0).add(c);

        int ring = 1;
        boolean continueRingLoop = true; // For stopping the loop after a certain number of rings
        while (continueRingLoop) {
            ringsOfCells.put(ring, new ArrayList<Cell>());
            int indexRing = 0; // Index of the array for the current ring

            // Loop : gets the cells of the inferior ring and checks their neighbour cells
            ArrayList<Cell> previousRing = ringsOfCells.get(ring - 1);
            for (int i = 0; i < previousRing.size(); i++) {
                Cell cell = previousRing.get(i); // Inferior ring cell
                ArrayList<Cell> aroundCurrentCell = surroundingCells(cell);

                // Loop : checking if the cells must be added to the current ring array
                for (int j = 0; j < aroundCurrentCell.size(); j++) {
                    Cell tempCell = aroundCurrentCell.get(j);
                    boolean rejectCell = false;
                    // Loop : is this cell already in the returned array ?
                    for (int k = ring - 2; k <= ring; k++) {
                        if (k < 0) {
                            k = 0;
                        } // Prevents error if ring = 1
                        // use of other function arrayContainsCoords(array, coords)
                        if ((tempCell == null) || ringsOfCells.get(k).contains(tempCell)) {
                            rejectCell = true;
                        }
                    }
                    if (!rejectCell) {
                        // Update of returned array and current ring index
                        ringsOfCells.get(ring).add(tempCell);
                        indexRing++;
                    }
                }
            }
            ring++;

            // Stop condition
            if (ring > numberOfRings) {
                continueRingLoop = false;
            }
        }

        return ringsOfCells;

    }

    public void setFileID(int fileID) {
        this.fileID = fileID;
    }

    public int getFileID() {
        return fileID;
    }
    
    public void initDirts(){
        Cell c = firstCell;
        for(int i=0;i<this.mapSize;i++){
            c.setNeedDirt();
            c = c.getNextInMap();
        }
    }

    public void setMapSize(int mapSize) {
        this.mapSize = mapSize;
    }

    public int getMapSize() {
        return mapSize;
    }

    public void setFirstCell(Cell firstCell) {
        this.firstCell = firstCell;
    }

    public Cell getFirstCell() {
        return firstCell;
    }
}
