package fap_java;

import java.awt.Graphics;

public abstract class Human extends Element{
    public Human() {
        super();
    }
    
    public abstract void paintComponent(Graphics g);
}
