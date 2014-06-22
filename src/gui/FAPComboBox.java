package gui;

import fap_java.Graph;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import java.awt.Graphics2D;

import javax.swing.JComboBox;

public class FAPComboBox extends JComboBox{
    public FAPComboBox(Object[] listV) {
        super(listV);
    }
    
    public FAPComboBox() {
        super();
    }
    
    public void paintComponent(Graphics g){
        //super.paintComponent(g);
        g.setColor(Graph.BLACK);
        g.fillRect(0,0,this.getWidth()-33,this.getHeight());
        g.setColor(Graph.WHITE);
        String value = this.getSelectedItem().toString();
        g.setFont(Graph.SIMPLE_TEXT_FONT);
        Graphics2D g2d = (Graphics2D)g;
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(value);
        g.drawString(value, (this.getWidth()-33-textWidth)/2, (2*this.getHeight() - 20)/2);
        
    }
}
