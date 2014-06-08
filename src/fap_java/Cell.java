package fap_java;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import java.awt.image.BufferedImage;

import java.util.ArrayList;

public class Cell extends Element {
    /**
     * The Design ID of the cell. Is used to call the right image to display (1 is normal, 2 is dirt, 3 rock, etc..)
     */
    private int did;
    /**
     * The image that will be painted as a representation of the cell
     */
    private Image img;
    /**
     * The Type of the cell.
     * Several types exist :
     * 1 - normal, wakable, takable
     * 2 - CountDown Cell
     * 3 - Neutral that can't be owned (not implemented yet)
     * 10 - warp
     * 11 - switch
     * 12 - NPC exit
     * 19 - blocking low (river)
     * 20 - blocking high (rock)
     */
    private int type;
    /**
     * The team that owns this cell
     */
    private Team owner;
    /**
     * The property walkable or not of the cell
     */
    private boolean walkable;
    /**
     * If the cell has ever been walked on (useful to test if a switch was activated)
     */
    private boolean walked;
    /**
     * The number of HealthPoints of the cell
     */
    private double hp;
    /**
     * The property of the cell to be high blocking or low blocking
     */
    private boolean height;
    /**
     * The map in wich the cell lives
     */
    private CMap map;
    /**
     * An additional parameter, such as the cell where a warp will teleport you
     */
    private String addParam;

    //Special params
    /**
     * If the cell is a trap, who traped it
     */
    private Team trap;
    /**
     * If the cell is from the Healthy Healthy special tile
     */
    private boolean healthy;
    /**
     * If the cell is unstable : hp will decrease no matter what
     */
    private boolean unstable;
    /**
     * If the cell is frozen : will slow down the player
    */
    private boolean frozen;

    /**
     * If the cell is currently being examinated by a miner who wants to teleport
     */
    private Player minerSelect;
    /**
     * If the cursor of the miner is on this cell
     */
    private boolean minerSing;

    /**
     * The game in wich the cell is living
     */
    private Game game;

    /**
     * cf Cell(int i, int j, int type, String param, int did)
     */
    public Cell(int i, int j, int type, int did, Game game) {
        this(i, j, type, "", did, game);
    }
    
    public Cell(int i, int j){
        this(i, j, 0, "", 0, null);
    }

    /**
     * A Cell object is an element of the map (grass, water, rock, ...).
     * Can be referred to as "tile" or even miswritten as "tale"
     * @param i : the line index of the cell
     * @param j : the column index of the cell
     * @param type : the type of the cell (takable, blocking, warp, ...)
     * @param param : An additional param, such as a property (frozen, unstable, healthy)
     * @param did : the design of the cell (grass, rock, water, lava,...)
     */
    public Cell(int i, int j, int type, String param, int did, Game game) {
        super();
        this.game = game;
        this.setI(i);
        this.setJ(j);
        this.walkable = true;
        this.height = false;
        this.hp = 0;
        this.addParam = param;
        this.did = did;
        this.setType(type);
        this.trap = null;
        this.owner = null;
        this.minerSing = false;
        this.img = Graph.cells.get(did);
        if (img == null) {
            System.out.println("Null image for this did : " + did);
        }
        walked = false;

    }

