package fap_java;

import java.awt.Color;
import java.awt.Graphics;

public abstract class Human extends Element{
    
    public Human() {
        super();
    }
    
    public void paintComponent(Graphics g) {
        int x = CMap.giveTalePosition(this.getI(), this.getJ())[0] + Params.OFFX;
        int y = CMap.giveTalePosition(this.getI(), this.getJ())[1] + Params.OFFY;
        
        g.setColor(Color.RED);
        g.drawRect(x, y, 10, 30);
    }
}
