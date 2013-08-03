package characters;

import fap_java.Cell;
import fap_java.Game;
import fap_java.Params;
import fap_java.Player;
import fap_java.Team;

public class Miner extends Player{
    public Miner(int id, Cell c, Game game, Team t, boolean ai) {
        super(id, c, game, 3,t, ai);
    }

        public void getSkill() {
            if (this.getGame().getThread().getCount() - this.getLastSkill() >= this.getSkillTime()) {
                this.setLastSkill(this.getGame().getThread().getCount());
                //System.out.println("*BANG*");
                //Send Skill :
                
                this.makeHimWait(Params.timeAfterSkill);
            }
        }
        }
