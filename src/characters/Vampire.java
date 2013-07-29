package characters;

import fap_java.Cell;
import fap_java.Game;
import fap_java.Params;
import fap_java.Player;
import fap_java.Team;

import java.util.ArrayList;
import java.util.Map;

public class Vampire extends Player{
    public Vampire(int id, Cell c, Game game, Team t) {
        super(id, c, game, 6,t);
    }

    public void getSkill() {
        if (this.getGame().getThread().getCount() - this.getLastSkill() >= this.getSkillTime()) {
            this.setLastSkill(this.getGame().getThread().getCount());
            System.out.println("*BANG*");
            //Send Skill :
            //Find the cells he owns
            ArrayList<Cell> ownArr = new ArrayList<Cell>();
            ArrayList<Cell> cellsMap = this.getGame().getMap().getMyMap();
            for(int i=0;i<cellsMap.size();i++){
                Cell c = cellsMap.get(i);
                if(c!=null && c.getOwner() == this.getTeam()){
                    ownArr.add(c);
                }
            }
            
            //Find ennemy around
            int nRings = Params.ringsVampirismTakes;
            int amount=0; //Of gained HPs;
            Map<Integer, ArrayList<Cell>> ringsOfCells = this.getGame().getMap().ringsSurrounding(this.getCurrent(), nRings);
            for(int i = 0;i<=nRings;i++){
                ArrayList<Cell> theRing = ringsOfCells.get(i);
                for(int j=0;j<theRing.size();j++){
                    Cell c = theRing.get(j);
                    if(c.getType() == 1 && c.getOwner()!=null && c.getOwner() != this.getTeam()){
                        int drainedLife = (int)(Math.pow((nRings-i), 2.0) + 3);
                        c.setHp(c.getHp()-drainedLife);
                        amount += drainedLife;
                    }
                }
            }
            int nCells = ownArr.size();
            int amountProCell = (int)(((double)amount/nCells)*Params.rateVampirismGains);
            for(int i=0;i<ownArr.size();i++){
                Cell c = ownArr.get(i);
                c.setHp(c.getHp()+amountProCell);
            }
        }
    }
    }
