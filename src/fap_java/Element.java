package fap_java;

import java.awt.Graphics;

public abstract class Element {
    private int i;
    private int j;

    /**
     * An Element of the map. Can be a cell, a player, a NPC, ...
     */
    public Element() {
        super();
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getI() {
        return i;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public int getJ() {
        return j;
    }

    public abstract String toString();

    public abstract void paintComponent(Graphics g);
}
