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

public class Knight extends Player{
    
    public Knight(int id, Cell c, Game game, Team t, int ai, int controler) {
        super(id, c, game, 1,t, ai, controler);
    }

    public void getSkill() {
        int ts = this.getGame().getThread().getCount() - this.getLastSkill();
        if (ts >= this.getSkillTime()) {
            
            //Send Skill :
                                    ArrayList<Cell> neighborHoodList = this.getGame().getMap().surroundingCells(this.getCurrent());
                                    for (int i =0; i<neighborHoodList.size();i++) {
                                            Cell c = neighborHoodList.get(i);
                                            if (c != null && c.getType()==1 && c.getOwner()!=this.getTeam()) {
                                                    // if he kills
                                                    /*
                                                    myMap[vi][vj] = stick.id;
                                                    healthPoints[1000*vi+vj] = new Array(stick.id, initHP);
                                                    */
                                                    //if he hurts
                                                    int dammage = (int)(Params.warriorDammage*Math.pow(ts,2));
                                                    //System.out.println(dammage);
                                                    c.setHp(c.getHp()-dammage);
                                                    if(c != null && c.getType() == 1 && ((c.getHp() <=0 && c.getOwner() != null) || (c.getOwner() == null && dammage>=90))){
                                                            c.setOwner(this.getTeam());
                                                            c.setHp(this.getInitHP());
                                                    }
                                            }
                                    }
                                    // Add animation
                                    //TODO add animation Knight
                                  //  attachAnimation("sKnight", stick._x, stick._y);
            
            this.setLastSkill(this.getGame().getThread().getCount());
            //He isn't blocked after throwing his skill
            //this.makeHimWait(Params.timeAfterSkill);
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
        Image img= Graph.basePlayer.get("shadow");
        int width = (int)(img.getWidth(this.getGame()) * paintFactorW);
        int height = (int)(img.getHeight(this.getGame()) * paintFactorH);
        Point offset = new Point(-18,4);
        g.drawImage(img, x +offset.x ,y+offset.y, width,height, this.getGame());
        offset = new Point(-8,-7);
        
        img = Graph.basePlayer.get("Knight"+ori+"_"+this.getColorName());
        g.drawImage(img, x +offset.x ,y+offset.y, 26, 40, this.getGame());
    }
}
