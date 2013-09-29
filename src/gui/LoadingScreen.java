package gui;

import fap_java.Game;

import fap_java.Graph;
import fap_java.Player;
import fap_java.Team;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

public class LoadingScreen extends FAPanel {
    private Game game;
    private JButton btnPlay = new JButton();
    private Minimap minimap;
    
    // Get infos from the game
    private ArrayList<Team> teams;
    private int origX = 40;
    private int origY = 80;
    private int incremY = 120;
    private int increm8X = 180;
    private int incremPlayerX = 30;
    
    public LoadingScreen(TheFrame fr, Game game, JPanel prevPanel, int nmap) {
        super(fr, prevPanel);
        this.game = game;
        
        teams = game.getTeams();
        
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
        btnPlay.setLocation(630, 370);
        this.add(btnPlay);
        
        minimap = new Minimap(nmap,610,100,this,"");
        //LS_BGteam : 322,103
        btnPlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
    }
    
    public void startGame(){
        game.pauseGame();
        parent.changePanel(game.getDisplayer(), BorderLayout.CENTER);
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        minimap.paintComponent(g);
        int col = 0;
        int nPerCol = 3;
        int triggerNewDispo = 4;
        //TODO better displaying of teams (with char)
        for(int i=0;i<teams.size();i++){
            int y;
            if(teams.size()>triggerNewDispo){
                y = (origY+(i%nPerCol)*incremY);
                g.drawImage(Graph.getGuimg().get("LS_BG8team"), origX+col*increm8X, y, this);
            }
            else{
                y = (origY+(i)*incremY);
                g.drawImage(Graph.getGuimg().get("LS_BGteam"), origX, y, this);
            }
            g.setColor(Color.black);
            //TODO pick one or the other
            //g.drawString("Team "+(i+1), origX+20+increm8X*col, y+30);
            g.drawString(""+teams.get(i), origX+20+increm8X*col, y+30);
            ArrayList<Player> players = teams.get(i).getPlayersInThisTeam();
            for(int j=0;j<players.size();j++){
                players.get(j).paintStick(g, origX+30+incremPlayerX*j+col*increm8X, y+50);
            }
            if(((i%nPerCol) == nPerCol-1) && teams.size()>triggerNewDispo){
                col++;
            }
        }
    }
}
