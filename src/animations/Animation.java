package animations;

import fap_java.Element;
import fap_java.TheThread;

import java.awt.Graphics;

import java.io.Serializable;

public abstract class Animation implements Serializable{
    
    protected int x;
    protected int y;
    protected int duration; // in ms
    protected int position; // in ms
    protected TheThread thread;
    protected boolean running;
    
    public Animation(int x, int y, int duration, TheThread thread){
        this.x = x;
        this.y = y;
        
        this.duration = duration;
        this.position = 0;
        
        this.thread = thread;
        thread.getMyGame().addAnim(this);
        running = true;
    }
    
    public abstract String toString();
    
    public abstract void paintComponent(Graphics g);

    public void executeAnimation(){
        position+=thread.getDelay();
        if(position>=duration){
            endAnimation();
        }
    }
    
    public void endAnimation(){
        running = false;
        thread.getMyGame().deleteAnim(this);
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }
}
