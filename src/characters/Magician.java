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
                        //TODO add animation Magician
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
        Image img = Graph.basePlayer.get("shadow");
        int width = (int)(img.getWidth(this.getGame()) * paintFactorW);
        int height = (int)(img.getHeight(this.getGame()) * paintFactorH);
        Point offset = new Point(-18, 4);
        g.drawImage(img, x + offset.x, y + offset.y, width, height, this.getGame());
        offset = new Point(-8, -7);

        switch (ori) {
        case 0:
        case 3:
            img = Graph.basePlayer.get(this.colorName + "diag0");
            offset = new Point(-8, -7);
            break;
        case 1:
        case 4:
            img = Graph.basePlayer.get(this.colorName + "diag1");
            offset = new Point(-8, -7);
            break;
        case 5:
            img = Graph.basePlayer.get(this.colorName + "left");
            offset = new Point(-8, -7);
            break;
        case 2:
            img = Graph.basePlayer.get(this.colorName + "right");
            offset = new Point(-8, -7);
            break;
        default:
            img = Graph.basePlayer.get(this.colorName + "right");
            offset = new Point(-8, -7);
            break;
        }

        g.drawImage(img, x + offset.x, y + offset.y, 26, 40, this.getGame());
    }
}
