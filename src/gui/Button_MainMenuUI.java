package gui;

import fap_java.Graph;

import java.awt.Color;
import java.awt.Dimension;

import java.awt.FontMetrics;
import java.awt.Graphics;

import java.awt.Insets;

import java.awt.Rectangle;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;

import sun.swing.SwingUtilities2;

public class Button_MainMenuUI extends BasicButtonUI {
    
    // Dimension par défaut
    public static final int BUTTON_WIDTH = 200;
    public static final int BUTTON_HEIGHT = 30;
    
    private static final int MARGIN_VALUE = 40;
    private static final Insets BUTTON_MARGIN = new Insets(MARGIN_VALUE, 0, MARGIN_VALUE, 0);
    
    private boolean hover=false;
    
    private static Rectangle viewRect = new Rectangle();
    private static Rectangle textRect = new Rectangle();
    private static Rectangle iconRect = new Rectangle();
    
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
        button.setForeground(Graph.BLACK);
        button.setBorder(Graph.NO_BORDER);
        button.setFont(Graph.BTN_MENU_FONT);

        if (isHover()) {
            button.setForeground(Graph.GREY_DARK);
        }
        
        
        
        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();


        
        String text = layout(b, SwingUtilities2.getFontMetrics(b, g),
               b.getWidth(), b.getHeight());

        clearTextShiftOffset();
        
        // perform UI specific press action, e.g. Windows L&F shifts text
        if (model.isArmed() && model.isPressed()) {
            paintButtonPressed(g,b); 
        }

        Color j = g.getColor();
        g.setColor(new Color(0,0,0,0));
        //TODO better border
        Graph.drawBorderedString(g, textRect.getLocation().x,textRect.getLocation().y+21, text, Graph.WHITE_ALPHA_160, 2);
        g.setColor(j);

        if (text != null && !text.equals("")){
            View v = (View) c.getClientProperty(BasicHTML.propertyKey);
            if (v != null) {
                v.paint(g, textRect);
            } else {
                paintText(g, b, textRect, text);
            }
        }


        if (b.isFocusPainted() && b.hasFocus()) {
            // paint UI specific focus
            paintFocus(g,b,viewRect,textRect,iconRect);
        }
    }

    public void setHover(boolean hover) {
        this.hover = hover;
    }

    public boolean isHover() {
        return hover;
    }
    
    private String layout(AbstractButton b, FontMetrics fm,
                          int width, int height) {
        Insets i = b.getInsets();
        viewRect.x = i.left;
        viewRect.y = i.top;
        viewRect.width = width - (i.right + viewRect.x);
        viewRect.height = height - (i.bottom + viewRect.y);

        textRect.x = textRect.y = textRect.width = textRect.height = 0;
        iconRect.x = iconRect.y = iconRect.width = iconRect.height = 0;

        // layout the text and icon
        return SwingUtilities.layoutCompoundLabel(
            b, fm, b.getText(), b.getIcon(), 
            b.getVerticalAlignment(), b.getHorizontalAlignment(),
            b.getVerticalTextPosition(), b.getHorizontalTextPosition(),
            viewRect, iconRect, textRect, 
            b.getText() == null ? 0 : b.getIconTextGap());
    }
}
