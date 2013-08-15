package gui;

import fap_java.XMLparser;

import java.awt.Graphics;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MapSelect extends FAPanel {
    private JButton btnNext = new JButton();
    private ArrayList<Minimap> mapList;
    private int selectedMap;
    
    public MapSelect(TheFrame theFrame, JPanel jPanel) {
        super(theFrame, jPanel);
        
        swordX = minxS;
        cloudsX = minxC;
        
        this.setLayout(null);
        this.setSize(Constants.frameDimension);

        btnGoBack.setText("Retour");
        btnGoBack.setSize(120, 40);
        btnGoBack.setLocation(20, 20);
        
        btnNext.setText("Suivant");
        btnNext.setSize(120, 40);
        btnNext.setLocation(this.getWidth()-30-btnNext.getWidth(), 20);
        
        this.add(btnGoBack);
        this.add(btnNext);
        this.validate();

        selectedMap = 0;
        
        mapList = XMLparser.parseMapList();
        for(int i=0;i<mapList.size();i++){
            Minimap m = mapList.get(i);
            m.setPanel(this);
        }
        this.repaint();
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