    /**
     * Allows to paint de Cell in order to have a complete map drawn
     * @param g
     */
    public void paintComponent(Graphics g) {
        int[] thisTilePosition = CMap.giveTalePosition(this.getI(), this.getJ());
        int x = thisTilePosition[0];
        int y = thisTilePosition[1];
        
        // Game paint factor
        double paintFactorW = Graph.facW;
        double paintFactorH = Graph.facH;

        int width;
        int height;
        int offX;
        int offY;
        // Paint dirt
        //test if needed
        ArrayList<Cell> surrounding = game.getMap().surroundingCells(this);
        if(surrounding.get(3) == null || surrounding.get(4) == null){
            BufferedImage dirtImage = Graph.cells.get(0);
            offX = (int)Graph.offsetsCells.get(0).getWidth();
            offY = (int)Graph.offsetsCells.get(0).getHeight();
            width = (int)(dirtImage.getWidth(game) * paintFactorW);
            height = (int)(dirtImage.getHeight(game) * paintFactorH);

            g.drawImage(dirtImage, x + offX, y + offY, width, height, game);
        }
        // Paint did
        width = (int)(this.img.getWidth(game) * paintFactorW);
        height = (int)(this.img.getHeight(game) * paintFactorH);
        offX = (int)Graph.offsetsCells.get(did).getWidth();
        offY = (int)Graph.offsetsCells.get(did).getHeight();
        
        g.drawImage(this.img, x + offX, y + offY, width, height, game);
        
        
        // Special case if the miner is currently selecting the cell
        //TODO better painting of miner selecting depending on player's color
        if (minerSelect != null) {
            int minerSlectID = 13;
            width = (int)(Graph.cells.get(minerSlectID).getWidth(game) * paintFactorW);
            height = (int)(Graph.cells.get(minerSlectID).getHeight(game) * paintFactorH);
            offX = (int)Graph.offsetsCells.get(minerSlectID).getWidth();
            offY = (int)Graph.offsetsCells.get(minerSlectID).getHeight();
            //If the miner's cursor is on the cell
            //TODO better painting of minor's cursor
            if(this.minerSing){
                g.drawImage(Graph.cells.get(minerSlectID+1), x + offX, y + offY, width, height, game);
            }
            else{
                g.drawImage(Graph.cells.get(minerSlectID), x + offX, y + offY, width, height, game);
            }
        }
        
        // Write the amount of HP of the tile
        if (hp > 0) {
            String hps = "" + (int)hp;
            Graphics2D g2d = (Graphics2D)g;
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(hps);
            
            //TODO coloured background
            if(owner !=null){
                g.setColor(owner.getColor());
            }
            else{
                g.setColor(Color.black);
            }
            
            g.setColor(Color.WHITE);
            g.drawString(hps, x + (CMap.TW-textWidth)/2 -1, y + 10 - 1);
            g.drawString(hps, x + (CMap.TW-textWidth)/2 -1, y + 10 + 1);
            g.drawString(hps, x + (CMap.TW-textWidth)/2 +1, y + 10 - 1);
            g.drawString(hps, x + (CMap.TW-textWidth)/2 +1, y + 10 + 1);
            g.setColor(Color.black);
            g.drawString(hps, x + (CMap.TW-textWidth)/2, y + 10);
            
        }
        
        //TODO Special Healthy Healthy design
    }

    /**
     * Allows a player to conquer a cell. Check for HPs and update them if needed
     * Also activate traps if the cell was trapped by a warlock
     * @param p : the player
     */
    public void activateCell(Player p) {
        // Check if the tile is takable and the property of the player
        if (owner != p.getTeam() && type == 1) {
            // if not, tests if the tale has HP
            if (hp <= 0) {
                // The tale is empty, sets it as the property of the player, gives HP and draw the according map

                //If the tile became unwakable, the player is sent back to his previous cell
                if (walkable == false) {
                    p.kickBack();
                } else {
                    // The tile becomes his property
                    owner = p.getTeam();
                    hp = p.getInitHP();
                }
            } else {
                // Else forces the healthpoints of the tale to decrease (Attack)
                hp -= p.getDecLifeForced();
            }
        }

        // Activate the trap system if needed
        if (trap != null) {
            if (trap != p.getTeam()) {
                // Blast that guy
                p.blast(Params.nBlastedTiles);
                trap = null;
                // Add animation
            } else { // Disable the bomb if the warlock walks onto his own trap-cell
                if (p.getGame().getThread().getCount() - p.getLastSkill() >= p.getSkillTime()) {
                    trap = null;
                    p.setLastSkill(p.getGame().getThread().getCount());
                    //Add animation
                }
            }
        }
    }

    /**
     * Recalculates the HP of a Cell, according to Conway's laws or other factors
     * @param game : the game where the cell is
     */
    
    public void refreshHealthPoints(Game game){
        //trace(_root.kco);
                // Counts the neighbours of the same type of the tale
                //Note : MyDMap != 8 is for lava floor and unstable cells
                //var recovB:Boolean = myDMap[vi][vj] != 8 && healthPoints[i][0] !=1 && countNeighbours(myMap, vi, vj, healthPoints[i][0])>=nNeighboursConwell;
        if(owner != null){
            boolean recovB = (type == 1) && owner != null && map.countNeighbours(this)>=Params.nNeighboursConway && !unstable;
                if (recovB) {
                        // If the cell is wounded (under initHP HPs)
                        if (hp<owner.getFirstPlayer().getInitHP()) {
                                        // The HP will recover slowly up to initHP
                                        hp += owner.getRecovLifeAuto();
                                // between initHP and maxHP
                        } else if (hp<owner.getMaxHP() || (hp<Params.higherMaxHP && healthy)) {
                                //_root["t"+i].onEnterFrame = function() {
                                        // The HP will very slowly increase up to the max limit
                                        double gainLifeFactor;
                                        if(healthy){
                                                gainLifeFactor = Params.gainLifeFactorMultiplier;
                                        }
                                        else{
                                                gainLifeFactor = 1;
                                        }
                                        hp += owner.getFirstPlayer().getGainLife()*gainLifeFactor;
                                //};
                        } else {
                                // If the tale isn't lonely or anything, do nothing
                                //delete _root["t"+i].onEnterFrame;
                        }
            } else {
                /* Only enabled when "GameOfLife" level 1 or more is on :
                        the goal here is to decrease the HP of the tale because it's alone.
                        Cells need to be in groups to survive
                        */
                if (Params.gameOfLife == true && type != 2) {
                    // The HP will decrease until the cell is dead OR not alone anymore
                    hp -= owner.getFirstPlayer().getDecLifeAuto();
                }
            }
        }
        // Testing if the tale is a neutral tale with HP (Countdown Cell) :
        if (type == 2) {
            // The HP will decrease until the cell is dead
            hp -= Params.decLifeCountDown;
        }
        // If a cell dies, it goes back to normal un-owned tale
        if (hp <= 0) {
            // if it's a timer-cell, set it to blocking tile
            if (type == 2) {
                type = 20;
                this.setDid(100);
                walkable = false;
                this.height = true;
                // KICK BACK
                Player p = game.isOccupied(this);
                if (p != null) {
                    p.kickBack();
                }
                //Change did
            } else {
                //else it goes back to neutral
            }
            // set the changes in the different variables
            hp = 0;
            owner = null;
        }
    }

