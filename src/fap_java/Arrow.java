package fap_java;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

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
    private Point offsetShadow;
    private Point GoffsetArrow;
    
    private int fac;
    private int f;
    
    private Image img;
    private int width;
    private int height;

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
        
        
        
        img = Graph.getList().get("Arrow_"+course);
        width = img.getWidth(game);
        height = img.getHeight(game);
        this.initConstants();
    }

    public void paintComponent(Graphics g) {
        //int offsetY;
        offsetY = 0;
        g.setColor(Color.BLACK);
        
        //TODO swaggy arrow ?
        
        g.drawImage(Graph.characters.get("shadow"), x +offsetShadow.x ,y+offsetShadow.y, 30,25, game);
        
        g.drawImage(img, x+GoffsetArrow.x, y+GoffsetArrow.y, width/CMap.FAC, height/CMap.FAC, game);
        
        //g.fillRect(x, y + offsetY, 4, 4);
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
        if(course == 2){
            //RIGHT
            x+=fac;
        }
        else if(course == 5){
            //LEFT
            x-=fac;
        }
        else{
            y = (int)(origY + fac*f*Math.tan(angle)) + offsetY;
            x = (int)(origX + fac*f) + offsetX;
        }
        boolean b = computeCell();

        if (b) {
            //Detect ennemy
            if(current != null){
                Player p = current.getOccupied();
                if (p != null && p != thrower) {
                    p.makeHimWait((Params.howLongBlockingMagician * 1000) / 2);
                    this.destroy();
                }
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
            offsetX = -15;
            angle = -approxAngle; // In rad, approximation with Maple
            fac *= -1;
            offsetShadow = new Point(12,0);
            GoffsetArrow = new Point(16,-12);
            break;
        case 1: //TR
            angle = approxAngle;
            offsetY = -8;
            offsetX = -2;
            offsetShadow = new Point(-25,0);
            GoffsetArrow = new Point(-30,-20);
            break;
        case 2: //R
            offsetY = 0;
            offsetX = 0;
            offsetShadow = new Point(-5,-12);
            GoffsetArrow = new Point(-10,-25);
            break;
        case 3: //BR
            angle =  - approxAngle;
            offsetX = -16;
            offsetY = -5;
            offsetShadow = new Point(10,0);
            GoffsetArrow = new Point(12,-15);
            break;
        case 4: //BL
            angle = approxAngle; // In rad, approximation with Maple
            fac *= -1;
            offsetY = -1;
            offsetX = 1;
            offsetShadow = new Point(-25,0);
            GoffsetArrow = new Point(-22,-20);
            break;
        case 5: //L
            offsetY = 0;
            offsetX = 0;
            offsetShadow = new Point(-5,-12);
            GoffsetArrow = new Point(-8,-25);
            break;
        default: //TL
            try {
                throw(new Exception("Wrong ori"));
            } catch (Exception e) {
            }
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
