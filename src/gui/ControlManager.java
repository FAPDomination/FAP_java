package gui;

import fap_java.Params;

import fap_java.Tools;

import java.awt.Graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;

import javax.swing.JButton;

public class ControlManager{
    private ControlsPanel panel;
    private int controler;
    private int x;
    private int y;
    private int active;
    private ArrayList<JButton> buttonList;
    
    public ControlManager(ControlsPanel panel, int controler, int x, int y) {
        super();
        this.panel = panel;
        this.controler = controler;
        this.x = x;
        this.y = y;
        active = -1;
        buttonList = new ArrayList<JButton>();
        for(int i=0;i<Params.numberOfKeys;i++){
            JButton jb = new JButton();
            jb.setSize(120, 40);
            jb.setLocation(x, y+60*i);
            buttonList.add(jb);
            //TODO init text
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
            //TODO save code
            //TODO Parse code
            //TODO init text
            jb.setText("");
            active = -1;
        }
    }
    
    
    public void paintComponent(Graphics g){
	
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
}
