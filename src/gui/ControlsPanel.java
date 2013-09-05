package gui;

import fap_java.Params;

import java.awt.Graphics;

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
        btnGoBack.setSize(120, 40);
        btnGoBack.setLocation(20, 20);
        this.add(btnGoBack);

        int incrementX = 200;
        int initY = 100;
        listForms = new ArrayList<ControlManager>();
        for (int i = 0; i < Params.nPlayersOn1Computer; i++) {
            listForms.add(new ControlManager(this, i, (i + 1) * incrementX, initY));
        }

        initFocus();

        this.validate();
        this.repaint();

        theThread = new ThreadGUI(this);
        theThread.setRunning(false);
        theThread.setDelay(100);
        new Thread(this.theThread).start();
        theThread.setRunning(true);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
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
}
