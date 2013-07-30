package fap_java;

import java.awt.Color;
import java.awt.Graphics;

public class Arrow {
    
    private Cell current;
    private int x;
    private int y;
    private int course;
    private Game game;
    
    public Arrow(Cell c, int course, Game game) {
        this.course = course;
        this.current = c;
        this.game = game;
        this.x = CMap.giveTalePosition(c.getI(), c.getJ())[0]+(CMap.TW/2);
        this.y = CMap.giveTalePosition(c.getI(), c.getJ())[1] + CMap.OFFMAP+(CMap.TH/2);
    }
    
    public void paintComponent(Graphics g) {
        int offsetY;
        switch(course){
        case 0:
            offsetY = 5;
            break;
            case 1:
            offsetY = 5;
                break;
            case 2:
            offsetY = 5;
                break;
            case 3:
            offsetY = 5;
                break;
            case 4:
            offsetY = 5;
                break;
            case 5:
            offsetY = 5;
                break;
        default:
            offsetY = 5;
            break;
        }
        
        g.setColor(Color.BLACK);
        g.fillRect(x, y+offsetY, 4, 4);
    }
}
