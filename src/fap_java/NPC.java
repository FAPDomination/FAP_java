package fap_java;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import java.io.Serializable;

import java.util.ArrayList;

import npcs.actions.*;

public class NPC extends Human implements Serializable{

    private Cell position;
    private boolean walkable;
    private boolean autoTrigger;
    protected Image img;
    private boolean running;
    protected Game game;
    protected int x;
    protected int y;

    protected int iterator;

    protected ArrayList<Action> actions;

    public NPC(Cell position, boolean walkable, boolean autoTrigger, Image img, Game game, int offX, int offY,
               ArrayList<Action> actions) {
        this.position = position;
        this.walkable = walkable;
        this.autoTrigger = autoTrigger;
        this.img = img;
        this.game = game;
        this.actions = actions;
        if (position != null) {
            int[] tableXY = CMap.giveTalePosition(position);
            x = tableXY[0] + offX;
            y = tableXY[1] + offY;
        }
        this.reInit();
    }

    public NPC(Cell position, boolean walkable, boolean autoTrigger, Image img, Game game, Dimension offsets,
               ArrayList<Action> actions) {
        this(position, walkable, autoTrigger, img, game, (int)offsets.getWidth(), (int)offsets.getHeight(), actions);
    }

    /**
     * Creates an auto-trigger NPC
     * @param actions The list of actions
     */
    public NPC(ArrayList<Action> actions, Game game) {
        this(null, false, true, null, game, 0, 0, actions);
    }

    public void paintComponent(Graphics g) {
        if (img != null) {
            int width = this.img.getWidth(game.getDisplayer());
            int height = this.img.getHeight(game.getDisplayer());

            g.drawImage(this.img, x, y, width, height, game.getDisplayer());
        }
    }

    public String toString() {
        //return "NPC at "+this.getI()+","+this.getJ();
        return "A NPC at " + position;
    }

    public Action getCurrentAction() {
        Action ac = null;
        if (iterator >= 0 && iterator < actions.size()) {
            ac = actions.get(iterator);
        }
        return ac;
    }


    public void execute() {
        if (game.getThread().getRunning()) {
            game.pauseGame(true);
        }
        if (iterator < actions.size()) {
            Action ac = actions.get(iterator);
            iterator++;
            ac.execute(this);
            //System.out.println(this+" is executing");
        } else {
            this.reInit();
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
        if (actions != null) {
            for (int i = 0; i < actions.size(); i++) {
                Action ac = actions.get(i);
                if (ac != null) {
                    ac.reinit();
                }
            }
        }
    }

    public void setIterator(int iterator) {
        this.iterator = iterator;
    }

    public int getIterator() {
        return iterator;
    }

    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public boolean equals(NPC e) {
        boolean b = false;
        // Check if they are on the same spot
        if (this.position == e.getPosition() && !this.autoTrigger) {
            b = true;
        }
        return b;
    }
}
