package npcs.actions;

import fap_java.Game;
import fap_java.GameConfig;
import fap_java.NPC;

import gui.CharacterSelection;
import gui.Fapplication;
import gui.TheFrame;

import javax.swing.JPanel;


public class AStartGame extends Action {
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = -6097939611199271001L;
    private GameConfig gameToLaunch;

    /**
     * Creates an NPC action that will start a game according to the given parameters.
     * @param gameToLaunch the parameters for starting the game
     */
    public AStartGame(GameConfig gameToLaunch) {
        super(null);
        this.gameToLaunch = gameToLaunch;

    }

    /**
     * Creates an NPC action that will start the worldMap.
     */
    public AStartGame() {
        this(null);
    }

    public void execute(NPC whoLaunches) {
        // Get the frame for this game
        TheFrame frame = (TheFrame)Fapplication.getFrame();
        JPanel panel = null;
        Game game = Fapplication.getWorldMap();
        if (gameToLaunch != null) {
            game = gameToLaunch.createGame();
        }

        if (game.getAdv() == 2) {
            panel = game;
            //gameToLaunch.getThread().setRunning(true);
            if (!game.getThread().getRunning()) {
                game.pauseGame(true);
            }
            game.setPauseNPC(false);
        } else {
            panel = new CharacterSelection(frame, Fapplication.getWorldMap(), game);
        }
        // CHange the panel
        frame.changePanel(panel);
        // add Animation
        //TODO add Animation for panel changing
    }

    public void setTransientValues(Game g) {
    }

}
