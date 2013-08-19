package gui;

import fap_java.Graph;

import java.awt.BorderLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public abstract class FAPanel extends JPanel {
    protected TheFrame parent;
    protected JPanel prevPanel;
    // Images
    protected Image sword;
    protected Image clouds;
    // Locations
    protected double swordX;
    protected double cloudsX;
    protected double buttonsX;
    // Slide animation
    protected int speed;
    protected int maxxS = Constants.maxxS;
    protected int minxS = Constants.minxS; 
    protected int maxxC = Constants.maxxC;
    protected int minxC = Constants.minxC;
    
    protected JButton btnGoBack = new JButton();
    
    public FAPanel(TheFrame parent, JPanel prevPanel) {
        super();
        this.prevPanel = prevPanel;
        this.parent = parent;
        sword = Graph.getGuimg().get("MM_sword");
        clouds = Graph.getGuimg().get("MM_clouds");
        
        btnGoBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                goBack();
            }
        });
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        // Background
        int w = this.getWidth();
        int h = this.getHeight();

        // Paint a gradient from top to bottom
        GradientPaint gp = new GradientPaint(0, 0, Constants.top, 0, h, Constants.bottom);

        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);

        // Sword & clouds
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(clouds, (int)cloudsX, 50, 1600, 195, this);
        g2d.drawImage(sword, (int)swordX, 80, 2153, 762, this);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }
    
    public void goBack() {
        if(prevPanel instanceof MainMenu){
            MainMenu pp = ((MainMenu)prevPanel);
            pp.setNextPanel(null);
            pp.startSliding(false);
        }
        parent.changePanel(prevPanel, BorderLayout.CENTER);
    }
}
