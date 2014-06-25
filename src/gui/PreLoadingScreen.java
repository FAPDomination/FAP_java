package gui;

import com.sun.jmx.snmp.Timestamp;

import fap_java.Game;
import fap_java.Graph;

import java.awt.Color;
import java.awt.Graphics;

import java.util.Date;

import javax.swing.JPanel;

public class PreLoadingScreen extends FAPanel implements AnimPanel {
    private String message;
    private ThreadGUI theThread;
    private boolean initiated;
    
    private long begin;
    
    public PreLoadingScreen(TheFrame theFrame) {
        super(theFrame, null);
        message = "Loading";
        initiated = false;
        
        theThread = new ThreadGUI(this);
        new Thread(this.theThread).start();
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.drawString(message, 20, 20);	
    }

    public void executeAnim() {
        message = "Loading";
        if(!initiated){
            Date d = new Date();
            begin = d.getTime();
            
            Graph.load(this);
            initiated = true;
        }
    }

    public void endAnim() {
        //MainMenu menu = new MainMenu(this.parent,false);
        Date d = new Date();
        //System.out.println("Resources loaded in "+(d.getTime()-begin)+"ms");
        Fapplication.setWorldMap(new Game(0));
        parent.changePanel(new MainMenu(this.parent,false));
        
    }
}
