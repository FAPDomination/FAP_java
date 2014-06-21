package fap_java;

import animations.AnimLightning;
import animations.Animation;
import animations.AnimWarp;

import characters.Booster;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import java.io.Serializable;
import java.awt.Image;

import java.awt.Point;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import npcs.actions.AAsk;

public abstract class Player extends Human implements Serializable{

    /**
     * The ID number of the player. Not that it is not avoidable, but it's better to have one
     */
    private int id;

    /**
     * The character class of the player.
     * Can be :
     * 0 - Admin (wouldn't work since 0 means not playing)
     * 1 - Knight
     * 2 - Old Magician (not implemented anymore)
     * 3 - Miner
     * 4 - Warlock
     * 5 - Archer
     * 6 - Vampire
     * 7 - No CHaracter
     * 8 - Magician
     * 9 - Booster
     *
     * @see package characters
     */
    private int pc;

    /**
     * The time between two displacements of this player
     */
    private int tmax;

    /**
     * The date of the last displacement
     */
    private int lastDisplacement;

    /**
     * The time between two uses of skill of this player
     */
    private int skillTime;

    /**
     * The date of last skill use
     */
    private int lastSkill;

    /**
     * The orientation (direction of the player)
     */
    protected int ori;

    /**
     * The cell the player is currently standing on
     */
    private Cell current;
    private Cell drawn;

    /**
     * The cell the player was standing on just before
     */
    private Cell parent;

    /**
     * The "force" the player has to kill a cell. This is the amount of HP the cell decreases per frame
     */
    private double decLifeForced;

    /**
     * The game where the player is playin'
     */
    private transient Game game;

    /**
     * The color of the player
     */
    protected Color color;
    
    protected String colorName;

    /**
     * The initial amount of HPs the player's cells have
     */
    private int initHP;

    /**
     * The rate at wich the cell gain HPs after passing the intHP level
     */
    private double gainLife;

    /**
     * The rate at wich the player's cells' HPs decrease when they are alone
     */
    private double decLifeAuto;

    /**
     * The gang the player is in (yo)
     */
    private Team team;

    /**
     * The set of Key the player uses for displacements
     */
    protected int[][] keys = new int[5][2];

    /**
     * Internal parameter
     */
    private String param;

    /**
     * The associated artificial intelligence that controls this player (if any)
     * @see fap_java.FSM
     */
    private FSM fsm;

    private int controler;
    
    private int x = -1,y=-1,wantedX=0,wantedY = 0;
    private int animCount;
    private boolean dispComputed = false;
    private int facDispX=0;
    private int facDispY=0;
    

