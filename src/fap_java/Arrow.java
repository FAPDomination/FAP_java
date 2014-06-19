package fap_java;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class Arrow extends Element {

    private Cell current;

    private int x;
    private int y;
    private int origX;
    private int origY;
    private int course;
    private Game game;
    private double angle;
    private Player thrower;

    private int offsetY;
    private int offsetX;
    
    private int fac;
    private int f;

    /**
     * Creates an arrow that will autommatically fly across the map
     * @param c : the start cell
     * @param course : the orientation for the arrow (NW, NE, E, SE, SW, W)
     * @param game : the game where it takes place
     * @param thrower : who threw it
     */
    public Arrow(Cell c, int course, Game game, Player thrower) {
        this.course = course;
        this.current = c;
        this.game = game;
        this.x = CMap.giveTalePosition(c.getI(), c.getJ())[0] + (CMap.TW / 2);
        this.y = CMap.giveTalePosition(c.getI(), c.getJ())[1] + (CMap.TH / 2);
        this.origX = x;
        this.origY = y;
        this.f=0;
        game.addObject(this);
        this.thrower = thrower;
        this.initConstants();
    }

    public void paintComponent(Graphics g) {
        //int offsetY;
        offsetY = 0;
        g.setColor(Color.BLACK);
        
        //TODO add design of arrow
        //TODO add graphical offsets according to ori
        
        g.fillRect(x, y + offsetY, 4, 4);
    }

    /**
     * Get information about the arrow such as the thrower
     * @return info
     */
    public String toString() {
        return "An arrow thrown by " + thrower;
    }

    /**
     * Makes the arrow move and convert cells
     */
    public void effect() {
        
        y = (int)(origY + fac*f*Math.tan(angle)) + offsetY;
        x = (int)(origX + fac*f) + offsetX;
        
        boolean b = computeCell();

        if (b) {
            //Detect ennemy
            Player p;
            p = game.isOccupied(current);
            if (p != null && p != thrower) {
                p.makeHimWait((Params.howLongBlockingMagician * 1000) / 2);
                this.destroy();
            }
        }
        //Detect ennemy's cell
        if (current != null) {
            Team te = current.getOwner();
            if (te != thrower.getTeam() && current.getType() == 1) {
                current.setHp(current.getHp() - Params.archerDammage);
                if (current.getHp() <= 0) {
                    current.setOwner(thrower.getTeam());
                    current.setHp(thrower.getInitHP());
                }
            }
            if (current.isHeight()) {
                this.destroy();
            }
        } else {
            int bound = 10;
            //System.out.println(game.getWidth());
            if (this.x < -bound || this.x > game.getWidth() + bound || this.y < -bound ||
                this.y > game.getHeight() + bound) {
                this.destroy();
            }
        }
        
        f++;
    }

    /**
     * Allows the arrow to find the cell it is currently on
     * @return : a boolean value telling if the cell changed
     */
    public boolean computeCell() {
        boolean b = false;
        int[] tab = null;
        switch(course){
        case 4:
            tab = CMap.givePositionTale(x, y - (CMap.TH / 2));
            break;
        default:
            tab = CMap.givePositionTale(x, y - (CMap.TH / 2));
            break;
        }
        
        Cell c = game.getMap().getCell(tab);
        if (c != current) {
            current = c;
            b = true;
        }
        return b;
    }

    /**
     * Initialization of constants about the arrow
     */
    public void initConstants() {
        //-0.85832
        double approxAngle = -0.84532;
        fac = Params.arrowSpeed;
        switch (course) {
        case 0: //TL
            offsetY = 0;
            offsetX = 0;
            angle = -approxAngle; // In rad, approximation with Maple
            fac *= -1;
            break;
        case 1: //TR
            angle = approxAngle;
            offsetY = -8;
            offsetX = -2;
            break;
        case 2: //R
            angle = Math.PI / 2;
            offsetY = 0;
            offsetX = 0;
            break;
        case 3: //BR
            angle =  - approxAngle;
            offsetX = -17;
            offsetY = -5;
            break;
        case 4: //BL
            angle = approxAngle; // In rad, approximation with Maple
            fac *= -1;
            offsetY = -1;
            offsetX = 1;
            break;
        case 5: //L
            angle = -Math.PI / 2;
            offsetY = 0;
            offsetX = 0;
            break;
        default: //TL
            angle = approxAngle; // In rad, approximation with Maple
            offsetY = 0;
            offsetX = 0;
            break;
        }
    }

    /**
     * What, you need and explanation ?
     */
    public void destroy() {
        this.game.deleteObject(this);
    }
}
