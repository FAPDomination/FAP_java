package animations;

import fap_java.TheThread;

import java.awt.Color;
import java.awt.Graphics;

public class PauseCountDown extends Animation {
    public PauseCountDown(int x, int y, int duration,TheThread thread) {
        super(x,y,duration, thread);
    }

    public String toString() {
        return "The pause countdown "+position;
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.drawString(""+((duration-position)/1000), x, y);
    }
    
    public void executeAnimation(){
        super.executeAnimation();
        // The only animation that forces the game to repaint
        thread.getMyGame().repaint();
    }

    public void endAnimation() {
        super.endAnimation();
        thread.setRunning(true);
    }
}
