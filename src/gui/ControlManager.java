package gui;

import fap_java.Params;

import fap_java.Tools;

import java.awt.Color;
import java.awt.Graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import java.util.ArrayList;

import javax.swing.JButton;

public class ControlManager{
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
        
        //TODO better init keyList
        keyList = new int[Params.numberOfKeys];
        try {
            FileInputStream fileIn = new FileInputStream(Constants.controlersFile);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            keyList = ((int[][])in.readObject())[controler];
            in.close();
            fileIn.close();
        } 
        catch(FileNotFoundException e){
            System.out.println("Couldn't load keys file");
        }
        catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Impossibru, class not found");
        }
        
        buttonList = new ArrayList<JButton>();
        for(int i=0;i<Params.numberOfKeys;i++){
            JButton jb = new JButton();
            jb.setSize(120, 40);
            jb.setLocation(x, y+incremY*i);
            buttonList.add(jb);
            //TODO better init text
            jb.setText(""+keyList[i]);
            jb.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    computeButtons(buttonList.indexOf(e.getSource()));
                }
            });
            panel.add(jb);
        }
        
    }
    
    public void receiveInfo(int code){
        if(active != -1){
            panel.setAllEnabled(true);
            JButton jb = buttonList.get(active);
            keyList[active] = code;
            //TODO Parse code
            //TODO better init text
            jb.setText(""+code);
            active = -1;
            panel.saveAll();
        }
    }
    
    
    public void paintComponent(Graphics g){
        int i=0;
        //TODO Relative
        //TODO pretty texts
        int decremX = 55;
        int initY = 25;
        g.setColor(Color.BLACK);
        //Title
        g.drawString("Player "+(controler+1), x+35, y-40);
        //Keys
        g.drawString("Haut", x-decremX, y+initY+incremY*i);
        i++;
        g.drawString("Bas", x-decremX, y+initY+incremY*i);
        i++;
        g.drawString("Droite", x-decremX, y+initY+incremY*i);
        i++;
        g.drawString("Gauche", x-decremX, y+initY+incremY*i);
        i++;
        g.drawString("Sort", x-decremX, y+initY+incremY*i);
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
    
    public void computeButtons(int buttonID){
        JButton jb = buttonList.get(buttonID);
        if(active == -1){
            panel.setAllEnabled(false);
            jb.setEnabled(true);
            active = buttonID;
            jb.setText("Press Key");
            panel.initFocus();
        }
        else{
            active = -1;
            panel.setAllEnabled(true);
            //TODO reinit text
        }
    }

    public void setKeyList(int[] keyList) {
        this.keyList = keyList;
    }

    public int[] getKeyList() {
        return keyList;
    }
}
