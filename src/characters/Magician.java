package characters;

import fap_java.Cell;
import fap_java.Game;
import fap_java.Player;

public class Magician extends Player{
    public Magician(int id, Cell c, Game game) {
        super(id, c, game, 8);
    }

    public void getSkill() {
        System.out.println("*BANG*");
        if (this.getGame().getThread().getCount() - this.getLastSkill() >= this.getSkillTime()) {
            this.setLastSkill(this.getGame().getThread().getCount());
            
            //Send Skill :
        }
    }
    }
