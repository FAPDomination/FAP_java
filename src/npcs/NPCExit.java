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

import npcs.actions.AAsk;
import npcs.actions.AStartGame;
import npcs.actions.Action;

public class NPCExit extends NPC {
    public NPCExit(Cell cell, Game game) {
        super(cell, true, false, null, game, 0,0,new AAsk("Retour a la carte du monde ?","Oui","Non",null,null));
    }
    public void paintComponent(Graphics g){}
    
    public void execute(){
        if(game.getThread().getRunning()){
            game.pauseGame(true);
        }
        if(iterator < actions.size()){
            Action ac = actions.get(iterator);
            iterator ++;
            if(ac != null){
                ac.execute(this);
            }
            else{
                new AStartGame().execute(this);
                iterator ++;
            }
            //System.out.println(this+" is executing");
        }
        else{
            
            game.pauseGame(true);
            game.setPauseNPC(false);
            this.reInit();
        }
    }
}
