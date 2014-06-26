package npcs.actions;

import fap_java.Game;

import fap_java.GameConfig;
import fap_java.NPC;

import gui.CharacterSelection;
import gui.Fapplication;
import gui.TheFrame;

import java.io.Serializable;

import java.util.ArrayList;

import javax.swing.JPanel;

public class AStartGame implements Action, Serializable {
    private GameConfig gameToLaunch;
    public AStartGame(GameConfig gameToLaunch) {
        this.gameToLaunch = gameToLaunch;
    }
    
    public AStartGame(){
        gameToLaunch = null;
    }

    public void execute(NPC whoLaunches) {
        TheFrame frame = (TheFrame)Fapplication.getFrame();
        JPanel panel = null;
        Game game = Fapplication.getWorldMap();
        if(gameToLaunch !=null){
            game = gameToLaunch.createGame();
        }
        if(game.getAdv() == 2){
            panel = game;
            //gameToLaunch.getThread().setRunning(true);
            if(!game.getThread().getRunning()){
                game.pauseGame(true);
            }
            game.setPauseNPC(false);
        }
        else{
            panel = new CharacterSelection(frame,Fapplication.getWorldMap(),game);
        }
        frame.changePanel(panel);
       // if(whoLaunches != null && whoLaunches.getIterator() <= whoLaunches.getActions().size()){
       //     whoLaunches.execute();
       // }
        // add Animation
        //TODO add Animation for panel changing
    }

    public void reinit() {
    }
}
