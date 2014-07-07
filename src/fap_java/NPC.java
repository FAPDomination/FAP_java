package fap_java;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import java.io.Serializable;

import java.util.ArrayList;

import npcs.actions.*;

public class NPC extends Human implements Serializable {

    @SuppressWarnings("compatibility:8918283573792917910")
    private static final long serialVersionUID = -2618102038212575413L;
    
    private transient Cell position;
    private String cellPositionHash;
    private boolean walkable;
    private boolean autoTrigger;
    protected String img;
    protected transient Game game;
    protected int x;
    protected int y;
    protected int offX,offY;

    
    protected Action firstAction;
    protected Action currentAction;
    protected boolean executing;


    public NPC(String cellPositionHash, boolean walkable, boolean autoTrigger, String img, int offX, int offY,
               Action firstAction) {
        this.cellPositionHash = cellPositionHash;
        this.walkable = walkable;
        this.autoTrigger = autoTrigger;
        this.img = img;
        this.offX = offX;
        this.offY = offY;
        
        this.firstAction = firstAction;
        this.currentAction = firstAction;
        this.reInit();
    }
    public NPC(String cellPositionHash, boolean walkable, boolean autoTrigger, String img, Dimension offsets, Action firstAction) {
        this(cellPositionHash,walkable,autoTrigger,img,(int)offsets.getWidth(),(int)offsets.getHeight(),firstAction);
    }
    
    /**
     * Creates an auto-trigger NPC
     */
    public NPC(Action firstAction) {
        this("", false, true, null, 0, 0, firstAction);
    }


    public void paintComponent(Graphics g) {
        if (img != null) {
            Image bild = Graph.getList().get(img);
            int width = bild.getWidth(game);
            int height = bild.getHeight(game);

            g.drawImage(bild, x, y, width, height, game);
        }
    }

    public String toString() {
        //return "NPC at "+this.getI()+","+this.getJ();
        return "A NPC at " + position;
    }
/*
    public Action getCurrentAction() {
        Action ac = null;
        if (iterator >= 0 && iterator < actions.size()) {
            ac = actions.get(iterator);
        }
        return ac;
    }
*/

    public void execute() {
        this.executing = true;
        if (game.getThread().getRunning()) {
            game.pauseGame(true);
        }
        if (currentAction != null) {
            currentAction.execute(this);
            //System.out.println(this+" is executing");
        } else {
            this.reInit();
            //TODO ?
            game.pauseGame(true);
            game.setPauseNPC(false);
        }

    }

    public void setPosition(Cell position) {
        this.position = position;
    }

    public Cell getPosition() {
        return position;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public void setAutoTrigger(boolean autoTrigger) {
        this.autoTrigger = autoTrigger;
    }

    public boolean isAutoTrigger() {
        return autoTrigger;
    }


    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public void reInit() {
            for (Action a = firstAction; a!=null; a=a.getNext()) {
                if (a != null) {
                    a.reinit();
                }
            }
        this.executing = false;
        currentAction = firstAction;
    }


    public boolean equals(NPC e) {
        boolean b = false;
        // Check if they are on the same spot
        if (this.position == e.getPosition() && !this.autoTrigger) {
            b = true;
        }
        return b;
    }

    public void setCurrentAction(Action currentAction) {
        this.currentAction = currentAction;
    }

    public Action getCurrentAction() {
        return currentAction;
    }
    
    public void gotoNextAction(){
        currentAction=currentAction.getNext();
    }
    
    public void setNextAction(Action a){
        currentAction.setNext(a);
    }
    
    public void setTransientValues(Game game){
        this.game = game;
        if(cellPositionHash != null && !cellPositionHash.equals("")){
            position = game.getMap().getCell(cellPositionHash);
            if (position != null) {
                int[] tableXY = CMap.giveTalePosition(position);
                x = tableXY[0] + offX;
                y = tableXY[1] + offY;
            }
        }
        
        for (Action a = firstAction; a!=null; a=a.getNext()) {
            if (a != null) {
                a.setTransientValues(game);
            }
        }
    }

    public void setExecuting(boolean executing) {
        this.executing = executing;
    }

    public boolean isExecuting() {
        return executing;
    }
}
