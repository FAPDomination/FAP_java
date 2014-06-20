package gui;

import fap_java.CMap;
import fap_java.Cell;

import fap_java.Graph;
import fap_java.XMLparser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;

import java.awt.Graphics2D;
import java.awt.Image;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class Minimap{
    private ArrayList<Cell> map;
    private int size = 5;
    private static Map<Integer,Color> colorList = new HashMap<Integer,Color>();

    private int x;
    private int y;
    private int w = 172;
    private int h = 251;

    private JPanel panel;
    private String name;
    private boolean isSelected;
    private int fileNumber;

    public Minimap(int map, String name) {
        CMap theMap = XMLparser.parseMap(map,null);
        this.map = theMap.getMyMap();
        this.name = name;
        this.fileNumber = map;

        colorList.put(1,new Color(0, 153, 0));
        colorList.put(2,new Color(202, 149, 69));
        colorList.put(3,new Color(150, 150, 150));
        colorList.put(4,new Color(150, 150, 150));
        colorList.put(5,new Color(116, 80, 18));
        colorList.put(6,new Color(0, 204, 0));
        colorList.put(7,new Color(51, 51, 51));
        colorList.put(8,new Color(128, 11, 0));
        colorList.put(15,new Color(254, 194, 105));
        colorList.put(16,new Color(254, 194, 105));
        colorList.put(17,new Color(254, 194, 105));

        colorList.put(10,new Color(106, 255, 102));
        colorList.put(11,new Color(153, 0, 0));

        colorList.put(100,new Color(86, 43, 2));
        
        colorList.put(200,new Color(46, 159, 200));
        colorList.put(201,new Color(241, 58, 5));
    }

    
    public Minimap(int map, int x, int y, JPanel panel, String name) {
        this(map,name);
        this.panel = panel;
        this.x = x;
        this.y = y;
    }

    public void paintComponent(Graphics g) {
        Color square = null;
        //g.drawImage(img, x, y, panel);
        if(isSelected){
            square = Graph.MINIMAP_SELECTED_COLOR;
        }
        Graph.drawDarkBackgroundRectangle(g, x-5, y-5, w, h, square, 15, Graph.BG_WHITETRANSLUSCENT);
        
        for (int i = 0; i < map.size(); i++) {
            Cell c = map.get(i);
            g.setColor(colorList.get(c.getDid()));
            int width = (int)(size * 1.4);
            g.fillRect(x + c.getJ() * width, y + c.getI() * size, width, size);
        }
        g.setColor(Color.black);
        g.setFont(Graph.MINIMAP_FONT);
        Graphics2D g2d = (Graphics2D)g;
        FontMetrics fm = g2d.getFontMetrics();
        int nameWidth = fm.stringWidth(name);
        int imgWidth = 150;
        Graph.drawBorderedString(g, x + (imgWidth - nameWidth) / 2, y + 238, name, Graph.MENU_TEXT_BORDER_TRANSLUSCENT);
        //g.drawString(name, x + (imgWidth - nameWidth) / 2, y + 238);
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isIsSelected() {
        return isSelected;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setFileNumber(int fileNumber) {
        this.fileNumber = fileNumber;
    }

    public int getFileNumber() {
        return fileNumber;
    }
    
    public int[] getArea(){
        int[] table = new int[4];
        table[0] = x;
        table[1] = y;
        table[2] = w;
        table[3] = h;
        return table;
    }
    
    public boolean inArea(double u, double v){
        boolean b = true;
        if(!(u>=this.x && u<=this.x+this.w)){
            b = false;
        }
        if(!(v>=this.y && v<=this.y+this.h)){
            b = false;
        }
        return b;
    }
    
    public boolean inArea(Point p){
        return this.inArea(p.getX(),p.getY());
    }
    
    public String toString(){
        return "Minimap for the file "+this.fileNumber;
    }
}
