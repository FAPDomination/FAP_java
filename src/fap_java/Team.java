package fap_java;

import java.awt.Color;
import java.util.ArrayList;

public class Team {
    /**
     * Stores the players who play in this team
     */
    private ArrayList<Player> playersInThisTeam;
    /**
     * The number of cells the team owns (ownz ownz ownz)
     */
    private int nCells;
    /**
     * The score of the team
     */
    private int score;
    /**
     * Will generate a new team with blank parameters that will be filled when the game will generate players
     */
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
    
    public String toString(){
        return "Team : "+getFirstPlayer().getColor()+"\n"+this.playersInThisTeam;
    }
}
