package fap_java;

import java.awt.Color;
import java.awt.Graphics;

import java.util.ArrayList;

public class ScoreBar {
    
    private int totalTakable;
    private Game game;
    
    public ScoreBar(Game game) {
        super();
        this.game = game;
    }
    
    public void computeScores(){
        ArrayList<Player> players = game.getPlayers();
        for(int i =0;i<players.size();i++){
            Player p = players.get(i);
            int score = p.getScore();
            score+=(p.getNCells()-1)*Params.rPoints;
            p.setScore(score);
        }
    }
    
    public void paintComponent(Graphics g) {
        g.setColor(Color.black);
        ArrayList<Player> players = game.getPlayers();
        for(int i =0;i<players.size();i++){
            Player p = players.get(i);
            int score = p.getScore();
            int nCells = p.getNCells();
            g.drawString(nCells+"    "+score, 20, 40*(i+1));
        }
    }
}
