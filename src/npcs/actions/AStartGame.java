package npcs.actions;

import fap_java.Game;

import gui.CharacterSelection;
import gui.Fapplication;
import gui.TheFrame;

import java.util.ArrayList;

import javax.swing.JPanel;

public class AStartGame implements Action {
    private Game gameToLaunch;
    public AStartGame(Game gameToLaunch) {
        this.gameToLaunch = gameToLaunch;
    }

    public void execute() {
        TheFrame frame = (TheFrame)Fapplication.getFrame();
        JPanel panel = null;
        if(gameToLaunch.getAdv() == 2){
            panel = gameToLaunch;
            gameToLaunch.getThread().setRunning(true);
        }
        else{
            panel = new CharacterSelection(frame,Fapplication.getWorldMap(),gameToLaunch);
        }
        frame.changePanel(panel);
        // add Animation
    }
}
