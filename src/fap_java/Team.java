package fap_java;

import java.awt.Color;

import java.util.ArrayList;

public class Team {
    private ArrayList<Player> playersInThisTeam;
    private int nCells;
    private int score;
    public Team() {
        super();
        playersInThisTeam = new ArrayList<Player>();
        score = 0;
    }

    Color getColor() {
        return this.getFirstPlayer().getColor();
    }
    
    public void addPlayer(Player p){
        playersInThisTeam.add(p);
    }

    public Player getFirstPlayer(){
        return this.playersInThisTeam.get(0);
    }

    public void setPlayersInThisTeam(ArrayList<Player> playersInThisTeam) {
        this.playersInThisTeam = playersInThisTeam;
    }

    public ArrayList<Player> getPlayersInThisTeam() {
        return playersInThisTeam;
    }

    public void setNCells(int nCells) {
        this.nCells = nCells;
    }

    public int getNCells() {
        return nCells;
    }
    
    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
