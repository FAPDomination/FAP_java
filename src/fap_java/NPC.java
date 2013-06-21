package fap_java;

import java.awt.Color;
import java.awt.Graphics;

public class NPC extends Human{
    public NPC() {
        super();
    }

    public String toString() {
        return "NPC at "+this.getI()+","+this.getJ();
    }
}
