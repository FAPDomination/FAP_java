package gui;

import fap_java.Graph;

import fap_java.Params;
import fap_java.Tools;

import java.awt.Color;
import java.awt.Dimension;

import java.awt.Graphics;

import java.awt.Insets;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;

public class Button_AddRemoveUI extends BasicButtonUI {
    
    // Dimension par défaut
    public static final int BUTTON_WIDTH = 80;
    public static final int BUTTON_HEIGHT = 30;
    
    private static final int MARGIN_VALUE = 40;
    private static final Insets BUTTON_MARGIN = new Insets(MARGIN_VALUE, 0, MARGIN_VALUE, 0);
    
    private boolean hover=false;
    String type;
    public static ComponentUI createUI(JComponent b) {
        b.setVisible(true);
        b.setOpaque(false);
        return new Button_AddRemoveUI("");
    }
    
    public Button_AddRemoveUI(String type) {
        super();
        this.type = type;
    }
    
    public Dimension getPreferredSize(JComponent c) {
        AbstractButton button = (AbstractButton)c;
        int width = Math.max(button.getWidth(), BUTTON_WIDTH);
        int height = Math.max(button.getHeight(), BUTTON_HEIGHT);
        return new Dimension(width, height);
    }
    
    
    public void paint(Graphics g, JComponent c) {
        AbstractButton button = (AbstractButton)c;

        //button.setSize(BUTTON_WIDTH,BUTTON_HEIGHT);
        //button.setOpaque(false);
        button.setBackground(null);
        //button.setPreferredSize(Design.MENU_BUTTON_DIM);
        button.setRolloverEnabled(true);
        button.setMargin(BUTTON_MARGIN);
        button.setBorder(Graph.NO_BORDER);
        //button.setVerticalTextPosition();
        
        if (isHover() && button.isEnabled()) {
            g.drawImage(Graph.getGuimg().get(this.type+"_hover"), 0, 0, null);
        }
        else{
            g.drawImage(Graph.getGuimg().get(this.type), 0, 0, null);
        }
        super.paint(g, button);
    }

    public void setHover(boolean hover) {
        this.hover = hover;
    }

    public boolean isHover() {
        return hover;
    }
}
