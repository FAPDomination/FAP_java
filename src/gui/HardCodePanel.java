package gui;

import fap_java.Game;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.MouseAdapter;

import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class HardCodePanel extends FAPanel implements NeedingFocus{
    private JButton btnPlay = new JButton();
    private JTextField jtxtWho = new JTextField();
    private JTextField jtxtisFSM = new JTextField();
    private JTextField jtxtTeam = new JTextField();
    private JTextField jtxtControlers = new JTextField();
    private Minimap mini;
    
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
        this.jtxtControlers.setText("0,0,0,0");
            
        this.jtxtWho.setSize(120, 40);
        this.jtxtTeam.setSize(120, 40);
        this.jtxtisFSM.setSize(120, 40);
        this.jtxtControlers.setSize(120,40);

        this.jtxtWho.setLocation(100, 100);
        this.jtxtTeam.setLocation(240, 100);
        this.jtxtisFSM.setLocation(380, 100);
        this.jtxtControlers.setLocation(520, 100);
        
        this.add(jtxtWho);
        this.add(jtxtTeam);
        this.add(jtxtisFSM);
        this.add(jtxtControlers);
        
        mini = new Minimap(5,300,300,this,"La Riviere");
        
        initFocus();
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        mini.paintComponent(g);
    }
    
    public void next(){
        System.out.println("yay");
        String whoIsPlaying = jtxtWho.getText();
        String wichTeam = jtxtTeam.getText();
        String isFSM = jtxtisFSM.getText();
        String controlers = jtxtControlers.getText();
        
        Game game = new Game(whoIsPlaying,wichTeam,isFSM,controlers,false,5);
       parent.changePanel(new LoadingScreen(parent,game,this), BorderLayout.CENTER);
    }

    public void initFocus() {
        this.setFocusable(true);
        requestFocus();
    }
}
