package gui;

import fapLan.Client;
import fapLan.Host;

import fap_java.Game;

import fap_java.Params;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JPanel;

public class HardCodePanel extends FAPanel {
    private JButton btnPlay = new JButton();
    private Game game;
    public HardCodePanel(TheFrame theFrame, JPanel jPanel) {
        super(theFrame, jPanel);
        
        btnGoBack.setText("Retour");
        btnGoBack.setSize(120, 40);
        btnGoBack.setLocation(20, 20);
        this.add(btnGoBack);
        
        btnPlay.setText("Play");
        btnPlay.setSize(120, 40);
        btnPlay.setLocation(630, 370);
        this.add(btnPlay);
        
        btnPlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        
        //game = new Game("" + 1 + "," + 1, "0,1", "0,1","0,1", false, 5,Params.defaultVictoryScore,0,0,0);
        game = new Game();
    }
    
    public void startGame(){
        //What the host needs
        
        System.out.println("tryina launch");
        Host h = new Host(game);
        Thread th1 = new Thread(h);
        th1.start();
        
        InetAddress srv=null;
        
        /*
        try {
            srv = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
        }
        */
        //What the client needs
        try {
            srv = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
        }
        Client ci = new Client(srv,parent);
        
        game.pauseGame();
    }
}
