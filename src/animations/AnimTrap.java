package animations;

import fap_java.Graph;
import fap_java.Params;
import fap_java.TheComputingThread;

import java.awt.Graphics;
import java.awt.Image;


public class AnimTrap extends Animation {
    private Image img;
    private int height = 40;
    private int width = 40;
    private int offX = -15;
    private int offY = -5;
    private double origFac = 3;
    private int origY;
    private double fac;

    private int shiftY = -100;
    private int durationMovement = 20;

    public AnimTrap(int x, int y, TheComputingThread theThread) {
        super(x, y, Params.timeWarningTrap * 1000, theThread);
        img = Graph.getList().get("warlock_Trap");
        this.x += offX;
        this.y += offY;

        origY = y;
    }

    public String toString() {
        return "A curse creating a trap";
    }

    public void paintComponent(Graphics g) {
        int frame = (int)(((double)this.position) / thread.getDelay());
        fac = origFac * (1 - ((double)frame) / durationMovement);
        if (fac < 1) {
            fac = 1;
        }
        y = (int)(origY + (shiftY * (1 - ((double)frame) / durationMovement)));
        if (y > origY) {
            y = origY;
        }
        g.drawImage(img, x - (int)((fac - 1) / 2 * width), y, (int)(width * fac), (int)(height * fac),
                    this.getThread().getMyGame());
    }
}
