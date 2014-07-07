package gui;

import fap_java.Game;
import fap_java.Graph;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.UIManager;


public class Fapplication {
    private static JFrame frame;
    private static Game worldMap;
    public Fapplication() {
        frame = new TheFrame();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        frame.setLocation( ( screenSize.width - frameSize.width ) / 2, ( screenSize.height - frameSize.height ) / 2 );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("ComboBox.background", Graph.BLACK);
            UIManager.put("ComboBox.selectionBackground", Graph.BLACK);
            UIManager.put("ComboBox.selectionForeground", Graph.BTN_COLOR_HOVER);
            UIManager.put("ComboBox.buttonBackground", Graph.BLACK);
            UIManager.put("ComboBox.buttonDarkShadow", Graph.BLACK);
            UIManager.put("ComboBox.buttonHighlight", Graph.BLACK);
            UIManager.put("ComboBox.buttonBackground", Graph.BLACK);
            UIManager.put("ComboBox.foreground", Graph.BTN_COLOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Fapplication();
    }

    public static void setFrame(JFrame frame) {
        Fapplication.frame = frame;
    }

    public static JFrame getFrame() {
        return frame;
    }

    public static void setWorldMap(Game worldMap) {
        Fapplication.worldMap = worldMap;
    }

    public static Game getWorldMap() {
        return worldMap;
    }
    
    public static void exitOnError(){
        System.exit(2);
    }
}
