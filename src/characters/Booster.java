package characters;

import fap_java.Cell;
import fap_java.Game;
import fap_java.Params;
import fap_java.Player;
import fap_java.Team;

public class Booster extends Player{
    public Booster(int id, Cell c, Game game, Team t) {
        super(id, c, game, 9,t);
    }

    public void getSkill() {
        if (this.getGame().getThread().getCount() - this.getLastSkill() >= this.getSkillTime()) {
            this.setLastSkill(this.getGame().getThread().getCount());
            //System.out.println("*BANG*");
            //Send Skill :
            this.changeParam("dispSpeed", 2, 6*1000);
            //this.makeHimWait(Params.timeAfterSkill);
        }
    }
    }
