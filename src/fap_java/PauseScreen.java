package fap_java;

import gui.Constants;
import gui.Fapplication;
import gui.MainMenu;

import gui.TheFrame;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.KeyEvent;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

// Also does victory screen
public class PauseScreen extends Element{
    private boolean displayVictory;
    private boolean resuming;
    private Game game;
    
    private String message;
    private Team winner;
    private boolean advMode;
    
    // Buttons
    private JButton btnResume = new JButton();
    private JButton btnWorldMap = new JButton();
    private JButton btnMainMenu = new JButton();
    
    
    public PauseScreen(boolean dispVict, Game game) {
        super();
        displayVictory = dispVict;
        this.game = game;
        resuming = false;
        message="";
        
        // Init buttons
        btnResume.setText("Continuer");
        btnResume.setSize(Constants.buttonSize);
        
        
        btnWorldMap.setText("Retour à la carte");
        btnWorldMap.setSize(Constants.buttonSize);
        
        btnMainMenu.setText("Retour au menu");
        btnMainMenu.setSize(Constants.buttonSize);
        
        btnResume.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resumeGame();
            }
        });
        
        btnMainMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gotoMainMenu();
            }
        });
        
        btnWorldMap.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gotoWorldMap();
            }
        });
        
        computeButtons();
    }
    
    public void paintComponent(Graphics g) {
        g.setColor(Color.black);
        g.drawImage(Graph.guimg.get("pauseScreen"), 0, 0,game.getWidth(),game.getHeight(), game);
        this.computeButtons();
        if(!resuming){
            // Draw buttons
            //System.out.println("I'm drawin' I'm drawin' !");
            
            // Draw victory
            if(displayVictory){
                int width = 528;
                int height = 444;
                int x =(game.getWidth()-width)/2;
                int y =(game.getHeight()-height)/2;
                //TODO replace w/h with relative
                g.drawImage(Graph.guimg.get("victoryScreen"), x, y,width,height, game);
                
                Graphics2D g2d = (Graphics2D)g;
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(message);
                g.drawString(message, x+(width-textWidth)/2, y+20);
                
                if(winner != null){
                    ArrayList<Player> players = winner.getPlayersInThisTeam();
                    for(int j=0;j<players.size();j++){
                        //TODO player placement (center and space)
                        players.get(j).paintStick(g, x+20+j*30, y+100);
                    }
                }
            }
        }
    }

    public String toString() {
        return null;
    }

    public void setResuming(boolean resuming) {
        this.resuming = resuming;
        computeButtons();
    }

    public boolean isResuming() {
        return resuming;
    }

    public void setDisplayVictory(boolean displayVictory) {
        this.displayVictory = displayVictory;
        // Remove Button
        computeButtons();
    }

    public boolean isDisplayVictory() {
        return displayVictory;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
    }

    public Team getWinner() {
        return winner;
    }

    public void setAdvMode(boolean advMode) {
        this.advMode = advMode;
        computeButtons();
    }

    public boolean isAdvMode() {
        return advMode;
    }
    
    // Buttons management
    
    private void resumeGame() {
       game.pauseGame();
       game.releaseFocus();
       game.initFocus();
    }
    
    private void gotoMainMenu(){
        TheFrame frame = (TheFrame)Fapplication.getFrame();
        MainMenu panel = new MainMenu(frame,true);
        
        frame.changePanel(panel);
    }
    
    private void gotoWorldMap(){
        TheFrame frame = (TheFrame)Fapplication.getFrame();
        Game panel = Fapplication.getWorldMap();
        Fapplication.getWorldMap().pauseGame(true);
        frame.changePanel(panel);
    }
    
    private void computeButtons(){
        int offX = (int)(game.getWidth() - 0.1*Constants.buttonSize.getWidth());
        int offY = (int)(game.getHeight()- 1.3*Constants.buttonSize.getHeight());

        int incrementX = (int)(1.2*Constants.buttonSize.getWidth());
        //Remove button
        game.remove(this.btnMainMenu);
        game.remove(this.btnResume);
        game.remove(this.btnWorldMap);
        if(!resuming){
            int nbuttons=1;     // for gotoMainMenu
            if(this.advMode && game.getMap().getFileID() != 0){
                nbuttons++;     // for gotoWorldMap
            }
            if(!this.displayVictory){
                nbuttons++;     // for resume
            }
            
            // Buttons placement and adding
            int i=0;
            //Resume
            if(!this.displayVictory){
                btnResume.setLocation(offX-(i+1)*incrementX, offY);
                i++;
                game.add(btnResume);
            }
            //World map
            if(this.advMode && game.getMap().getFileID() != 0){
                btnWorldMap.setLocation(offX-(i+1)*incrementX, offY);
                i++;
                game.add(btnWorldMap);
            }
            // MainMenu
            btnMainMenu.setLocation(offX-(i+1)*incrementX, offY);
            game.add(btnMainMenu);
            i++;
        }
        
        //TODO compute sizes ?
    }
}