    /**
     * Initializes a Player. Abstract class since the player creation is called by the characters extending this
     * class.
     * A player will walk through the game to conquer cells, can be controled either by a human or an Artificial
     * Intelligence ( see {@link fap_java.FSM} ).
     * @param id The ID of the soon-to-be-created player
     * @param c The cell on wich he starts
     * @param game The game where he is player
     * @param pc His character class
     * @param t His team
     * @param ai The level of his artificial intelligence, if any
     * @param controler The key set that controls his displacements
     */
    public Player(int id, Cell c, Game game, int pc, Team t, int ai, int controler) {
        super();
        this.id = id;
        current = c;
        drawn = c;
        this.setI(c.getI());
        this.setJ(c.getJ());
        this.game = game;
        this.team = t;
        this.controler = controler;
        team.addPlayer(this);
        // Add FSM if needed
        if (ai > 0) {
            fsm = new FSM(this, ai);
            controler = 1;
        } else {
            fsm = null;
        }

        this.pc = pc;

        team.initConstants();

        color = Params.colorList[id];
        colorName = Params.colorName[id];
        // If no FSM, get keys for displacement
        if (fsm == null) {
            for (int i = 0; i <= 4; i++) {
                keys[i][0] = Params.controlsList[controler][i];
            }
        }
        //Init key pressing
        keys[0][1] = 0;
        keys[1][1] = 0;
        keys[2][1] = 0;
        keys[3][1] = 0;

        // Init the rest
        this.initParams();
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

    /**
     * Sets the value of the designated key to "pressed". This system allows us to handle multi- key-pressing
     * @param i The id of the Key
     */
    public void keyHigh(int i) {
        keys[i][1] = 1;

        //handleKeys();

        if (i == 4) { //Skill
            if (game.getAdv() < 2 && !game.isPauseNPC()) {
                this.getSkill();
            }
            /*else if(game.isPauseNPC() && (game.getThread().getCount() - this.getLastSkill() >= Params.timeForSelection)){
                // Execute actions for the NPC
                // Timer to not mess around
                this.setLastSkill(game.getThread().getCount());
            }*/
            else if (game.getThread().getCount() - this.getLastSkill() >= Params.timeForSelection) {
                // Timer to not mess around
                this.setLastSkill(game.getThread().getCount());
                //Check for NPC
                NPC npc = null;
                if (game.getMap().getFileID() == 0) {
                    npc = Tools.checkNPCOnCell(game, this.current);
                } else {
                    npc = Tools.checkNPCOnCell(game, this.current);
                    if (npc == null) {
                        ArrayList<Cell> toCheck = game.getMap().surroundingCells(current);
                        for (int j = 0; j < toCheck.size(); j++) {
                            Cell c = toCheck.get(j);
                            if (c != null) {
                                npc = Tools.checkNPCOnCell(game, c);
                                if (npc != null) {
                                    break;
                                }
                            }
                        }
                    }
                }

                if(npc == null){
                    // at last, look for auto-trigger ones
                    npc = Tools.checkAutoTriggerNPC(game);
                }
                if (npc != null) {
                    npc.execute();
                }
            }
        }

    }

    /**
     * Sets the value of the designated key to "released"
     * @param i the key ID
     */
    public void keyLow(int i) {
        keys[i][1] = 0;
        //handleKeys();
    }

    /**
     * Computes the decisions for the currently pressed keys, such as displacements
     */
    public void handleKeys() {
        if (game.getThread().getRunning()) {
            //[Key.UP, Key.DOWN, Key.RIGHT, Key.LEFT, Key.END]
            // Hexa displacements :
            // Left + Down
            if (keys[3][1] == 1 && keys[1][1] == 1) {
                ori = 4;
                if (current.getI() % 2 == 0) {
                    shiftStick(-1, 1);
                } else {
                    shiftStick(0, 1);
                }
                
            }
            //Up + Right
            else if (keys[0][1] == 1 && keys[2][1] == 1) {
                ori = 1;
                if (current.getI() % 2 == 0) {
                    shiftStick(0, -1);
                } else {
                    shiftStick(1, -1);
                }
                
            }
            //Up + Left
            else if (keys[0][1] == 1 && keys[3][1] == 1) {
                ori = 0;
                if (current.getI() % 2 == 0) {
                    shiftStick(-1, -1);
                } else {
                    shiftStick(0, -1);
                }
                
            }
            // Down + Right
            else if (keys[1][1] == 1 && keys[2][1] == 1) {
                ori = 3;
                if (current.getI() % 2 == 0) {
                    shiftStick(0, 1);
                } else {
                    shiftStick(1, 1);
                }
               
            }
            //Regular
            // If the key LEFT is pressed
            else if (keys[3][1] == 1) {
                // Update the stick's orientation :
                ori = 5;
                // Move the stick
                shiftStick(-1, 0);
                
            } else if (keys[2][1] == 1) { // If key RIGHT is pressed
                ori = 2;
                shiftStick(1, 0);
                // If the key1 is pressed
                
            } else if (keys[0][1] == 1) { // If key UP is pressed
                // reset the timer for this stick so the player is not able to move for a little while
                // Because of the hexa-grid to movings up and down are complicated
                // find if the stick is on an odd or even number of line
                ori = 0;
                if (current.getI() % 2 == 0) {
                    // Move the stick
                    shiftStick(-1, -1);

                } else {
                    shiftStick(0, -1);
                }
                
            } else if (keys[1][1] == 1) { // If key DOWN is pressed
                ori = 3;
                if (current.getI() % 2 == 0) {
                    shiftStick(0, 1);
                } else {
                    shiftStick(1, 1);
                }
                
            }
        } else if (game.isPauseNPC()) {
            //System.out.println("In D");
            // test if allowed
            if (keys[2][1] == 1 || keys[3][1] == 1) {
                if (game.getThread().getCount() - lastDisplacement >= Params.timeForSelection) {
                    lastDisplacement = game.getThread().getCount();
                    // Find NPC
                    //Check for NPC
                    NPC npc = null;
                    npc = Tools.checkNPCOnCell(game, this.current);
                    if (npc == null) {
                        ArrayList<Cell> toCheck = game.getMap().surroundingCells(current);
                        for (int j = 0; j < toCheck.size(); j++) {
                            Cell c = toCheck.get(j);
                            if (c != null) {
                                npc = Tools.checkNPCOnCell(game, c);
                                if (npc != null) {
                                    break;
                                }
                            }
                        }
                    }
                    if (npc != null && npc.getCurrentAction() instanceof AAsk) {
                        AAsk ac = (AAsk)npc.getCurrentAction();
                        ac.setChoice(!ac.isChoice());
                    }
                }
            }
        }
    }

    /**
     * Moves the player from his cell to another located dx line and dy columns away
     * @param dx The amount of line to be changed
     * @param dy The amount of columns to be shifted
     */
    public void shiftStick(int dx, int dy) {
        // Test if displacement is allowed
        if (game.getThread().getCount() - lastDisplacement >= tmax) {
            lastDisplacement = game.getThread().getCount();
            Cell c;
            if (this.fsm != null) {
                c = fsm.getNextCell();
            } else {
                // Get the supposed new position of the stick
                int[] tal2Arr = new int[2];
                tal2Arr[0] = current.getI() + dy;
                tal2Arr[1] = current.getJ() + dx;
                c = game.getMap().getCell(tal2Arr);
            }
            // Adds colisions : one does not simply walk into an occupied tale
            boolean walkable = c != null && c.isWalkable() == true;
            if (game.isOccupied(c) != null) {
                walkable = false;
            }
            // Special Walk-on-NPC handling
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
            NPC npc = null;
            if (c != null) {
                npc = Tools.checkNPCOnCell(game, c);
                if (npc != null && !npc.isWalkable()) {
                    walkable = false;
                }
            }
            //Apply walkable
            if (walkable) {
                // Move the stick
                parent = current;
                current = c;
                setDrawn(parent);
                this.setI(current.getI());
                this.setJ(current.getJ());
                
                switch(ori){
                    case 2:
                        drawn = current;
                        break;
                    case 3:
                        drawn = current;
                        break;
                case 4:
                    drawn = current;
                    break;
                }

                if (!current.isWalked()) {
                    current.setWalked(true);
                }

                //Check frozen cell :
                if (current.isFrozen() && !(this instanceof Booster)) {
                    int value = (int)((Params.paramTable.get("dispSpeed")[pc]) * Params.frozenFac);
                    this.changeParam("dispSpeed", value, Params.frozenTime);
                }

                int[] tab = new int[2];

                // Check for special tiles
                switch (current.getType()) {
                case 10: // Warp

                    // get the cell to be warped at
                    String[] tabS = new String[2];
                    tabS = c.getAddParam().split(",");
                    tab[0] = Integer.parseInt(tabS[0]);
                    tab[1] = Integer.parseInt(tabS[1]);

                    Cell wantedCell = game.getMap().getCell(tab);
                    if (wantedCell != null) {
                        // If no one stand on it
                        if (game.isOccupied(wantedCell) == null) {
                            // Warp the player to it
                            current = wantedCell;
                            this.setI(current.getI());
                            this.setJ(current.getJ());
                            current.activateCell(this);
                            // add a little animation :p
                            Animation anim = new AnimWarp(current, game.getThread());
                        } else {
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
                case 11: // Switch
                    // Get the switch parameters
                    String[] tabSw = new String[3];
                    tabSw = c.getAddParam().split(",", 3);
                    tab[0] = Integer.parseInt(tabSw[0]);
                    tab[1] = Integer.parseInt(tabSw[1]);

                    // The cell to be switched
                    Cell switchedCell = game.getMap().getCell(tab);
                    String code = tabSw[2];

                    String[] tabNewCell = code.split(",", 2);
                    param = "";
                    int did = Integer.parseInt(tabNewCell[0]);
                    if (tabNewCell.length > 1) {
                        param = tabNewCell[1];
                    }
                    //System.out.println(did);

                    // Compute new type and properties
                    int t = MapHandler.setTypeWithDid(did, param);
                    switchedCell.setAddParam(param);
                    switchedCell.setDid(did);

                    switchedCell.setType(t);

                    break;
                case 12: // Exit NPC
                    break;
                }
            }
            if (npc != null && game.getMap().getFileID() != 0) {
                //if(npc != null){
                npc.execute();
            }
        }
    };

    /**
     * Paints the player (not really implemented yet)
     * @param g Graphical thing
     */
    public void paintComponent(Graphics g) {
        // Smooth displacement
        
        if((wantedX != x || wantedY !=y) && !dispComputed){
            dispComputed = true;
            facDispX = -(x-wantedX)/Params.displacementAnimationLength;
            facDispY = -(y-wantedY)/Params.displacementAnimationLength;
            int limit = 10;
            if(facDispX > limit ||facDispY > limit){
                x = wantedX;
                y = wantedY;
            }
            animCount = Params.displacementAnimationLength;
        }
        
        if(x == -1 || Math.abs(wantedX - x) < 3 ||animCount <= 0){
            x = CMap.giveTalePosition(this.getI(), this.getJ())[0] + Params.OFFX;
            wantedX = x;
        }
        else if(wantedX != x){
                x+=facDispX;
        }
        if(y == -1 ||  Math.abs(wantedY - y) < 3 ||animCount <= 0){
            y = CMap.giveTalePosition(this.getI(), this.getJ())[1] + Params.OFFY;
            wantedY = y;
        }
        else if(wantedY != y){
                y+=facDispY;
        }
        
        if(wantedX == x && wantedY == y ||animCount <= 0){
            drawn = current;
            dispComputed = false;
        }
        
        if(animCount > 0){
            animCount --;
        }
        
        // Comment these three lines and uncomment the rest to have smooth displacement
        /*
        drawn = current;
        int x = CMap.giveTalePosition(this.getI(), this.getJ())[0] + Params.OFFX;
        int y = CMap.giveTalePosition(this.getI(), this.getJ())[1] + Params.OFFY;
        */
        paintStick(g, x, y);
    }

    /**
     * Paints the player (not really implemented yet)
     * @param g Graphical thing
     * @param x The x axis position
     * @param y The y axis position
     */
    public abstract void paintStick(Graphics g, int x, int y);

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

    /**
     * Gets the player to go back at his previous tile
     */
    public void kickBack() {
        //Note : you can't double kickback
        if (game.isOccupied(parent) == null) {
            current = parent;
            this.setI(current.getI());
            this.setJ(current.getJ());
            current.activateCell(this);
        }
    }

    public abstract void getSkill();

    public void setSkillTime(int skillTime) {
        this.skillTime = skillTime;
    }

    public int getSkillTime() {
        return skillTime;
    }

    public String toString() {
        return "Player "+this.getColorName()+" no " + id + " at " + this.getI() + "," + this.getJ();
    }

    /**
     * Blasts a designated number of cells of the player's team. It just destroys a specific number of random cells
     * that the player owned
     * @param numberOfCells
     */
    public void blast(int numberOfCells) {
        // Get the list of the array he owns
        ArrayList<Cell> map = game.getMap().getMyMap();
        ArrayList<Cell> owned = new ArrayList<Cell>();
        for (int i = 0; i < map.size(); i++) {
            Cell c = map.get(i);
            if (c.getOwner() == this.team) {
                owned.add(c);
            }
        }

        if(owned.size()>0){
            //Blast
            for (int i = 0; i < numberOfCells; i++) {
                //Pick random cell
                int rand = Tools.randRange(0, owned.size() - 1);
                Cell randCell = owned.get(rand);
                //KILL IT WITH FIRE !
                randCell.setOwner(null);
                randCell.setHp(0);
                owned.remove(randCell);
                int cx = CMap.giveTalePosition(randCell.getI(), randCell.getJ())[0] + Params.OFFX;
                int cy = CMap.giveTalePosition(randCell.getI(), randCell.getJ())[1] + Params.OFFY;
                Animation lightning = new AnimLightning(cx,cy,this.getGame().getThread());
            }
        }
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    /**
     * Make the player wait for "delay" m-seconds
     */
    public void makeHimWait(int delay) {
        //Modify date of last displacement into the FUTCHA
        this.setLastDisplacement(this.getGame().getThread().getCount() + delay);
    }

    /**
     * Initializes the parameters for the player. They are gotten from the ParamTable XML
     * @see fap_java.ParamTableHandler
     */
    public void initParams() {
        tmax = (int)(game.getThread().getDelay() * Params.paramTable.get("dispSpeed")[pc]);
        if(game.getAdv() == 2){
            tmax/=3;
        }
        //System.out.println(tmax);
        initHP = 100;
        //maxHP = (int)Params.paramTable.get("maxHP")[pc];
        decLifeForced = Params.paramTable.get("decLifeForced")[pc];
        
        gainLife = 0.01;
        decLifeAuto = 1;
        lastDisplacement = 0;
        lastSkill = 0;
        ori = 3;

        this.setSkillTime((int)(Params.paramTable.get("skillTime")[pc] * 1000));
    }

    public void setOri(int ori) {
        this.ori = ori;
    }

    public int getOri() {
        return ori;
    }

    /**
     * Changes a value of a parameter (displacment speed, healing rate,...) for a specific amount of time
     * @param wich Wich parameter to be changed
     * @param newValue The ne value of the parameter
     * @param time The duration of the modification in ms
     */
    public void changeParam(String wich, double newValue, int time) {
        //TODO escape the double switch
        //TODO pass values to the timer without parameters
        Timer timer = new Timer();
        this.param = wich;
        boolean go = true;
        if (param.equals("dispSpeed")) {
            tmax = (int)(game.getThread().getDelay() * newValue);
        } else if (param.equals("maxHP")) {
            team.setMaxHP((int)newValue);
        } else if (param.equals("decLifeForced")) {
            decLifeForced = newValue;
        } else if (param.equals("recovLifeAuto")) {
            team.setRecovLifeAuto(newValue);
        } else if (param.equals("skillTime")) {
            this.setSkillTime((int)(newValue * 1000));
        } else {
            go = false;
        }
        if (go) {
            // Create a timer that will clock-tick in "time" ms
            timer.schedule(new TimerTask() {
                    public void run() {
                        if (param.equals("dispSpeed")) {
                            tmax = (int)(game.getThread().getDelay() * Params.paramTable.get("dispSpeed")[pc]);
                        } else if (param.equals("maxHP")) {
                            team.initConstants();
                        } else if (param.equals("decLifeForced")) {
                            decLifeForced = Params.paramTable.get("decLifeForced")[pc];
                        } else if (param.equals("recovLifeAuto")) {
                            team.initConstants();
                        } else if (param.equals("skillTime")) {
                            setSkillTime((int)(Params.paramTable.get("skillTime")[pc] * 1000));
                        }
                    }
                }, time);
        }
    }

    public void setFsm(FSM fsm) {
        this.fsm = fsm;
    }

    public FSM getFsm() {
        return fsm;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }

    public int getPc() {
        return pc;
    }

    public void setControler(int controler) {
        this.controler = controler;
    }

    public int getControler() {
        return controler;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getColorName() {
        return Params.colorName[id];
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setWantedX(int diffX) {
        this.wantedX = diffX;
    }

    public int getWantedX() {
        return wantedX;
    }

    public void setDrawn(Cell drawn) {
        this.drawn = drawn;
        wantedX = CMap.giveTalePosition(current.getI(), current.getJ())[0] + Params.OFFX;
        wantedY = CMap.giveTalePosition(current.getI(), current.getJ())[1] + Params.OFFY;
    }

    public Cell getDrawn() {
        return drawn;
    }
}
