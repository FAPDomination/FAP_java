package gui;

import fap_java.Params;

import java.awt.Graphics;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import java.util.ArrayList;

import javax.swing.JPanel;

public class ControlsPanel extends FAPanel implements NeedingFocus, AnimPanel {
    ArrayList<ControlManager> listForms;
    private StandardKListener kl;
    private ThreadGUI theThread;

    public ControlsPanel(TheFrame theFrame, JPanel jPanel) {
        super(theFrame, jPanel);

        swordX = minxS;
        cloudsX = minxC;

        this.setLayout(null);
        this.setSize(Constants.frameDimension);

        btnGoBack.setText("Retour");
        btnGoBack.setSize(120,60);
        btnGoBack.setLocation(origX-5, origY-5);
        this.add(btnGoBack);

        //TODO responsive
        
        int incrementX = 250;
        int initX = (this.getWidth()-3*incrementX)/2 + 70;
        int initY = 200;
        listForms = new ArrayList<ControlManager>();
        for (int i = 0; i < Params.nPlayersOn1Computer; i++) {
            listForms.add(new ControlManager(this, i, (i) * incrementX + initX, initY));
        }

        initFocus();

        this.validate();
        this.repaint();

        theThread = new ThreadGUI(this);
        theThread.setDelay(100);
        new Thread(this.theThread).start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < listForms.size(); i++) {
           listForms.get(i).paintComponent(g);
        }
        
        //TODO put setX and setY for everyone here so it becomes relative
    }

    private void initKListener() {
        kl = new StandardKListener();
        this.addKeyListener(kl);
    }

    public void initFocus() {
        initKListener();
        this.setFocusable(true);
        requestFocus();
    }

    public void setAllEnabled(boolean enabled) {
        for (int i = 0; i < listForms.size(); i++) {
            for (int j = 0; j < listForms.get(i).getButtonList().size(); j++) {
                listForms.get(i).getButtonList().get(j).setEnabled(enabled);
            }
        }
    }

    public void releaseFocus() {
    }

    public void executeAnim() {
        if (kl.getPressed().size() > 0) {
            for (int i = 0; i < listForms.size(); i++) {
                ControlManager manager = listForms.get(i);
                if (manager.getActive() != -1) {
                    manager.receiveInfo(kl.getPressed().get(0));
                }
            }
        }
    }

    public void endAnim() {
        // NEVAH
    }
    
    public void saveAll(){
        
        //Collecting
        int[][] keyList = new int[listForms.size()][Params.numberOfKeys];
        for(int i=0;i<listForms.size();i++){
            keyList[i] = listForms.get(i).getKeyList();
        }
        
        //Saving
        try {
            FileOutputStream fileOut = new FileOutputStream(Constants.c.get(Constants.controlersFile));
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(keyList);
            out.close();
            fileOut.close();
            System.out.println("Saved controls in "+Constants.c.get(Constants.controlersFile));
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
}
