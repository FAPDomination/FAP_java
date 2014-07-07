package npcs.actions;

import fap_java.Game;
import fap_java.NPC;


public class ASetVictory extends Action {

    @SuppressWarnings("compatibility:-2383668378599317269")
    private static final long serialVersionUID = -9074609463056578798L;

    /**
     * Creates an NPC action that will trigger victory on the current game
     */
    public ASetVictory() {
        super(null);
    }

    public void execute(NPC whoLaunches) {
        //Load datas
        Game game = whoLaunches.getGame();
        game.pauseGame(true);
        game.endGame(game.getTeams().get(0));

    }

    public void setTransientValues(Game g) {
    }
}
