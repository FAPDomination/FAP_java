package npcs;

import fap_java.CMap;
import fap_java.Cell;
import fap_java.Game;
import fap_java.GameConfig;
import fap_java.Graph;
import fap_java.NPC;

import fap_java.Params;
import fap_java.Tools;

import gui.GameSave;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

import java.util.Set;

import javax.imageio.ImageIO;

import npcs.actions.AAsk;
import npcs.actions.AStartGame;
import npcs.actions.Action;

public class NPCWMStarting extends NPC {
    @SuppressWarnings("compatibility:-9003127236683247915")
    private static final long serialVersionUID = -441251255864270036L;
    private boolean conquered;
    private static int offY = 5;
    private static int[][] points =
    { { CMap.TW / 4, 3 * CMap.TH / 4 + offY }, { 3 * CMap.TW / 4, 2 * CMap.TH / 4 + offY },
      { 5 * CMap.TW / 4 , 3 * CMap.TH / 4 + offY + 1 }, { 5 * CMap.TW / 4 , 6 * CMap.TH / 4 + offY + 1 },
      { 3 * CMap.TW / 4, 7 * CMap.TH / 4 + offY + 2 }, { CMap.TW / 4, 6 * CMap.TH / 4 + offY } };

    public NPCWMStarting(Cell pos, boolean conquered, Game game, GameConfig gameToLaunch) {
        super(pos, true, false, null, game, Graph.getOffsetsCells().get(0), null);
        this.conquered = conquered;
        
        Action sg = new AStartGame(gameToLaunch);
        firstAction = new AAsk("Lancer la partie ?", "Oui", "Non", null, sg);
        currentAction = firstAction;
    }

    public void paintComponent(Graphics g) {
        //Tools.drawFilteredImage((BufferedImage)img, 1f,0.4f,1f, (float)0.5, g,x,y);
        g.setColor(Color.GREEN);

        //TODO better greenZone
        //TODO greenZone for cities ? (Make them automatically conquered)
        ArrayList<Cell> surrounding = game.getMap().surroundingCells(this.getPosition());
        if (conquered) {
            for (int i = 0; i < surrounding.size(); i++) {
                Cell c = surrounding.get(i);
                boolean draw = false;
                if (c == null) {
                    draw = true;
                }
                else{
                    NPC npc = Tools.checkNPCOnCell(game, c);
                    if(npc !=null && npc instanceof NPCWMStarting){
                        boolean value = ((NPCWMStarting)npc).isConquered();
                        if(value){
                            draw = false;   
                        }
                        else{
                            draw = true;
                        }
                    }
                    else{
                        draw = true;
                    }
                }
                
                if (draw) {
                    g.drawLine(x + points[i][0], y + points[i][1], x + points[(i + 1) % 6][0],
                               y + points[(i + 1) % 6][1]);
                }
            }
        }
    }

    public void setConquered(boolean conquered) {
        this.conquered = conquered;
    }

    public boolean isConquered() {
        return conquered;
    }

    public String toString() {
        return super.toString() + " strt";
    }

}
