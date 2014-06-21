package fap_java;

import java.awt.Color;
import java.awt.Graphics;

import java.io.Serializable;
import java.awt.Image;
import java.util.ArrayList;

public class ScoreBar implements Serializable{
    
    private int totalTakable;
    private Game game;
    private Image medalR;
    private Image medalL;
    private int heightBar=50;
    private int widthMedal;
    
    public ScoreBar(Game game) {
        super();
        this.game = game;
        totalTakable = game.getMap().getTakableCells().size();
        medalR = Graph.getGuimg().get("ScoreMedalR");
        medalL = Graph.getGuimg().get("ScoreMedalL");
        widthMedal = medalR.getWidth(game)*heightBar/medalR.getHeight(game);
    }
    
    public void computeScores(){
        ArrayList<Team> teams = game.getTeams();
        for(int i =0;i<teams.size();i++){
            Team te = teams.get(i);
            int score = te.getScore();
            score+=(te.getNCells()-1)*Params.rPoints;
            te.setScore(score);
        }
        //Check victory
        Team winner = game.testVictory();
        if(winner != null){
            game.endGame(winner);
        }
    }
    
    public void paintComponent(Graphics g) {
        g.setColor(Color.black);
        //g.drawString(""+totalTakable, 20, 10);
        int x=0;
        int notTaken = computeNotTaken();
        ArrayList<Team> teams = game.getTeams();
        int nTeams = teams.size();
        
        for(int i =0;i<teams.size();i++){
           Team te = teams.get(i);
            int nCells = te.getNCells();

            double percentCell = ((double)nCells)/totalTakable;
            g.setColor(te.getColor());
            g.fillRect(x, 0, (int)(game.getWidth()*(double)percentCell), heightBar);
            
            g.drawImage(medalR, x, 0, widthMedal,heightBar, game);
            g.drawImage(medalL, x+(int)(game.getWidth()*(double)percentCell)-widthMedal, 0, widthMedal,heightBar, game);
            x += (int)(game.getWidth()*(double)percentCell);

            
            
            if(i == (nTeams/2-1) && notTaken >=1){
                 percentCell = ((double)notTaken)/totalTakable;
                g.setColor(Color.GRAY);
                g.fillRect(x, 0, (int)(game.getWidth()*(double)percentCell), heightBar);
                x += (int)(game.getWidth()*(double)percentCell);
            }
            
            
        }
        
        //Display skillTimes
        if(!game.isGameEnded()){
            ArrayList<Player> players = game.getPlayers();
            for(int i=0;i<players.size();i++){
                Player p = players.get(i);
                g.setColor(p.getColor());
                int skillTime = (p.getSkillTime() - (game.getThread().getCount() - p.getLastSkill()));
                double skillLeft = 1-(((double)skillTime)/(Params.paramTable.get("skillTime")[p.getPc()]*1000));
                if(skillLeft<0){
                    skillLeft = 0;
                }
                else if(skillLeft > 1){
                    skillLeft=1;
                }
                g.fillRect(game.getWidth() - 130, i*30+100, (int)(skillLeft*100), 20);
                g.drawImage(Graph.guimg.get("skillTimeBar"), game.getWidth() - 130, i*30+100, game);
            }
        }
        
        // Draw shade
        g.setColor(new Color(0,0,0,30));
        g.fillRect(0,0,game.getWidth(),heightBar);
        g.drawImage(Graph.guimg.get("ScoreBottom"), 0, heightBar-20,game.getWidth(),20, game);
        
        // Draw score
        x=0;
        for(int i =0;i<teams.size();i++){
           Team te = teams.get(i);
            int score = te.getScore();
            int nCells = te.getNCells();

            double percentCell = ((double)nCells)/totalTakable;
            g.setColor(Color.white);
            g.drawString(""+score, x+5, 10);
            x += (int)(game.getWidth()*(double)percentCell);

            if(i == (nTeams/2-1) && notTaken >=1){
                 percentCell = ((double)notTaken)/totalTakable;
                x += (int)(game.getWidth()*(double)percentCell);
            }
            
            
        }
    }
    
    public int computeNotTaken(){
        int notTaken = totalTakable;
        ArrayList<Team> teams = game.getTeams();
            for(int j =0;j<teams.size();j++){
                Team te = teams.get(j);
                notTaken-= te.getNCells();
            }
        return notTaken;
    }
}
