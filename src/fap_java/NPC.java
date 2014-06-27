package fap_java;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import java.util.ArrayList;

import npcs.actions.*;

public class NPC extends Human {

    private Cell position;
    private boolean walkable;
    private boolean autoTrigger;
    protected Image img;
    private boolean running;
    protected Game game;
    protected int x;
    protected int y;

    protected int iterator;
    
    protected Action firstAction;
    protected Action currentAction;


    public NPC(Cell position, boolean walkable, boolean autoTrigger, Image img, Game game, int offX, int offY,
               Action firstAction) {
        this.position = position;
        this.walkable = walkable;
        this.autoTrigger = autoTrigger;
        this.img = img;
        this.game = game;
        if (position != null) {
            int[] tableXY = CMap.giveTalePosition(position);
            x = tableXY[0] + offX;
            y = tableXY[1] + offY;
        }
        this.firstAction = firstAction;
        this.currentAction = firstAction;
        this.reInit();
    }

    public NPC(Cell position, boolean walkable, boolean autoTrigger, Image img, Game game, Dimension offsets,
               Action firstAction) {
        this(position, walkable, autoTrigger, img, game, (int)offsets.getWidth(), (int)offsets.getHeight(), firstAction);
    }

    /**
     * Creates an auto-trigger NPC
     */
    public NPC(Action firstAction, Game game) {
        this(null, false, true, null, game, 0, 0, firstAction);
    }

    public void paintComponent(Graphics g) {
        if (img != null) {
            int width = this.img.getWidth(game);
            int height = this.img.getHeight(game);

            g.drawImage(this.img, x, y, width, height, game);
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
        if (game.getThread().getRunning()) {
            game.pauseGame(true);
        }
        if (currentAction.getNext() != null) {
            currentAction = currentAction.getNext();
            iterator++;
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

    public void setImg(Image img) {
        this.img = img;
    }

    public Image getImg() {
        return img;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public void reInit() {
        iterator = 0;
            for (Action a = firstAction; a!=null; a=a.getNext()) {
                if (a != null) {
                    a.reinit();
                }
            }
    }

    public void setIterator(int iterator) {
        this.iterator = iterator;
    }

    public int getIterator() {
        return iterator;
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
}
