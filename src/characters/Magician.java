package characters;

import fap_java.Cell;
import fap_java.Game;
import fap_java.Params;
import fap_java.Player;
import fap_java.Team;

import java.util.ArrayList;
import java.util.Map;

public class Magician extends Player{
    public Magician(int id, Cell c, Game game, Team t, int ai, int controler) {
        super(id, c, game, 8,t, ai, controler);
    }

        public void getSkill() {
            if (this.getGame().getThread().getCount() - this.getLastSkill() >= this.getSkillTime()) {
                this.setLastSkill(this.getGame().getThread().getCount());
                //System.out.println("*BANG*");
                //Send Skill :
                int nRings = Params.howManyRingsIstheMagicianActive;
                Map<Integer, ArrayList<Cell>> ringsOfCells = this.getGame().getMap().ringsSurrounding(this.getCurrent(), nRings);
                for(int i = 0;i<=nRings;i++){
                    ArrayList<Cell> theRing = ringsOfCells.get(i);
                    for(int j=0;j<theRing.size();j++){
                        Cell c = theRing.get(j);
                        //c.setOwner(this.getTeam());
                        //c.setHp(this.getInitHP());
                        Player p = this.getGame().isOccupied(c);
                        if(p != null && p.getTeam()!=this.getTeam()){
                            //p.blast(10);
                            p.makeHimWait(Params.howLongBlockingMagician*1000);
                            //Add animation
                        }
                    }
                }
                this.makeHimWait(Params.timeAfterSkill);
            }
        }
        }
