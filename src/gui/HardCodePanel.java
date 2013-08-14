package gui;

import fap_java.Game;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class HardCodePanel extends FAPanel{
    private JButton btnPlay = new JButton();
    private JTextField jtxtWho = new JTextField();
    private JTextField jtxtisFSM = new JTextField();
    private JTextField jtxtTeam = new JTextField();
    
    public HardCodePanel(TheFrame fr, JPanel prevPanel) {
        super(fr, prevPanel);
        
        swordX = minxS;
        cloudsX = minxC;
        
        this.setLayout(null);
        this.setSize(Constants.frameDimension);

        btnGoBack.setText("Retour");
        btnGoBack.setSize(120, 40);
        btnGoBack.setLocation(20, 20);
        this.add(btnGoBack);
        
        btnPlay.setText("Play");
        btnPlay.setSize(120, 40);
        btnPlay.setLocation(600, 250);
        this.add(btnPlay);
        
        btnPlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                next();
            }
        });
        
        this.jtxtWho.setText("0,0,0,0");
        this.jtxtTeam.setText("0,0,0,0");
        this.jtxtisFSM.setText("0,0,0,0");
            
        this.jtxtWho.setSize(120, 40);
        this.jtxtTeam.setSize(120, 40);
        this.jtxtisFSM.setSize(120, 40);

        this.jtxtWho.setLocation(100, 100);
        this.jtxtTeam.setLocation(240, 100);
        this.jtxtisFSM.setLocation(380, 100);
        
        this.add(jtxtWho);
        this.add(jtxtTeam);
        this.add(jtxtisFSM);
    }
    
    public void next(){
        String whoIsPlaying = jtxtWho.getText();
        String wichTeam = jtxtTeam.getText();
        String isFSM = jtxtisFSM.getText();
        
        Game game = new Game(whoIsPlaying,wichTeam,isFSM,false,5);
       parent.changePanel(new LoadingScreen(parent,game,this), BorderLayout.CENTER);
    }
}
