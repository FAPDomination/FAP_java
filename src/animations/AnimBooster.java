package animations;

import fap_java.Graph;
import fap_java.TheComputingThread;

import java.awt.Graphics;
import java.awt.Image;


public class AnimBooster extends Animation {
    private int appearing = 8; //frames
    private Image img;
    private int offX = -15;
    private int offY = -10;
    private int width = 40;
    private int height = 60;

    private int speedY = -4;

    private int f1 = offY;
    private int f2 = offY;
    private int f3 = offY;

    private int dec2 = 3;
    private int dec3 = 7;

    public AnimBooster(int x, int y, TheComputingThread theThread) {
        super(x, y, 1000, theThread);
        img = Graph.getList().get("booster");
        this.x += offX;
    }

    public String toString() {
        return "BOOSTER SPEED !";
    }

    public void paintComponent(Graphics g) {
        int frame = (int)(((double)this.position) / thread.getDelay());
        //F1 :
        if (frame < appearing) {
            g.drawImage(img, x, y + f1, width, height, this.getThread().getMyGame());
            f1 += speedY;
        }
        //F2 :
        if (frame >= dec2 && frame < appearing + dec2) {
            g.drawImage(img, x + 4, y + f2, width, height, this.getThread().getMyGame());
            f2 += speedY;
        }
        //F3 :
        if (frame >= dec3 && frame < appearing + dec3) {
            g.drawImage(img, x - 4, y + f3, width, height, this.getThread().getMyGame());
            f3 += speedY;
        }
    }
}
