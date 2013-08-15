package gui;

import fap_java.CMap;
import fap_java.Cell;

import fap_java.Graph;
import fap_java.XMLparser;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import java.awt.Graphics2D;
import java.awt.Image;

import java.util.ArrayList;

import javax.swing.JPanel;

public class Minimap {
    private ArrayList<Cell> map;
    private int size = 5;
    private Color[] colorList = new Color[300];

    private int x;
    private int y;

    private Image img = Graph.guimg.get("minimapBG");
    private JPanel panel;
    private String name;

    public Minimap(int map, int x, int y, JPanel panel, String name) {
        CMap theMap = XMLparser.parseMap(map);
        this.map = theMap.getMyMap();
        this.x = x;
        this.y = y;
        this.panel = panel;
        this.name = name;

        colorList[1] = new Color(0, 153, 0);
        colorList[2] = new Color(202, 149, 69);
        colorList[3] = new Color(150, 150, 150);
        colorList[4] = new Color(150, 150, 150);
        colorList[5] = new Color(116, 80, 18);
        colorList[6] = new Color(0, 204, 0);
        colorList[7] = new Color(51, 51, 51);
        colorList[8] = new Color(128, 11, 0);

        colorList[10] = new Color(106, 255, 102);
        colorList[11] = new Color(153, 0, 0);

        colorList[100] = new Color(86, 43, 2);
        colorList[200] = new Color(46, 159, 200);
        colorList[201] = new Color(241, 58, 5);
    }

    public void paintComponent(Graphics g) {
        g.drawImage(img, x, y, panel);
        for (int i = 0; i < map.size(); i++) {
            Cell c = map.get(i);
            g.setColor(colorList[c.getDid()]);
            int width = (int)(size * 1.4);
            g.fillRect(x + c.getJ() * width, y + c.getI() * size, width, size);
        }
        g.setColor(Color.black);
        Graphics2D g2d = (Graphics2D)g;
        FontMetrics fm = g2d.getFontMetrics();
        int nameWidth = fm.stringWidth(name);
        int imgWidth = 150;
        g.drawString(name, x + (imgWidth - nameWidth) / 2, y + 220);
    }
}
