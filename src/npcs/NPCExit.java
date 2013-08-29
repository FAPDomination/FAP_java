package npcs;

import fap_java.Cell;
import fap_java.Game;
import fap_java.NPC;

import gui.CharacterSelection;
import gui.Fapplication;

import gui.TheFrame;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import java.util.ArrayList;

import javax.swing.JPanel;

import npcs.actions.AStartGame;
import npcs.actions.Action;

public class NPCExit extends NPC {
    boolean done;
    public NPCExit(Cell cell, Game game) {
        super(cell, true, false, null, game, 0,0,new ArrayList<Action>());
        done = false;
    }
    public void paintComponent(Graphics g){}
    
    public void execute(){
        if(!done){
            done = true;
            new AStartGame(Fapplication.getWorldMap()).execute(this);
        }
    }
    
    public void reInit(){
        done = false;   
    }
}
