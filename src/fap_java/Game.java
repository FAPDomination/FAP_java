package fap_java;

import java.awt.Dimension;
import java.awt.Graphics;

import java.util.ArrayList;

import javax.swing.JPanel;

public class Game extends JPanel{
    
    private CMap map;
    private TheThread thread;
    private ArrayList<Player> players = new ArrayList<Player>();
    private KListener kl;
    
    public Game() {

        this.setLayout(null);
        map = new CMap();
        Cell c1 = new Cell(5,3,1);
        Cell c2 = new Cell(5,4,1);
        Cell c3 = new Cell(6,3,1);
        Cell c4 = new Cell(6,4,1);
        Cell c5 = new Cell(6,5,1);
        Cell c6 = new Cell(7,3,1);
        Cell c7 = new Cell(7,4,1);
        
        Cell c8 = new Cell(6,6,1);
        Cell c9 = new Cell(6,7,1);
        Cell c10 = new Cell(6,8,1);
        Cell c11 = new Cell(6,9,1);
        Cell c12 = new Cell(6,10,1);
        
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
        
        thread = new TheThread(this);
        thread.setRunning(false);
        new Thread(this.thread).start();
        thread.setRunning(true);
        
        int[] coord = new int[2];
        coord[0] = 6;
        coord[1] = 4;
        Player p1 = new Player(coord, this);
        players.add(p1);
        
        kl = new KListener(this);
        this.addKeyListener(kl);
        this.setFocusable(true);
        requestFocus();
        
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        map.paintComponent(g);
        for(int i = 0; i< players.size();i++){
            players.get(i).paintComponent(g);
        }
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
}
