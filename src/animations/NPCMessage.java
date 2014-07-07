package animations;

import fap_java.Graph;
import fap_java.TheComputingThread;
import fap_java.Tools;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import npcs.actions.AAsk;


public class NPCMessage extends Animation {
    private boolean ask;
    private String message;
    private String yes;
    private String no;
    private boolean choice;
    //private Image img;
    private AAsk launcher;
    private JLabel npcLabel;
    private int width = 389;
    private int height = 77;
    private int margins = 10;

    private int x = 0;
    private int y = 0;

    public NPCMessage(String message, String yes, String no, TheComputingThread thread, AAsk launcher, boolean bAsk) {
        super(0, 0, 0, thread);
        ask = bAsk;
        this.launcher = launcher;
        this.message = message;
        this.yes = yes;
        this.no = no;
        choice = true;
        npcLabel = new JLabel();

        x = (thread.getMyGame().getWidth() - width) / 2;
        y = (thread.getMyGame().getHeight() - height - 30);

        Tools.parametrizeJLabel(npcLabel, message, Graph.REGULAR_FONT, Color.WHITE, width - 2 * margins,
                                height - 2 * margins, x + margins, y + margins, SwingConstants.TOP);
        thread.getMyGame().add(npcLabel);
        //img = Graph.getGuimg().get("npcDisplayMessage");
        //thread.getMyGame().addAnim(this);
    }

    public NPCMessage(String message, String yes, String no, TheComputingThread thread, AAsk launcher) {
        this(message, yes, no, thread, launcher, true);
    }

    public NPCMessage(String message, TheComputingThread thread) {
        this(message, "", "", thread, null, false);
        ask = false;
        choice = false;
    }

    public String toString() {
        return null;
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        if (x != 0 && y != 0) {
            Graph.drawDarkBackgroundRectangle(g, x, y, width, height, Graph.NPC_SQUARE_COLOR, 20, Graph.BG_DARK);
            //g.drawImage(img,x,y,width,height,thread.getMyGame());

            //g.drawString(message, x+20, y+20);
            if (ask) {
                int fac;
                choice = launcher.isChoice();
                if (choice) {
                    fac = 0;
                } else {
                    fac = 1;
                }

                g.setColor(Color.WHITE);
                //TODO positions
                g.drawString(yes, x + (width / 4), y + 50); // these positions are bad, to be re-made
                g.drawString(no, x + (3 * width / 4), y + 50);
                //TODO paint cursor
                g.fillRect(x + (width / 4) - 6 + fac * 2 * width / 4, y + 45, 4, 4); // Whole cursor to be re-made
            }

        }
    }

    public void executeAnimation() {
    }

    public void setChoice(boolean choice) {
        this.choice = choice;
    }

    public boolean isChoice() {
        return choice;
    }

    public void endAnimation() {
        thread.getMyGame().remove(npcLabel);
        super.endAnimation();
    }
}
