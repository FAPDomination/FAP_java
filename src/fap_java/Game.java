package fap_java;

import characters.*;

import java.awt.Dimension;
import java.awt.Graphics;

import java.util.ArrayList;

import javax.swing.JPanel;

import org.xml.sax.helpers.DefaultHandler;

public class Game extends JPanel{
    
    private CMap map;
    private TheThread thread;
    private ArrayList<Player> players = new ArrayList<Player>();
    private KListener kl;
    private ScoreBar scoreHandler;
    
    public Game() {

        this.setLayout(null);
        
        map = new CMap();
        map = XMLparser.parseMap(5);
       /* map = new CMap();
        Cell c1 = new Cell(5,3,1);
        Cell c2 = new Cell(5,4,1);
        Cell c3 = new Cell(6,3,10,"7,12");
        Cell c4 = new Cell(6,4,1);
        Cell c5 = new Cell(6,5,1);
        Cell c6 = new Cell(7,3,1);
        Cell c7 = new Cell(7,4,1);
        
        Cell c8 = new Cell(6,6,1);
        Cell c9 = new Cell(6,7,2,"100");
        Cell c10 = new Cell(6,8,1);
        Cell c11 = new Cell(6,9,1);
        Cell c12 = new Cell(6,10,1);
        
        Cell c13 = new Cell(6,11,1);
        Cell c14 = new Cell(7,9,1);
        Cell c15 = new Cell(7,10,1);
        Cell c16 = new Cell(7,11,1);
        Cell c17 = new Cell(7,12,1);
        Cell c18 = new Cell(8,10,1);
        Cell c19 = new Cell(8,11,1);
        
        Cell c20 = new Cell(9,9,20);
        Cell c21 = new Cell(9,10,20);
        Cell c22 = new Cell(9,11,20);
        Cell c23 = new Cell(9,12,20);
        Cell c24 = new Cell(8,12,20);
        
        map.addElement(c5);
        map.addElement(c1);
        map.addElement(c2);
        map.addElement(c3);
        map.addElement(c4);
        map.addElement(c6);
        map.addElement(c7);
        map.addElement(c8);
        map.addElement(c9);
        map.addElement(c10);
        map.addElement(c11);
        map.addElement(c12);
        map.addElement(c13);
        map.addElement(c14);
        map.addElement(c15);
        map.addElement(c16);
        map.addElement(c17);
        map.addElement(c18);
        map.addElement(c19);
        map.addElement(c20);
        map.addElement(c21);
        map.addElement(c22);
        map.addElement(c23);
        map.addElement(c24);
        */
        
        thread = new TheThread(this);
        thread.setRunning(false);
        new Thread(this.thread).start();
        thread.setRunning(true);
        
        kl = new KListener(this);
        this.addKeyListener(kl);
        this.setFocusable(true);
        requestFocus();
        
        initPlayers();
        
        scoreHandler = new ScoreBar(this);
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        map.paintComponent(g);
        for(int i = 0; i< players.size();i++){
            players.get(i).paintComponent(g);
        }
        this.scoreHandler.paintComponent(g);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
    
    public CMap getMap() {
        return map;
    }

    public TheThread getThread() {
        return thread;
    }
    
    public void refreshHealthPoints(){
        ArrayList<Cell> myMap = map.getMyMap();
        for(int j=0;j<myMap.size();j++){
            Cell c = myMap.get(j);
            c.refreshHealthPoints();
        }
        for(int i=0;i<players.size();i++){
            Player p = players.get(i);
            Cell c = p.getCurrent();
            c.activateCell(p);
        }
        this.repaint();
    }
    
    public Player isOccupied(Cell c){
        Player p = null;
        if(c!=null && map.containsCell(c) != -1){
            for(int i =0; i< players.size();i++){
                Player q = players.get(i);
                if(q.getI() == c.getI() && q.getJ() == c.getJ()){
                    p=q;
                    break;
                }
            }
        }
        return p;
    }
    
    public void initPlayers(){
        int[] coord = new int[2];
        coord[0] = 16;
        coord[1] = 5;
        Cell c = map.getCell(coord);
        System.out.println(c);
        Player p1 = new Knight(1,c, this);
        players.add(p1);
        
        coord = new int[2];
        coord[0] = 17;
        coord[1] = 15;
        c = map.getCell(coord);
        Player p2 = new Knight(2,c, this);
        players.add(p2);
    }
    
    public void updateCellsByOwner(){
        for(int j=0;j<players.size();j++){
            Player p = players.get(j);
            p.setNCells(0);
        }
        ArrayList<Cell> cells = map.getMyMap();
        for(int i=0;i<cells.size();i++){
            Cell c = cells.get(i);
            if(c.getOwner()!=null){
                Player p = c.getOwner();
                p.setNCells(p.getNCells()+1);
            }
        }
        
    }


    public ScoreBar getScoreHandler() {
        return scoreHandler;
    }
}
