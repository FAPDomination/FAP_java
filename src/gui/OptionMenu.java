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
        
        btnControls=new JButton();
        this.add(btnControls);
        initOptionBtn(btnControls,"Commandes",240+Tools.randRange(-40,40), 160+60*i);
        i++;
        
        btnSound=new JButton();
        initOptionBtn(btnSound,"Son On",240+Tools.randRange(-40,40), 160+60*i);
        this.add(btnSound);
        i++;
        
        btnGoBack=new JButton();
        initOptionBtn(btnGoBack,"Retour",240+Tools.randRange(-40,40), 160+60*i);
        this.add(btnGoBack);
        i++;
        
        btnControls.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                //LectureFichierSon.lire(Design.sonChtk);
                ((Button_MainMenuUI)btnControls.getUI()).setHover(true);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                ((Button_MainMenuUI)btnControls.getUI()).setHover(false);
            }
        });
        
        btnSound.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                //LectureFichierSon.lire(Design.sonChtk);
                ((Button_MainMenuUI)btnSound.getUI()).setHover(true);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                ((Button_MainMenuUI)btnSound.getUI()).setHover(false);
            }
        });
        
        btnGoBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                //LectureFichierSon.lire(Design.sonChtk);
                ((Button_MainMenuUI)btnGoBack.getUI()).setHover(true);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                ((Button_MainMenuUI)btnGoBack.getUI()).setHover(false);
            }
        });
        
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
    
    private void initOptionBtn(JButton jbutt, String label, int posx, int posy){
        jbutt.setUI(new Button_MainMenuUI());
        ((Button_MainMenuUI)jbutt.getUI()).setHover(false);
        jbutt.setText(label);
        jbutt.setSize(120, 40);
        jbutt.setLocation(posx, posy);
        jbutt.setOpaque(false);
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
