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
    @SuppressWarnings("compatibility:-9179486704078853616")
    private static final long serialVersionUID = 746096623204554497L;

    public NPCExit(Cell cell, Game game) {
        super(cell, true, false, null, game, 0,0,null);
        
        Action sg = new AStartGame();
        firstAction = new AAsk("Retour a la carte du monde ?","Oui","Non",null,sg);
        currentAction = firstAction;
    }
    public void paintComponent(Graphics g){}
    /*
    public void execute(){
        if(game.getThread().getRunning()){
            game.pauseGame(true);
        }
        if (currentAction != null) {
            iterator++;
            currentAction.execute(this);
            //System.out.println(this+" is executing");
        }
        else{
            new AStartGame().execute(this);
            iterator ++;
            game.pauseGame(true);
            game.setPauseNPC(false);
            this.reInit();
        }
    }
*/
}