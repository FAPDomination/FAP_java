package gui;

import fap_java.Game;

import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class LoadingScreen extends FAPanel {
    Game game;
    private JButton btnPlay = new JButton();
    public LoadingScreen(TheFrame fr, Game game, JPanel prevPanel) {
        super(fr, prevPanel);
        this.game = game;

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
                startGame();
            }
        });
    }
    
    public void startGame(){
        game.pauseGame();
        parent.changePanel(game, BorderLayout.CENTER);
    }
}
