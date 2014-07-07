package animations;

import fap_java.Graph;
import fap_java.Params;
import fap_java.TheComputingThread;

import java.awt.Graphics;
import java.awt.Image;


public class AnimFrozen extends Animation {
    private int appearing; //frames
    private Image img;
    private int offX = -26;
    private int offY = -38;
    private int width = 63;
    private int maxHeight = 75;

    public AnimFrozen(int x, int y, TheComputingThread theThread) {
        super(x, y, Params.howLongBlockingMagician * 1000, theThread);
        img = Graph.getList().get("frozen");
        this.x += offX;
        this.y += offY + maxHeight;
        appearing = 4 * theThread.getDelay();
    }

    public String toString() {
        return "Animation for Frozen";
    }

    public void paintComponent(Graphics g) {
        int height = (int)(maxHeight * ((double)this.position) / appearing);
        if (height >= maxHeight) {
            height = maxHeight;
        }
        g.drawImage(img, x, y - height, width, height, this.getThread().getMyGame());
    }
}
