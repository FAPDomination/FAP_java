package characters;


import animations.AnimBooster;

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


public class Booster extends Player {
    public Booster(int id, Cell c, Game game, Team t, int ai, int controler) {
        super(id, c, game, 9, t, ai, controler);
    }

    public void getSkill() {
        if (this.getGame().getThread().getCount() - this.getLastSkill() >= this.getSkillTime()) {
            this.setLastSkill(this.getGame().getThread().getCount());
            //Send Skill :
            this.changeParam("dispSpeed", Params.boosterSpeed, Params.boosterTime * 1000);
            //Add animation
            int x = CMap.giveTalePosition(this.getCurrent().getI(), this.getCurrent().getJ())[0] + Params.OFFX;
            int y = CMap.giveTalePosition(this.getCurrent().getI(), this.getCurrent().getJ())[1] + Params.OFFY;
            new AnimBooster(x, y, this.getGame().getThread());
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
        offset = new Point(-24, -31);

        img = Graph.characters.get("Booster" + ori + "_" + this.getColorName());
        g.drawImage(img, x + offset.x, y + offset.y, 58, 90, this.getGame());
    }
}
