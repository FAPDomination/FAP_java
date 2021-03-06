package gui;

import fap_java.Game;
import fap_java.Params;
import fap_java.XMLparser;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;


public class MapSelect extends FAPanel implements MouseListener {
    private JButton btnNext = new JButton();
    private ArrayList<Minimap> mapList;
    private Minimap selectedMap;

    private JButton btnNextPage = new JButton();
    private JButton btnPrevPage = new JButton();

    private int maxMapsOnPage = 8;
    private int mapStart = 0;
    private int nPages = 0;

    public MapSelect(TheFrame theFrame, JPanel jPanel) {
        super(theFrame, jPanel);


        swordX = minxS;
        cloudsX = minxC;

        this.setLayout(null);
        this.setSize(Constants.frameDimension);

        btnGoBack.setText("Retour");
        btnGoBack.setSize(120, 60);
        btnGoBack.setLocation(origX - 5, origY - 5);

        origX += 5;
        origY += 5;

        btnNext.setText("Jouer");
        btnNext.setSize(120, 60);
        btnNext.setUI(new Button_SampleUI());
        ((Button_SampleUI)btnNext.getUI()).setHover(false);
        btnNext.setOpaque(false);
        btnNext.setLocation(this.getWidth() - 30 - btnNext.getWidth(), 20);
        btnNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextFrame();
                ((Button_SampleUI)btnNext.getUI()).setHover(false);
            }
        });
        btnNext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                //LectureFichierSon.lire(Design.sonChtk);
                ((Button_SampleUI)btnNext.getUI()).setHover(true);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                ((Button_SampleUI)btnNext.getUI()).setHover(false);
            }
        });

        btnNextPage.setSize(48, 48);
        btnNextPage.setOpaque(false);
        btnNextPage.setUI(new Button_AddRemoveUI("btn_arrow_next"));
        ((Button_AddRemoveUI)btnNextPage.getUI()).setHover(false);
        btnNextPage.setLocation(this.getWidth() - origX - 48, this.getHeight() / 2 - 55);
        btnNextPage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mapStart++;
                if (mapStart > nPages) {
                    mapStart--;
                }
                repaint();
                ((Button_AddRemoveUI)btnNextPage.getUI()).setHover(false);
            }
        });
        btnNextPage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                //LectureFichierSon.lire(Design.sonChtk);
                ((Button_AddRemoveUI)btnNextPage.getUI()).setHover(true);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                ((Button_AddRemoveUI)btnNextPage.getUI()).setHover(false);
            }
        });

        btnPrevPage.setSize(48, 48);
        btnPrevPage.setOpaque(false);
        btnPrevPage.setUI(new Button_AddRemoveUI("btn_arrow_prev"));
        ((Button_AddRemoveUI)btnPrevPage.getUI()).setHover(false);
        btnPrevPage.setLocation(this.getWidth() - origX - 48, this.getHeight() / 2 + 5);
        btnPrevPage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mapStart--;
                if (mapStart < 0) {
                    mapStart++;
                }
                repaint();
                ((Button_AddRemoveUI)btnPrevPage.getUI()).setHover(false);
            }
        });
        btnPrevPage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                //LectureFichierSon.lire(Design.sonChtk);
                ((Button_AddRemoveUI)btnPrevPage.getUI()).setHover(true);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                ((Button_AddRemoveUI)btnPrevPage.getUI()).setHover(false);
            }
        });

        this.add(btnNextPage);
        this.add(btnPrevPage);
        this.add(btnGoBack);
        this.add(btnNext);
        this.validate();

        mapList = XMLparser.parseMapList();
        nPages = mapList.size() / this.maxMapsOnPage;
        for (int i = 0; i < mapList.size(); i++) {
            Minimap m = mapList.get(i);
            m.setPanel(this);
        }

        selectedMap = null;
        this.btnNext.setEnabled(false);
        this.addMouseListener(this);
        this.repaint();
    }

    public void nextFrame() {
        // Check if map is selected
        if (selectedMap != null) {

            ArrayList<PlayerSelect> players = ((CharacterSelection)prevPanel).getPlayers();

            Game game = new Game(players, true, selectedMap.getFileNumber(), Params.defaultVictoryScore, 0, 0, 0);
            parent.changePanel(new LoadingScreen(parent, game, this, selectedMap.getFileNumber()));
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int nMapPerLine = 4;
        origY = 100;
        int h = 0;
        int incrementX = (this.getWidth() - (2 * origX)) / nMapPerLine;
        int incrementY = 270;
        for (int j = mapStart * maxMapsOnPage; j < Math.min((mapStart + 1) * maxMapsOnPage, mapList.size()); j++) {
            int i = j - mapStart * maxMapsOnPage;
            Minimap m = mapList.get(j);
            m.setX(origX + (i % nMapPerLine) * incrementX);
            m.setY(origY + h * incrementY);
            m.paintComponent(g);
            if (i > 1 && (i + 1) % (nMapPerLine) == 0) {
                h++;
            }
        }
    }

    private Minimap whoIsClicked(Point p) {
        Minimap m = null;
        for (int j = mapStart * maxMapsOnPage; j < Math.min((mapStart + 1) * maxMapsOnPage, mapList.size()); j++) {
            Minimap k = mapList.get(j);
            if (k.inArea(p)) {
                m = k;
                break;
            }
        }
        return m;
    }

    public void mouseClicked(MouseEvent e) {
        Point p = e.getPoint(); // Where is the click
        Minimap m = whoIsClicked(p);
        if (m != null) {
            if (selectedMap != null) {
                selectedMap.setIsSelected(false);
            } else {
                this.btnNext.setEnabled(true);
            }
            selectedMap = m;
            selectedMap.setIsSelected(true);
            this.repaint();
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
