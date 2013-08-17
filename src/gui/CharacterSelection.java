package gui;

import fap_java.Params;
import fap_java.Tools;

import java.awt.Graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

public class CharacterSelection extends FAPanel {
    private JButton btnNext = new JButton();
    private ArrayList<CharacterDisplay> charList;
    private ArrayList<ArrowSelect> arrowList;
    private ArrayList<PlayerSelect> players;
    
    // Displaying
    private static int characDisplayOrigX = 0;
    private static int characDisplayIncrement = 100;
    
    public CharacterSelection(TheFrame theFrame, JPanel jPanel) {
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
        btnNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextFrame();
            }
        });
        
        this.add(btnGoBack);
        this.add(btnNext);
        this.validate();
        this.repaint();
        
        players = ((PlayerSelection)prevPanel).getPlayers();
        charList = new ArrayList<CharacterDisplay>();
        arrowList = new ArrayList<ArrowSelect>();
        
        int k=1;
        for(int i=1;i<10;i++){
            if(i != 2 && i != 7){
                charList.add(new CharacterDisplay(this.characDisplayOrigX+k*this.characDisplayIncrement,200,i,this));
                k++;
            }
        }
        
        for(int j=0;j<players.size();j++){
            PlayerSelect ps = players.get(j);
            if(ps.getIsFSM() == 0){
                ArrowSelect as = new ArrowSelect(ps,this);
                arrowList.add(as);
            }
        }
        
    }
    
    public void nextFrame(){
        // Set skills for FSMs :
        for(int i=0;i<players.size();i++){
            PlayerSelect ps = players.get(i);
            if(ps.getIsFSM() != 0){
                ps.setPc(Tools.randRange(1, 9, Params.excludedChars));
            }
        }
        // Proceeding to next panel
        JPanel nextPanel = new MapSelect(parent,this);
        parent.changePanel(nextPanel);
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        for(int i=0;i<charList.size();i++){
            charList.get(i).paintComponent(g);
        }
        for(int j=0;j<arrowList.size();j++){
            arrowList.get(j).paintComponent(g);
        }
    }

    public static void setCharacDisplayOrigX(int characDisplayOrigX) {
        CharacterSelection.characDisplayOrigX = characDisplayOrigX;
    }

    public static int getCharacDisplayOrigX() {
        return characDisplayOrigX;
    }

    public static void setCharacDisplayIncrement(int characDisplayIncrement) {
        CharacterSelection.characDisplayIncrement = characDisplayIncrement;
    }

    public static int getCharacDisplayIncrement() {
        return characDisplayIncrement;
    }

    public void setCharList(ArrayList<CharacterDisplay> charList) {
        this.charList = charList;
    }

    public ArrayList<CharacterDisplay> getCharList() {
        return charList;
    }

    public void setPlayers(ArrayList<PlayerSelect> players) {
        this.players = players;
    }

    public ArrayList<PlayerSelect> getPlayers() {
        return players;
    }
}
