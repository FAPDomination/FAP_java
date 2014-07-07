package animations;

import fap_java.Element;
import fap_java.Graph;
import fap_java.PauseScreen;
import fap_java.TheComputingThread;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class PauseCountDown extends Animation {
    BufferedImage bufferedImage;
    public PauseCountDown(int duration,TheComputingThread thread) {
        super(0,0,duration, thread);
        this.x = thread.getMyGame().getWidth()-200;
        this.y = (int)(thread.getMyGame().getHeight()*0.1 +1);
    }

    public String toString() {
        return "The pause countdown "+position;
    }

    public void paintComponent(Graphics g) {
        Graph.drawDarkBackgroundRectangle(g, x-30, y, 160, 200, Graph.DEFAULT_SQUARE_COLOR, 20, Graph.BG_DARK);
        g.setColor(Color.white);
        //TODO pretty text
        g.setFont(Graph.PAUSECOUNTDOWN_FONT);
        g.drawString(""+((duration-position)/1000), x, y+150);
    }
    
    public void executeAnimation(){
        super.executeAnimation();
        // The only animation that forces the game to repaint
        thread.getMyGame().repaint();
    }

    public void endAnimation() {
        super.endAnimation();
        for(int j=0;j<thread.getMyGame().getObjects().size();j++){
            Element e = thread.getMyGame().getObjects().get(j);
            if(e instanceof PauseScreen){
                ((PauseScreen)e).exit();
                thread.getMyGame().deleteObject(e);
            }
        }
        thread.setRunning(true);
    }
}
