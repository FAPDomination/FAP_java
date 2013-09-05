package gui;

import fap_java.Tools;

import java.awt.Graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class OptionMenu extends FAPanel {
    
    private JButton btnControls = new JButton();
    private JButton btnSound = new JButton();
    
    public OptionMenu(TheFrame fr, JPanel prevPanel) {
        super(fr,prevPanel);
        
        swordX = minxS;
        cloudsX = minxC;
        
        this.setLayout(null);
        this.setSize(Constants.frameDimension);
        
        int i=0;
        
        btnControls.setText("Commandes");
        btnControls.setSize(120, 40);
        btnControls.setLocation(240+Tools.randRange(-40, 40), 160+60*i);
        this.add(btnControls);
        i++;
        
        btnSound.setText("Son On");
        btnSound.setSize(120, 40);
        btnSound.setLocation(240+Tools.randRange(-40, 40), 160+60*i);
        this.add(btnSound);
        i++;
        
        btnGoBack.setText("Retour");
        btnGoBack.setSize(120, 40);
        btnGoBack.setLocation(240+Tools.randRange(-40, 40), 160+60*i);
        this.add(btnGoBack);
        i++;
        
        btnSound.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toggleSound();
            }
        });
        btnControls.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                goToControls();
            }
        });
        
        this.validate();
        this.repaint();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
    
    private void toggleSound(){
        //TODO toggle sound
        /*
         * That could be done with a big boolean parameter stored in gui.Constants
         * Pressing this button will check the boolean and
         * start/stop all sound (or set volume to 0 or whatever way is necessary there)
         * 
         * If possible, also change the text on the button to do "Sound off" / "Sound on"
         */
        System.out.println("Toggling sound");
    }
    
    private void goToControls(){
        // Proceeding to next panel
        JPanel nextPanel = new ControlsPanel(parent,this);
        parent.changePanel(nextPanel);
    }
}
