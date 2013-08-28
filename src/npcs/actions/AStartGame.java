package npcs.actions;

import fap_java.Game;

import gui.Fapplication;
import gui.TheFrame;

public class AStartGame implements Action {
    private Game gameToLaunch;
    public AStartGame(Game gameToLaunch) {
        this.gameToLaunch = gameToLaunch;
    }

    public void execute() {
        TheFrame frame = (TheFrame)Fapplication.getFrame();
        frame.changePanel(gameToLaunch);
        // add Animation
    }
}
