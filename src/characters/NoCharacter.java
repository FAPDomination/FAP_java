package characters;

import fap_java.Cell;
import fap_java.Game;
import fap_java.Player;
import fap_java.Team;

public class NoCharacter extends Player{
    public NoCharacter(int id, Cell c, Game game, Team t, boolean ai) {
        super(id, c, game, 7, t, ai);
    }

    public void getSkill() {
        //No character, no skill
    }
}