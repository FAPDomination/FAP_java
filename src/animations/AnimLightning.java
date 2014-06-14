package animations;

import fap_java.Graph;
import fap_java.Params;
import fap_java.TheComputingThread;

import fap_java.Tools;

import java.awt.Graphics;
import java.awt.Image;

public class AnimLightning extends Animation {
    private int randLght;
    public AnimLightning(int x, int y, TheComputingThread theThread) {
        super(x, y,10*theThread.getDelay(), theThread);
        randLght = Tools.randRange(1, Params.NLIGHTNING_EFFECTS);
    }

    public String toString() {
        return "Lightning in yo face";
    }

    public void paintComponent(Graphics g) {
        int frame = (int)(((double)this.position)/thread.getDelay());
        //Image img = Graph.getList().get("Lght"+randLght+"_"+3);
        Image img = Graph.getList().get("Lght"+randLght+"_"+(frame+1));
        int h = 292;
        int offx = -62;
        int offy = 100;
        g.drawImage(img, x+offx, y-h+offy, 131, h, thread.getMyGame());
    }
}
