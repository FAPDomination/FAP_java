package characters;

import fap_java.Cell;
import fap_java.Game;
import fap_java.Graph;
import fap_java.Player;
import fap_java.Team;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

public class NoCharacter extends Player{
    public NoCharacter(int id, Cell c, Game game, Team t, int ai, int controler) {
        super(id, c, game, 7, t, ai, controler);
    }

    public void getSkill() {
        //No character, no skill
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
        offset = new Point(-10,-11);
        
        img = Graph.characters.get("NoCharacterGuy"+ori+"_"+this.getColorName());
        g.drawImage(img, x +offset.x ,y+offset.y, 29,45, this.getGame());
    }
}