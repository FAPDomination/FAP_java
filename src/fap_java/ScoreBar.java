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
        totalTakable = game.getMap().getNTakableCells().size();
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
        //g.drawString(""+totalTakable, 20, 10);
        int x=0;
        int notTaken = computeNotTaken();
        ArrayList<Player> players = game.getPlayers();
        int nPlayers = players.size();
        
        for(int i =0;i<players.size();i++){
            Player p = players.get(i);
            int score = p.getScore();
            int nCells = p.getNCells();

            double percentCell = ((double)nCells)/totalTakable;
            g.setColor(p.getColor());
            g.fillRect(x, 0, (int)(game.getRWidth()*(double)percentCell), 30);
            g.setColor(Color.white);
            g.drawString(""+score, x+5, 10);
            x += (int)(game.getRWidth()*(double)percentCell);

            
            
            if(i == (nPlayers/2-1) && notTaken >=1){
                 percentCell = ((double)notTaken)/totalTakable;
                g.setColor(Color.GRAY);
                g.fillRect(x, 0, (int)(game.getRWidth()*(double)percentCell), 30);
                x += (int)(game.getRWidth()*(double)percentCell);
                g.setColor(Color.white);
                g.drawString(""+notTaken, x, 10);
            }
        }
    }
    
    public int computeNotTaken(){
        int notTaken = totalTakable;
            ArrayList<Player> players = game.getPlayers();
            for(int j =0;j<players.size();j++){
                Player p = players.get(j);
                notTaken-=p.getNCells();
            }
        return notTaken;
    }
}
