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

public class Button_SampleUI extends BasicButtonUI {
    
    // Dimension par défaut
    public static final int BUTTON_WIDTH = 80;
    public static final int BUTTON_HEIGHT = 30;
    
    private static final int MARGIN_VALUE = 40;
    private static final Insets BUTTON_MARGIN = new Insets(MARGIN_VALUE, 0, MARGIN_VALUE, 0);
    
    private Color hsquare= null;
    
    private boolean hover=false;
    
    public static ComponentUI createUI(JComponent b) {
        b.setVisible(true);
        b.setOpaque(false);
        return new Button_SampleUI();
    }
    
    public Button_SampleUI() {
        super();
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
        button.setForeground(Graph.BTN_COLOR);
        button.setBorder(Graph.NO_BORDER);
        button.setFont(Graph.BTN_MENU_FONT);
        //button.setVerticalTextPosition();
        Color square = null;
        if (isHover() && button.isEnabled()) {
            //button.setBackground(null);
            button.setForeground(Graph.BTN_COLOR_HOVER);
            if(hsquare == null){
                int randColor = Tools.randRange(0, Params.colorList.length-1);
                hsquare = Params.colorList[randColor];
            }
            square = hsquare;
            /*
            final int BWIDTH = button.getWidth();
            final int BHEIGHT = button.getHeight();
            g.setColor(Graph.BG_Blue);
            g.fillRect(0, 0, BWIDTH, BHEIGHT);
            */
        }
        Graph.drawDarkBackgroundRectangle(g, 5, 5, button.getWidth()-10, button.getHeight(), square, 15, Graph.BG_DARKTRANSLUSCENT);
        super.paint(g, button);
    }

    public void setHover(boolean hover) {
        this.hover = hover;
    }

    public boolean isHover() {
        return hover;
    }
}
