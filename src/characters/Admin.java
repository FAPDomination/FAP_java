package characters;

import fap_java.Cell;
import fap_java.Game;
import fap_java.Player;
import fap_java.Team;

public class Admin extends Player{
    
    public Admin(int id, Cell c, Game game, Team t, boolean ai) {
        super(id, c, game, 0,t, ai);
    }

    public void getSkill() {
    }
}
