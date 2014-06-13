package characters;

import animations.AnimFrozen;
import animations.AnimWarp;
import animations.Animation;

import fap_java.CMap;
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

public class Magician extends Player {
    public Magician(int id, Cell c, Game game, Team t, int ai, int controler) {
        super(id, c, game, 8, t, ai, controler);
    }

    public void getSkill() {
        if (this.getGame().getThread().getCount() - this.getLastSkill() >= this.getSkillTime()) {
            this.setLastSkill(this.getGame().getThread().getCount());
            //System.out.println("*BANG*");
            //Send Skill :
            int nRings = Params.howManyRingsIstheMagicianActive;
            Map<Integer, ArrayList<Cell>> ringsOfCells =
                this.getGame().getMap().ringsSurrounding(this.getCurrent(), nRings);
            for (int i = 0; i <= nRings; i++) {
                ArrayList<Cell> theRing = ringsOfCells.get(i);
                for (int j = 0; j < theRing.size(); j++) {
                    Cell c = theRing.get(j);
                    //c.setOwner(this.getTeam());
                    //c.setHp(this.getInitHP());
                    Player p = this.getGame().isOccupied(c);
                    if (p != null && p.getTeam() != this.getTeam()) {
                        //p.blast(10);
                        p.makeHimWait(Params.howLongBlockingMagician * 1000);
                        //Add animation
                        int x = CMap.giveTalePosition(p.getCurrent().getI(), p.getCurrent().getJ())[0] + Params.OFFX;
                        int y = CMap.giveTalePosition(p.getCurrent().getI(), p.getCurrent().getJ())[1] + Params.OFFY;
                        Animation anim = new AnimFrozen(x,y,this.getGame().getThread());
                    }
                }
            }
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
        offset = new Point(-24,-31);
        
        img = Graph.characters.get("Magician"+ori+"_"+this.getColorName());
        g.drawImage(img, x +offset.x ,y+offset.y, 58,90, this.getGame());
    }
}
