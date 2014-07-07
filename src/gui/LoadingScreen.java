package gui;

import fap_java.Game;
import fap_java.Graph;
import fap_java.Player;
import fap_java.Team;

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
        btnGoBack.setSize(120,60);
        btnGoBack.setLocation(origX-5, origY-5);
        this.add(btnGoBack);
        
        
        btnPlay.setText("Play");
        btnPlay.setSize(120, 60);
        btnPlay.setLocation(630, 400);
        btnPlay.setUI(new Button_SampleUI());
        ((Button_SampleUI)btnPlay.getUI()).setHover(false);
        btnPlay.setOpaque(false);
        this.add(btnPlay);
        
        origY = 130;
        
        minimap = new Minimap(nmap,610,origY+5,this,"");
        //LS_BGteam : 322,103
        btnPlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame();
                ((Button_SampleUI)btnPlay.getUI()).setHover(false);
            }
        });
        
        btnPlay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                //LectureFichierSon.lire(Design.sonChtk);
                ((Button_SampleUI)btnPlay.getUI()).setHover(true);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                ((Button_SampleUI)btnPlay.getUI()).setHover(false);
            }
        });
    }
    
    public void startGame(){
        game.pauseGame();
        parent.changePanel(game);
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
            int x;
            if(teams.size()>triggerNewDispo){
                y = (origY+(i%nPerCol)*incremY);
                x = origX+col*increm8X;
                int w = 161;
                int h = 102;
                int squareW = 20;
                Graph.drawDarkBackgroundRectangle(g, x, y, w, h, teams.get(i).getFirstPlayer().getColor(), squareW, Graph.BG_DARK);
                //g.drawImage(Graph.getGuimg().get("LS_BG8team"), origX+col*increm8X, y, this);
            }
            else{
                y = (origY+(i)*incremY);
                x = origX;
                
                int w = 322;
                int h = 103;
                int squareW = 20;
                Graph.drawDarkBackgroundRectangle(g, x, y, w, h, teams.get(i).getFirstPlayer().getColor(), squareW, Graph.BG_DARK);
                //g.drawImage(Graph.getGuimg().get("LS_BGteam"), origX, y, this);
            }
            g.setColor(Color.black);
            //TODO pick one or the other
            //TODO nice text
            //g.drawString("Team "+(i+1), origX+20+increm8X*col, y+30);
            g.setFont(Graph.REGULAR_FONT);
            Graph.drawBorderedString(g, origX+20+increm8X*col, y+30, ""+teams.get(i), Graph.MENU_TEXT_BORDER_TRANSLUSCENT);
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
