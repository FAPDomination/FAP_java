package fap_java;

import java.awt.Graphics;
import java.awt.Image;

public class NPC extends Human{
    
    private Cell position;
    private boolean walkable;
    private boolean autoTrigger;
    private Image img;
    private boolean running;
    private Game game;
    
    public NPC(Cell position, boolean walkable, boolean autoTrigger, Image img, Game game) {
        this.position = position;
        this.walkable = walkable;
        this.autoTrigger = autoTrigger;
        this.img = img;
        this.game = game;
    }

    public String toString() {
        //return "NPC at "+this.getI()+","+this.getJ();
        return "A NPC";
    }

    public void paintComponent(Graphics g) {
    }
    
    public void execute(){
        game.pauseGame();
        System.out.println(this+" is executing");
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
}
