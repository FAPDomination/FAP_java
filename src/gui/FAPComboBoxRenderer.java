package gui;


import fap_java.Graph;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.basic.BasicComboBoxRenderer;


public class FAPComboBoxRenderer extends BasicComboBoxRenderer implements ListCellRenderer {
    public FAPComboBoxRenderer() {
        super();
        this.setBackground(Color.black);
        this.setOpaque(true);
    }

    public Component getListCellRendererComponent(
                                       JList list,
                                       Object value,
                                       int index,
                                       boolean isSelected,
                                       boolean cellHasFocus) {
        JLabel jl=new JLabel((String)value);
        jl.setOpaque(true);
        if (isSelected) {
            jl.setBackground(Graph.BLACK);
            jl.setForeground(Graph.BTN_COLOR);
        } else {
            jl.setBackground(Graph.BLACK);
            jl.setForeground(Graph.BTN_COLOR_HOVER);
        }

        return jl;
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.fillRect(0,0,40,50);
    }
}