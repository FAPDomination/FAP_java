package gui;

import fap_java.Graph;
import fap_java.Params;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import java.util.ArrayList;

import javax.swing.JButton;


public class ControlManager {
    private ControlsPanel panel;
    private int controler;
    private int x;
    private int y;
    private int active;
    private ArrayList<JButton> buttonList;

    private int[] keyList;

    private int incremY = 60;

    public ControlManager(ControlsPanel panel, int controler, int x, int y) {
        super();
        this.panel = panel;
        this.controler = controler;
        this.x = x;
        this.y = y;
        active = -1;

        keyList = new int[Params.numberOfKeys];
        try {
            FileInputStream fileIn = new FileInputStream(Constants.c.get(Constants.controlersFile));
            ObjectInputStream in = new ObjectInputStream(fileIn);
            keyList = ((int[][])in.readObject())[controler];
            in.close();
            fileIn.close();
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't load keys file");
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Impossibru, class not found");
        }

        buttonList = new ArrayList<JButton>();
        for (int i = 0; i < Params.numberOfKeys; i++) {
            JButton jb = new JButton();
            jb.setSize(120, 40);
            jb.setLocation(x, y + incremY * i);

            jb.setUI(new Button_SampleUI());
            ((Button_SampleUI)jb.getUI()).setHover(false);
            jb.setOpaque(false);

            buttonList.add(jb);

            jb.setText("" + KeyEvent.getKeyText(keyList[i]));
            jb.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    computeButtons(buttonList.indexOf(e.getSource()));
                }
            });
            panel.add(jb);
        }

    }

    public void receiveInfo(int code) {
        if (active != -1) {
            panel.setAllEnabled(true);
            JButton jb = buttonList.get(active);
            ((Button_SampleUI)jb.getUI()).setHover(false);
            keyList[active] = code;

            jb.setText("" + KeyEvent.getKeyText(code));
            active = -1;
            panel.saveAll();
        }
    }


    public void paintComponent(Graphics g) {
        int i = 0;
        //TODO Relative
        //TODO pretty texts
        int decremX = 55;
        int initY = 25;
        g.setColor(Color.BLACK);
        //Title
        g.setFont(Graph.H2_TITLE_FONT);
        Graph.drawBorderedString(g, x + 25, y - 35, "Player " + (controler + 1), Graph.MENU_TEXT_BORDER_TRANSLUSCENT);
        g.setFont(Graph.SIMPLE_TEXT_FONT);
        //Keys
        Graph.drawBorderedString(g, x - decremX, y + initY + incremY * i, "Haut ",
                                 Graph.MENU_TEXT_BORDER_TRANSLUSCENT);
        i++;
        Graph.drawBorderedString(g, x - decremX, y + initY + incremY * i, "Bas ", Graph.MENU_TEXT_BORDER_TRANSLUSCENT);
        i++;
        Graph.drawBorderedString(g, x - decremX, y + initY + incremY * i, "Droite ",
                                 Graph.MENU_TEXT_BORDER_TRANSLUSCENT);
        i++;
        Graph.drawBorderedString(g, x - decremX, y + initY + incremY * i, "Gauche ",
                                 Graph.MENU_TEXT_BORDER_TRANSLUSCENT);
        i++;
        Graph.drawBorderedString(g, x - decremX, y + initY + incremY * i, "Sort ",
                                 Graph.MENU_TEXT_BORDER_TRANSLUSCENT);
    }

    public void setPanel(ControlsPanel panel) {
        this.panel = panel;
    }

    public ControlsPanel getPanel() {
        return panel;
    }

    public void setControler(int controler) {
        this.controler = controler;
    }

    public int getControler() {
        return controler;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getActive() {
        return active;
    }

    public void setButtonList(ArrayList<JButton> buttonList) {
        this.buttonList = buttonList;
    }

    public ArrayList<JButton> getButtonList() {
        return buttonList;
    }

    public void computeButtons(int buttonID) {
        JButton jb = buttonList.get(buttonID);
        if (active == -1) {
            panel.setAllEnabled(false);
            jb.setEnabled(true);
            ((Button_SampleUI)jb.getUI()).setHover(true);
            active = buttonID;
            jb.setText("Press Key");
            panel.initFocus();
        } else {
            active = -1;
            panel.setAllEnabled(true);
            ((Button_SampleUI)jb.getUI()).setHover(false);

            jb.setText("" + KeyEvent.getKeyText(keyList[buttonID]));
        }
    }

    public void setKeyList(int[] keyList) {
        this.keyList = keyList;
    }

    public int[] getKeyList() {
        return keyList;
    }
}
