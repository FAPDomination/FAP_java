package animations;

import java.awt.Graphics;

public abstract class Animation {
    
    protected int x;
    protected int y;
    protected int nFrames;
    
    public Animation(int x, int y, int nFrames){
        this.x = x;
        this.y = y;
        
        this.nFrames = nFrames;
    }
    
    public abstract String toString();
    
    public abstract void paintComponent(Graphics g);

    public abstract void computeNewPosition();
    
}
