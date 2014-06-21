package characters;

import fap_java.Cell;
import fap_java.Game;
import fap_java.Graph;
import fap_java.Params;
import fap_java.Player;
import fap_java.Team;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import java.util.ArrayList;
import java.util.Map;

public class Vampire extends Player{
    public Vampire(int id, Cell c, Game game, Team t, int ai, int controler) {
        super(id, c, game, 6,t, ai, controler);
    }

    public void getSkill() {
        if (this.getGame().getThread().getCount() - this.getLastSkill() >= this.getSkillTime()) {
            this.setLastSkill(this.getGame().getThread().getCount());
            //System.out.println("*BANG*");
            //Send Skill :
            //Find the cells he owns
            ArrayList<Cell> ownArr = new ArrayList<Cell>();
            ArrayList<Cell> cellsMap = this.getGame().getMap().getTakableCells();
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
            //TODO add animation Vampire
            
            this.makeHimWait(Params.timeAfterSkill);
        }
    }
    
        public void paintStick(Graphics g, int x, int y) {
            /*
            g.setColor(color);
            // Switch on ori
            g.fillRect(x, y, 10, 30);
            */
            // Game paint factor
            double paintFactorW = Graph.facW;
            double paintFactorH = Graph.facH;
            // Paint shadow
            Image img= Graph.characters.get("shadow");
            int width = (int)(img.getWidth(this.getGame()) * paintFactorW);
            int height = (int)(img.getHeight(this.getGame()) * paintFactorH);
            Point offset = new Point(-18,4);
            g.drawImage(img, x +offset.x ,y+offset.y, width,height, this.getGame());
            offset = new Point(-8,-7);
            
            switch(ori){
                case 0:
                case 3:
                    img= Graph.characters.get(this.colorName+"diag0");
                    offset = new Point(-8,-7);
                break;
                case 1:
                case 4:
                    img= Graph.characters.get(this.colorName+"diag1");
                    offset = new Point(-8,-7);
                break;
                case 5:
                    img= Graph.characters.get(this.colorName+"left");
                    offset = new Point(-8,-7);
                break;
                case 2:
                    img= Graph.characters.get(this.colorName+"right");
                    offset = new Point(-8,-7);
                break;
                default:
                    img= Graph.characters.get(this.colorName+"right");
                    offset = new Point(-8,-7);
                    break;
            }
            
            g.drawImage(img, x +offset.x ,y+offset.y, 26, 40, this.getGame());
        }
    }
