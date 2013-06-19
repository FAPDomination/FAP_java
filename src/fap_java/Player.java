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
    private double decLifeForced;
    private double recovLifeAuto;
    private Game game;
    private Color color;
    private int initHP;
    private int maxHP;
    private double gainLife;
    private double decLifeAuto;
    //Keys
    private int[][] keys = new int[5][2];

    public Player(int id, int[] coord, Game game) {
        super();
        this.id = id;
        this.setI(coord[0]);
        this.setJ(coord[1]);
        this.game = game;
        
        color = Color.RED;
        if(id == 2){
            color = Color.BLUE;
        }
        
        current = game.getMap().getCell(coord);
        // 38 40 39 37 : arrow keys
        if(id == 1){
        keys[0][0] = 38;
        keys[1][0] = 40;
        keys[2][0] = 39;
        keys[3][0] = 37;
        }
        else{
            keys[0][0] = 90;
            keys[1][0] = 83;
            keys[2][0] = 68;
            keys[3][0] = 81; 
        }
        keys[0][1] = 0;
        keys[1][1] = 0;
        keys[2][1] = 0;
        keys[3][1] = 0;
        
        tmax = game.getThread().getDelay()*10;
        
        initHP = 100;
        maxHP = 130;
        decLifeForced = 5;
        recovLifeAuto = 0.2;
        gainLife = 0.01;
        decLifeAuto = 1;
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
        if (game.getThread().getCount() - lastDisplacement >= tmax) {
            lastDisplacement = game.getThread().getCount();

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
           boolean walkable = c!=null && c.isWalkable() == true;
            if (game.isOccupied(c) !=null) {
                    walkable = false;
            }
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
            if (walkable) {
                    // Move the stick
                    parent = current;
                    current = c;
                    this.setI(current.getI());
                    this.setJ(current.getJ());
                    current.activateCell(this);
                    
                    switch(current.getType()){
                    case 10:        // Warp
                        int[] tab = new int[2];
                        String[] tabS = new String[2];
                        tabS = c.getAddParam().split(",");
                        tab[0] = Integer.parseInt(tabS[0]);
                        tab[1] = Integer.parseInt(tabS[1]);
                        
                        Cell wantedCell = game.getMap().getCell(tab);
                        if(wantedCell != null){
                            if(game.isOccupied(wantedCell)==null){
                                   current = wantedCell;
                                    this.setI(current.getI());
                                    this.setJ(current.getJ());
                                    current.activateCell(this);
                                    // add a little animation :p
                            }
                            else{
                                //Restore parent ?
                                /*
                                    var pPos:Array = stick.prevTale;
                                    var nPos:Array = giveTalePosition(pPos[0], pPos[1]);
                                    stick._x = nPos[0]+offx;
                                    stick._y = nPos[1]+offy;
                                */
                            }
                        }
                        break;
                    case 11:        // Switch
                        break;
                    case 12:        // Exit NPC
                        break;
                    }
            }
            
            //Test  trap Cell
            //-----
            
            game.repaint();
    };

    // Will be used to have to repeat da key pressing and for H-Displacement
    public void keyLow(int i) {
        keys[i][1] = 0;
    }
    
    public void paintComponent(Graphics g) {
        int x = CMap.giveTalePosition(this.getI(), this.getJ())[0] + Params.OFFX;
        int y = CMap.giveTalePosition(this.getI(), this.getJ())[1] + Params.OFFY;
        
        g.setColor(color);
        g.drawRect(x, y, 10, 30);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setInitHP(int initHP) {
        this.initHP = initHP;
    }

    public int getInitHP() {
        return initHP;
    }

    public void setDecLifeForced(double decLifeForced) {
        this.decLifeForced = decLifeForced;
    }

    public double getDecLifeForced() {
        return decLifeForced;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public void setRecovLifeAuto(double recovLifeAuto) {
        this.recovLifeAuto = recovLifeAuto;
    }

    public double getRecovLifeAuto() {
        return recovLifeAuto;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setGainLife(double gainLife) {
        this.gainLife = gainLife;
    }

    public double getGainLife() {
        return gainLife;
    }

    public void setDecLifeAuto(double decLifeAuto) {
        this.decLifeAuto = decLifeAuto;
    }

    public double getDecLifeAuto() {
        return decLifeAuto;
    }
    
    public void kickBack(){
        //Note : you can't double kickback
        if(game.isOccupied(parent) == null){
            current = parent;
            this.setI(current.getI());
            this.setJ(current.getJ());
            current.activateCell(this);
        }
    }
}
