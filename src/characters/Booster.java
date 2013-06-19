package characters;

import fap_java.Cell;
import fap_java.Game;
import fap_java.Player;

public class Booster extends Player{
    public Booster(int id, Cell c, Game game) {
        super(id, c, game, 9);
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
