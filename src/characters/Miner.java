package characters;


import animations.AnimWarp;
import animations.Animation;

import fap_java.CMap;
import fap_java.Cell;
import fap_java.Game;
import fap_java.Graph;
import fap_java.Params;
import fap_java.Player;
import fap_java.Team;
import fap_java.Tools;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import java.util.ArrayList;


public class Miner extends Player{
    
    private boolean selecting;
    private ArrayList<Cell> randCells;
    private int cursor;
    
        public Miner(int id, Cell c, Game game, Team t, int ai, int controler) {
        super(id, c, game, 3,t, ai, controler);
        selecting = false;
        randCells = new ArrayList<Cell>();
    }

        public void getSkill() {
            if (this.getGame().getThread().getCount() - this.getLastSkill() >= this.getSkillTime()) {
                this.setLastSkill(this.getGame().getThread().getCount());
                //System.out.println("*BANG*");
                //Send Skill :
                selecting = true;
                ArrayList<Cell> takable = this.getGame().getMap().getTakableCells();
                for(int k=0;k<Params.minerNCells;k++){
                    int rand = Tools.randRange(0, takable.size()-1);
                    Cell c = takable.get(rand);
                    takable.remove(c);
                    c.setMinerSelect(this);
                    randCells.add(c);
                    if(k==0){
                        c.setMinerSing(true);
                        cursor = 0;
                    }
                }
                this.makeHimWait(Params.timeAfterSkill);
            }
        }

    public void setSelecting(boolean selecting) {
        this.selecting = selecting;
    }

    public boolean isSelecting() {
        return selecting;
    }
    
    public void keyHigh(int i) {
        super.keys[i][1] = 1;
        if(this.selecting){
            if (this.getGame().getThread().getCount() - this.getLastDisplacement() >= Params.timeForSelection) {
                this.setLastDisplacement(this.getGame().getThread().getCount());
                // If the key LEFT is pressed
                if (keys[3][1] == 1) {
                    this.cursor++;
                } else if (keys[2][1] == 1) { // If key RIGHT is pressed
                    this.cursor--;
                }
                
                if(cursor <0){
                    cursor = 2;
                }
                
                cursor = cursor%Params.minerNCells;
                for(int k=0;k<randCells.size();k++){
                    Cell c = randCells.get(k);
                    if(k==cursor){
                        c.setMinerSing(true);
                    }
                    else{
                        c.setMinerSing(false);
                    }
                }
            }
        }
        else{
            super.keyHigh(i);
        }
    }
    
    public void keyLow(int i) {
        super.keyLow(i);
        if (i == 4) { //Skill
           selecting = false;
           for(int j=0;j<randCells.size();j++){
               Cell c = randCells.get(j);
               c.setMinerSelect(null);
               if(c.isMinerSing()){
                   //Teleport
                   if (c!= null && c.getOccupied() == null && c.getType()==1) {
                       this.getCurrent().setOccupied(null);
                       this.setCurrent(c);
                       this.setDrawn(c);
                       this.setX( CMap.giveTalePosition(this.getCurrent().getI(), this.getCurrent().getJ())[0] + Params.OFFX);
                       this.setY( CMap.giveTalePosition(this.getCurrent().getI(), this.getCurrent().getJ())[1] + Params.OFFY);
                       this.setI(c.getI());
                       this.setJ(c.getJ());
                       c.activateCell(this);
                       // add a little animation :p
                       Animation anim = new AnimWarp(this.getCurrent(),this.getGame().getThread());
                   }
               }
               c.setMinerSing(false);
           }
           this.randCells = new ArrayList<Cell>();
        }
    }

    public void setRandCells(ArrayList<Cell> randCells) {
        this.randCells = randCells;
    }

    public ArrayList<Cell> getRandCells() {
        return randCells;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    public int getCursor() {
        return cursor;
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
        
        img = Graph.characters.get("Miner"+ori+"_"+this.getColorName());
        g.drawImage(img, x +offset.x ,y+offset.y, 58,90, this.getGame());
    }
}
