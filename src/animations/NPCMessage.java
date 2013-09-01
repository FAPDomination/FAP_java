package animations;

import fap_java.Graph;
import fap_java.TheThread;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class NPCMessage extends Animation {
    private boolean ask;
    private String message;
    private String yes;
    private String no;
    private boolean choice;
    private Image img;
    public NPCMessage(String message, String yes, String no,TheThread thread) {
        super(0,0,0,thread);
        ask = true;
        this.message = message;
        this.yes = yes;
        this.no = no;
        choice = true;
        img = Graph.getGuimg().get("npcDisplayMessage");
        thread.getMyGame().addAnim(this);
    }
    
    public NPCMessage(String message,TheThread thread){
        this(message,"","",thread);
        ask = false;
        choice = false;
    }

    public String toString() {
        return null;
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        int width=389;
        int height=77;
        int x=(thread.getMyGame().getWidth()-width)/2;
        int y=(thread.getMyGame().getHeight()-height-30);

        g.drawImage(img,x,y,width,height,thread.getMyGame());

        g.drawString(message, x+20, y+20);
        if(ask){
            int fac;
            if(choice){
                fac=0;
            }
            else{
                fac = 1;
            }
            
            g.drawString(yes, x+(width/4), y+40);       // these positions are bad, to be re-made
            g.drawString(no, x+(3*width/4), y+40);
            //TODO paint cursor
            g.fillRect(x+(width/4)-6+fac*2*width/4, y+35, 4,4);       // Whole cursor to be re-made
        }
        
        //TODO multiline
        //TODO resizable sizes (incl. multiline) and positions
    }

    public void executeAnimation(){}

    public void setChoice(boolean choice) {
        this.choice = choice;
    }

    public boolean isChoice() {
        return choice;
    }
}
