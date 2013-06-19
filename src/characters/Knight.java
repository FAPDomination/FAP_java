package characters;

import fap_java.Cell;
import fap_java.Game;
import fap_java.Player;

public class Knight extends Player{
    
    public Knight(int id, Cell c, Game game) {
        super(id, c, game, 1);
        this.setSkillTime(300);
    }

    public void getSkill() {
        if (this.getGame().getThread().getCount() - this.getLastSkill() >= this.getSkillTime()) {
            this.setLastSkill(this.getGame().getThread().getCount());
            System.out.println("*BANG*");
            //Send Skill :
            
            
        }
    }
}
