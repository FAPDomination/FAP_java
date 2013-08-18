package pathFinder;

import fap_java.Cell;

public class Node{
    
                    private double g;
                    private double h;
                    private double f;
                    private Cell cell;
                    private int col;
                    private int line;
                    private boolean walkable;
                    private Node parent;
    
    public Node(Cell c) {
        //super();
        cell = c;
        col = c.getJ();
        line = c.getI();
        walkable = c.isWalkable();
        g=0;
        f=0;
        h=0;
        parent = this;
    }

    public String toString() {
        return "The Node";
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getG() {
        return g;
    }

    public void setH(double h) {
        this.h = h;
    }

    public double getH() {
        return h;
    }

    public void setF(double f) {
        this.f = f;
    }

    public double getF() {
        return f;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getCol() {
        return col;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getLine() {
        return line;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public void setParent(Node m_parent) {
        this.parent = m_parent;
    }

    public Node getParent() {
        return parent;
    }
    
    public boolean equals(Node c){
        boolean b = false;
        if(this.getLine() == c.getLine() && this.getCol() == c.getCol()){
            b = true;
        }
        return b;
    }
}
