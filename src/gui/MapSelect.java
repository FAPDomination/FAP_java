package gui;

import fap_java.Game;
import fap_java.Params;
import fap_java.XMLparser;

import java.awt.BorderLayout;
import java.awt.Graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;

import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MapSelect extends FAPanel {
    private JButton btnNext = new JButton();
    private ArrayList<Minimap> mapList;
    private Minimap selectedMap;
    
    public MapSelect(TheFrame theFrame, JPanel jPanel) {
        super(theFrame, jPanel);
        
        swordX = minxS;
        cloudsX = minxC;
        
        this.setLayout(null);
        this.setSize(Constants.frameDimension);

        btnGoBack.setText("Retour");
        btnGoBack.setSize(120, 40);
        btnGoBack.setLocation(20, 20);
        
        btnNext.setText("Jouer");
        btnNext.setSize(120, 40);
        btnNext.setLocation(this.getWidth()-30-btnNext.getWidth(), 20);
        btnNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextFrame();
            }
        });
        
        this.add(btnGoBack);
        this.add(btnNext);
        this.validate();
        
        mapList = XMLparser.parseMapList();
        for(int i=0;i<mapList.size();i++){
            Minimap m = mapList.get(i);
            m.setPanel(this);
        }
        
        selectedMap = mapList.get(0);
        this.repaint();
    }
    
    public void nextFrame(){
        // Check if map is selected
        if(selectedMap != null){
            
            
            ArrayList<PlayerSelect> players = ((CharacterSelection)prevPanel).getPlayers();
            /*
            PlayerSelect[] psTable = new PlayerSelect[Params.maxPlayers];
            
            String whoIsPlaying = "";
            String wichTeam = "";
            String isFSM = "";
            
            for(int i=0;i<psTable.length;i++){
                PlayerSelect play = null;
                boolean playing = false;
                for(int j=0;j<players.size();j++){
                    play = players.get(j);
                    if(play.getControler() == i){
                        playing = true;
                        break;
                    }
                    else{
                        play = null;
                    }
                }
                if(playing){
                    System.out.println("yop");
                    
                    whoIsPlaying+=""+play.getPc()+",";
                    wichTeam+=""+play.getTeam()+",";
                    isFSM+=""+play.getIsFSM()+",";
                }
                else{
                    whoIsPlaying+="0,";
                    wichTeam+="0,";
                    isFSM+="0,";
                }
                //players.contains(ps)
                //PlayerSelect ps = players.get(i);
            }
            whoIsPlaying+="0";
            wichTeam+="0";
            isFSM+="0";
            // Proceeding to next panel
            System.out.println(whoIsPlaying+"-"+wichTeam+"-"+isFSM);
            Game game = new Game(whoIsPlaying,wichTeam,isFSM,true,selectedMap.getFileNumber());
            parent.changePanel(new LoadingScreen(parent,game,this));
            */
            Game game = new Game(players,true,selectedMap.getFileNumber());
            parent.changePanel(new LoadingScreen(parent,game,this));
        }
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        int nMapPerLine = 4;
        int origX = 50;
        int origY = 80;
        int h=0;
        int incrementX = (this.getWidth()-(2*origX))/nMapPerLine;
        int incrementY = 250;
        for(int i=0;i<mapList.size();i++){
            Minimap m = mapList.get(i);
            m.setX(origX+(i%nMapPerLine)*incrementX);
            m.setY(origY+h*incrementY);
            m.paintComponent(g);
            if(i>1 && (i+1)%(nMapPerLine) == 0){
                h++;
            }
        }
    }
}
