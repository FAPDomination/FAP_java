package characters;

import fap_java.Arrow;
import fap_java.Cell;
import fap_java.Game;
import fap_java.Graph;
import fap_java.Params;
import fap_java.Player;
import fap_java.Team;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;


public class Archer extends Player {
    public Archer(int id, Cell c, Game game, Team t, int ai, int controler) {
        super(id, c, game, 5, t, ai, controler);
    }

    public void getSkill() {
        if (this.getGame().getThread().getCount() - this.getLastSkill() >= this.getSkillTime()) {
            this.setLastSkill(this.getGame().getThread().getCount());
            //System.out.println("*BANG*");
            //Send Skill :
            new Arrow(this.getCurrent(), this.getOri(), this.getGame(), this);
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
        Image img = Graph.characters.get("shadow");
        int width = (int)(img.getWidth(this.getGame()) * paintFactorW);
        int height = (int)(img.getHeight(this.getGame()) * paintFactorH);
        Point offset = new Point(-18, 4);
        g.drawImage(img, x + offset.x, y + offset.y, width, height, this.getGame());
        offset = new Point(-8, -7);

        switch (ori) {
        case 0:
        case 3:
            img = Graph.characters.get(this.colorName + "diag0");
            offset = new Point(-8, -7);
            break;
        case 1:
        case 4:
            img = Graph.characters.get(this.colorName + "diag1");
            offset = new Point(-8, -7);
            break;
        case 5:
            img = Graph.characters.get(this.colorName + "left");
            offset = new Point(-8, -7);
            break;
        case 2:
            img = Graph.characters.get(this.colorName + "right");
            offset = new Point(-8, -7);
            break;
        default:
            img = Graph.characters.get(this.colorName + "right");
            offset = new Point(-8, -7);
            break;
        }

        g.drawImage(img, x + offset.x, y + offset.y, 26, 40, this.getGame());
    }
}
