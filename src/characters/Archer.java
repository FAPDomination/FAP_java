package characters;

import fap_java.Arrow;
import fap_java.Cell;
import fap_java.Game;
import fap_java.Params;
import fap_java.Player;
import fap_java.Team;

public class Archer extends Player{
    public Archer(int id, Cell c, Game game, Team t, boolean ai) {
        super(id, c, game, 5,t, ai);
    }

    public void getSkill() {
        if (this.getGame().getThread().getCount() - this.getLastSkill() >= this.getSkillTime()) {
            this.setLastSkill(this.getGame().getThread().getCount());
            //System.out.println("*BANG*");
            //Send Skill :
            Arrow arr = new Arrow(this.getCurrent(),this.getOri(),this.getGame(),this);
            
            this.makeHimWait(Params.timeAfterSkill);
        }
    }
}
