package characters;

import fap_java.Cell;
import fap_java.Game;
import fap_java.Player;
import fap_java.Team;

public class NoCharacter extends Player{
    public NoCharacter(int id, Cell c, Game game, Team t) {
        super(id, c, game, 7, t);
    }

    public void getSkill() {
        //No character, no skill
    }
}