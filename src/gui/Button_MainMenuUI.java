package gui;

import fap_java.Graph;

import java.awt.Dimension;

import java.awt.Graphics;

import java.awt.Insets;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;

public class Button_MainMenuUI extends BasicButtonUI {
    
    // Dimension par défaut
    public static final int BUTTON_WIDTH = 200;
    public static final int BUTTON_HEIGHT = 30;
    
    private static final int MARGIN_VALUE = 40;
    private static final Insets BUTTON_MARGIN = new Insets(MARGIN_VALUE, 0, MARGIN_VALUE, 0);
    
    private boolean hover=false;
    
    public static ComponentUI createUI(JComponent b) {
        b.setVisible(true);
        b.setOpaque(false);
        return new Button_MainMenuUI();
    }
    
    public Button_MainMenuUI() {
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

        button.setSize(BUTTON_WIDTH,BUTTON_HEIGHT);
        //button.setOpaque(false);
        button.setBackground(null);
        //button.setPreferredSize(Design.MENU_BUTTON_DIM);
        button.setRolloverEnabled(true);
        button.setMargin(BUTTON_MARGIN);
        button.setForeground(Graph.BG_Blue);
        button.setBorder(Graph.NO_BORDER);
        button.setFont(Graph.BTN_MENU_FONT);

        if (isHover()) {
            //button.setBackground(null);
            button.setForeground(Graph.BG_Red);
            /*
            final int BWIDTH = button.getWidth();
            final int BHEIGHT = button.getHeight();
            g.setColor(Graph.BG_Blue);
            g.fillRect(0, 0, BWIDTH, BHEIGHT);
            */
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
