package characters;

import fap_java.Cell;
import fap_java.Game;
import fap_java.Player;

public class Admin extends Player{
    
    public Admin(int id, Cell c, Game game) {
        super(id, c, game, 0);
        this.setSkillTime(10);
    }

    public void getSkill() {
    }
}