    // ----------- Accessors

    // --- Getters

    public boolean isWalkable() {
        return walkable;
    }

    public Team getOwner() {
        return owner;
    }

    public int getType() {
        return type;
    }

    public String getAddParam() {
        return addParam;
    }

    public CMap getMap() {
        return map;
    }

    public int getDid() {
        return did;
    }

    public double getHp() {
        return hp;
    }

    public Player getMinerSelect() {
        return minerSelect;
    }

    public boolean isMinerSing() {
        return minerSing;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public boolean isHeight() {
        return height;
    }

    public Team getTrap() {
        return trap;
    }

    public boolean isHealthy() {
        return healthy;
    }

    public boolean isUnstable() {
        return unstable;
    }

    // --- Setters

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public void setOwner(Team owner) {
        this.owner = owner;
    }

    /**
     * Computes the new type and properties according to several factors
     * @param type
     */
    public void setType(int type) {
        
        this.type = type;
        
        //Blocking high
        if (type == 20) {
            walkable = false;
            this.height = true;
        }
        //Blocking low
        else if (type == 19) {
            //this.type = 1;
            walkable = false;
        }
        //Countdown cell
        else if (type == 2) {
            hp = Integer.parseInt(addParam);
        }
        
        if(type != 20 && type != 19){
            walkable = true;
        }
        if(type != 20){
            height = false;
        }

        //Healthy Healthy and special tiles
        if (type == 1 && addParam.matches("[a-z]")) {
            // Healthy
            if (addParam.charAt(0) == 'h') {
                healthy = true;
            }
            // Unstable (lava)
            else if (addParam.charAt(0) == 'u') {
                unstable = true;
            }
            // Frozen (slows the player down)
            else if (addParam.charAt(0) == 'f') {
                frozen = true;
            }
        }
    }

    public void setAddParam(String addParam) {
        this.addParam = addParam;
    }

    public void setMap(CMap map) {
        this.map = map;
    }

    public void setDid(int did) {
        this.did = did;
        this.img = Graph.cells.get(did);
    }

    public void setHp(double hp) {
        this.hp = hp;
    }

    public void setMinerSing(boolean minerSing) {
        this.minerSing = minerSing;
    }

    public void setMinerSelect(Player minerSelect) {
        this.minerSelect = minerSelect;
    }

    public void setTrap(Team trap) {
        this.trap = trap;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }

    public void setUnstable(boolean unstable) {
        this.unstable = unstable;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public void setHeight(boolean height) {
        this.height = height;
    }

    // ----- End Accessors

    // -------- Useful

    /**
     * Get information about the cell, such as position (I,J), owner, and wakable property.
     * @return a string with information
     */
    public String toString() {
        String s = "Cell at " + this.getI() + "," + this.getJ();
        if (this.owner != null) {
            s += ", owned by " + owner;
        }
        if (walkable) {
            s += ", walkable.";
        }
        return s;
    }

    /**
     * Check if the cell is equal to another (ie on the same spot)
     * @param c : the cell to be compared
     * @return : boolean value
     */
    public boolean equals(Cell c) {
        boolean b = false;
        // Check if they are on the same spot
        if (this.getI() == c.getI() && this.getJ() == c.getJ()) {
            b = true;
        }
        return b;
    }

    public void setWalked(boolean walked) {
        this.walked = walked;
    }

    public boolean isWalked() {
        return walked;
    }
}
