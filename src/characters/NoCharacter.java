package characters;

import fap_java.Cell;
import fap_java.Game;
import fap_java.Player;

public class NoCharacter extends Player{
    public NoCharacter(int id, Cell c, Game game) {
        super(id, c, game, 7);
    }

    public void getSkill() {
        //No character, no skill
    }
}