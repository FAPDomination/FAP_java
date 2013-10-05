package gui;

import animations.NPCMessage;
import animations.PauseCountDown;

import fapLan.Client;

import fapLan.LListener;

import fap_java.Game;

import fap_java.KListener;

import java.awt.Color;

import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.event.KeyListener;

import java.io.Serializable;

import javax.swing.JPanel;

public class Displayer extends JPanel implements NeedingFocus,Serializable {
    private Game game;
    private boolean lanMode;
    private Client client;
    /**
     * The Key Listener that will handle player displacements and pause
     */
    private transient KeyListener kl;
    
    public Displayer(Game game, boolean lanMode, Client client) {
        super();
        this.lanMode = lanMode;
        this.client = client;
        // Panel related stuff 
        this.setLayout(null);
        this.setBackground(Color.white);
        this.setSize(Constants.frameDimension);
        
        this.game = game;
    }
    
    public Displayer(Game game){
        this(game,false,null);
    }
    
    /**
     * Paint the panel. takes care of background, map, players and other components
     * @param g Graphical thing that no one really understands
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Background
        Graphics2D g2d = (Graphics2D)g;
        
        int w = this.getWidth();
        int h = this.getHeight();

        // Paint a gradient from top to bottom
        GradientPaint gp = new GradientPaint(0, 0, Constants.top, 0, h, Constants.bottom);

        g2d.setPaint(gp);
        //TODO Better background ??
        g2d.fillRect(0, 0, w, h);
        // --- End BG
        
        // Repaint the map
        game.getMap().paintComponent(g);
        if(game.getAdv() <2){
            // Repaint the scoreHandler
            this.game.getScoreHandler().paintComponent(g);
        }
        // Repaint the objects (such as arrows)
        for(int j=0;j<game.getObjects().size();j++){
            game.getObjects().get(j).paintComponent(g);
        }
        
        /*
        if(map.getFileID() == 0){
            // if world map, paint the npcs with green line
            for(int i=0;i<listNPCs.size(); i++){
                NPC npc = listNPCs.get(i);
                if(npc instanceof NPCWMStarting){
                    npc.paintComponent(g);
                }
            }
        }*/
        
        /*
        // Paint black screen if the game is paused
        if(!thread.getRunning() && !pauseNPC){
            g.drawImage(Graph.guimg.get("pauseScreen"), 0, 0,this.getWidth(),this.getHeight(), this);
        }
        */
        // Paint the animations (warps, explosions, bitches,...)
        for(int j=0;j<game.getAnims().size();j++){
            if(game.getThread().getRunning() || game.getAnims().get(j) instanceof PauseCountDown || game.getAnims().get(j) instanceof NPCMessage){
                game.getAnims().get(j).paintComponent(g);
            }
        }
    }
    
    public int getRWidth() {
        return this.getWidth();
    }
    
    /**
     * Initializes the Key Listener of the game
     */
    private void initKListener() {
        if(!lanMode){
            kl = new KListener(game);
        }
        else{
            kl = new LListener(this);
        }
        this.addKeyListener(kl);
    }
    
    private void deleteKListener(){
        if(kl != null){
            this.removeKeyListener(kl);
            kl = new KListener(game);
        }
    }
    
    /**
     * Request the focus so that the K listener works
     */
    public void initFocus() {
        //System.out.println("initing focus");
        if(!lanMode){
            game.initListNPCs(game.getMap().getFileID());
        }
        initKListener(); 
        this.setFocusable(true);
        requestFocus();
    }
    
    public void releaseFocus(){
        if(!lanMode){
        game.getThread().setRunning(false);
        }
        deleteKListener();
        this.setFocusable(false);
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public void setKl(KeyListener kl) {
        this.kl = kl;
    }

    public KeyListener getKl() {
        return kl;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }
}
