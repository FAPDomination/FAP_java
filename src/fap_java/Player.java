package fap_java;

import java.awt.Color;
import java.awt.Graphics;

import java.security.Key;

public class Player extends Human {

    private int id;
    private int pc;
    private int tmax;
    private int lastDisplacement;
    private int skillTime;
    private int lastSkill;
    private String ori;
    private Cell current;
    private Cell parent;
    private int decLifeForced;
    private int recovLifeAuto;
    private Game game;
    //Keys
    private int[][] keys = new int[5][2];

    public Player(int[] coord, Game game) {
        super();
        this.setI(coord[0]);
        this.setJ(coord[1]);
        this.game = game;
        
        current = game.getMap().getCell(coord);
        // 38 40 39 37 : arrow keys
        keys[0][0] = 38;
        keys[1][0] = 40;
        keys[2][0] = 39;
        keys[3][0] = 37;
        
        keys[0][1] = 0;
        keys[1][1] = 0;
        keys[2][1] = 0;
        keys[3][1] = 0;
        
        tmax = game.getThread().getDelay()*10;
    }

    public void setCurrent(Cell current) {
        this.current = current;
    }

    public Cell getCurrent() {
        return current;
    }

    public void setLastDisplacement(int lastDisplacement) {
        this.lastDisplacement = lastDisplacement;
    }

    public int getLastDisplacement() {
        return lastDisplacement;
    }

    public void setLastSkill(int lastSkill) {
        this.lastSkill = lastSkill;
    }

    public int getLastSkill() {
        return lastSkill;
    }

    public int[][] getKeys() {
        return keys;
    }

    public void keyHigh(int i) {
        keys[i][1] = 1;

        // Test if displacement is allowed
        if (TheThread.getCount() - lastDisplacement >= tmax) {
            lastDisplacement = TheThread.getCount();

            // If the key LEFT is pressed
            if (keys[3][1] == 1) {
                // Move the stick
                shiftStick(-1, 0);
                // Update the stick's orientation :
                ori = "l";
            } else if (keys[2][1] == 1) { // If key RIGHT is pressed
                shiftStick(1, 0);
                // If the key1 is pressed
                ori = "r";
            } else if (keys[0][1] == 1) { // If key UP is pressed
                // reset the timer for this stick so the player is not able to move for a little while
                // Because of the hexa-grid to movings up and down are complicated
                // find if the stick is on an odd or even number of line
                if (current.getI() % 2 == 0) {
                    // Move the stick
                    shiftStick(-1, -1);

                } else {
                    shiftStick(0, -1);
                }
                ori = "tl";
            } else if (keys[1][1] == 1) { // If key DOWN is pressed
                if (current.getI() % 2 == 0) {
                    shiftStick(0, 1);
                } else {
                    shiftStick(1, 1);
                }
                ori = "br";
            }
        }
    }
    
    public void shiftStick(int dx, int dy) {
            // Get the position of the stick
            int[] talArr = new int[2];
            talArr[0] = current.getI();
            talArr[1] = current.getJ();
            // Get the supposed new position of the stick
            int[] tal2Arr = new int[2];
            tal2Arr[0] = talArr[0] + dy;
            tal2Arr[1] = talArr[1] + dx;
            Cell c = game.getMap().getCell(tal2Arr);
            //trace(myMap[tal2Arr[0]][tal2Arr[1]][0]);
            /* 
                    Variable: walkable
                    tests if it's allowed to walk on the tile
                    
                    Results:
                            Is true if the tile type is t and 1<= t < 20, and the tile is not occupied.
            */
            // Adds colisions : one does not simply walk into an occupied tale
           
           /* if (isOccupied(tal2Arr)) {
                    walkable = false;
            }*/
            /*
            var n:NPC=isNPC(tal2Arr[0],tal2Arr[1]);
            if(n!=null){
                    if(f != 0){
                            n.interract();
                    }
                    if(n.getWalkable() < 1){
                            walkable = false;
                    }
            }
            */
            //Apply walkable
            if (c!=null && c.isWalkable() == true) {
                    // Move the stick
                    parent = current;
                    current = c;
                    this.setI(current.getI());
                    this.setJ(current.getJ());
            }
            
            game.repaint();
    };

    // Will be used to have to repeat da key pressing
    public void keyLow(int i) {
        keys[i][1] = 0;
    }
}
