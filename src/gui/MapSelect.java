package gui;

import fap_java.Game;
import fap_java.Params;
import fap_java.XMLparser;

import java.awt.BorderLayout;
import java.awt.Graphics;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.ArrayList;

import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MapSelect extends FAPanel implements MouseListener{
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
        
        selectedMap = null;
        this.btnNext.setEnabled(false);
        this.addMouseListener(this);
        this.repaint();
    }
    
    public void nextFrame(){
        // Check if map is selected
        if(selectedMap != null){
            
            ArrayList<PlayerSelect> players = ((CharacterSelection)prevPanel).getPlayers();

            Game game = new Game(players,true,selectedMap.getFileNumber(),Params.defaultVictoryScore,0,0,0);
            parent.changePanel(new LoadingScreen(parent,game,this,selectedMap.getFileNumber()));
        }
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        int nMapPerLine = 4;
        origY = 100;
        int h=0;
        //TODO fit to width
        int incrementX = (this.getWidth()-(2*origX))/nMapPerLine;
        int incrementY = 270;
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
    
    private Minimap whoIsClicked(Point p){
        Minimap m = null;
        for(int i=0;i<this.mapList.size();i++){
            Minimap k = mapList.get(i);
            if(k.inArea(p)){
                m = k;
                break;
            }
        }
        return m;
    }

    public void mouseClicked(MouseEvent e) {
        Point p = e.getPoint();     // Where is the click
        Minimap m = whoIsClicked(p);
        if(m!=null){
            if(selectedMap != null){
                selectedMap.setIsSelected(false);
            }
            else{
                this.btnNext.setEnabled(true);
            }
            selectedMap = m;
            selectedMap.setIsSelected(true);
            this.repaint();
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
