package fap_java;

import gui.Button_SampleUI;
import gui.Constants;
import gui.Fapplication;
import gui.MainMenu;

import gui.TheFrame;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.KeyEvent;

import java.io.Serializable;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

// Also does victory screen
public class PauseScreen extends Element implements Serializable{

    private boolean displayVictory;
    private boolean resuming;
    private Game game;

    private String message;
    private Team winner;
    private boolean advMode;

    // Buttons
    private JButton btnResume = new JButton();
    private JButton btnWorldMap = new JButton();
    private JButton btnMainMenu = new JButton();

    private static boolean parsedXML = false;
    
    private int randClass;


    public PauseScreen(boolean dispVict, Game game) {
        super();

        if (!parsedXML) {
            parsedXML = true;
            XMLparser.parseClassesDescList();
        }
        displayVictory = dispVict;
        this.game = game;
        resuming = false;
        message = "";
        randClass = Tools.randRange(0, Params.waitingMessages.length - 1);

        // Init buttons
        btnResume.setText("Continuer");
        btnResume.setSize(Constants.buttonSize);
        btnResume.setUI(new Button_SampleUI());
        ((Button_SampleUI)btnResume.getUI()).setHover(false);
        btnResume.setOpaque(false);
        btnResume.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                //LectureFichierSon.lire(Design.sonChtk);
                ((Button_SampleUI)btnResume.getUI()).setHover(true);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                ((Button_SampleUI)btnResume.getUI()).setHover(false);
            }
        });


        btnWorldMap.setText("Retour a la carte");
        btnWorldMap.setSize(Constants.buttonSize);
        btnWorldMap.setUI(new Button_SampleUI());
        ((Button_SampleUI)btnWorldMap.getUI()).setHover(false);
        btnWorldMap.setOpaque(false);
        btnWorldMap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                //LectureFichierSon.lire(Design.sonChtk);
                ((Button_SampleUI)btnWorldMap.getUI()).setHover(true);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                ((Button_SampleUI)btnWorldMap.getUI()).setHover(false);
            }
        });

        btnMainMenu.setText("Retour au menu");
        btnMainMenu.setSize(Constants.buttonSize);
        btnMainMenu.setUI(new Button_SampleUI());
        ((Button_SampleUI)btnMainMenu.getUI()).setHover(false);
        btnMainMenu.setOpaque(false);
        btnMainMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                //LectureFichierSon.lire(Design.sonChtk);
                ((Button_SampleUI)btnMainMenu.getUI()).setHover(true);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                ((Button_SampleUI)btnMainMenu.getUI()).setHover(false);
            }
        });

        btnResume.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resumeGame();
                ((Button_SampleUI)btnResume.getUI()).setHover(false);
            }
        });

        btnMainMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gotoMainMenu();
                ((Button_SampleUI)btnMainMenu.getUI()).setHover(false);
            }
        });

        btnWorldMap.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gotoWorldMap();
                ((Button_SampleUI)btnWorldMap.getUI()).setHover(false);
            }
        });

        computeButtons();
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.black);
        //TODO better background
        if (!displayVictory) {
            g.setColor(Graph.BG_DARKTRANSLUSCENT);
            g.fillRect(0, 0, game.getWidth(), game.getHeight());

            g.setColor(Color.black);
            Color squareColor = Graph.DEFAULT_SQUARE_COLOR;
            int width = (int)(game.getWidth() * 0.5);
            int height = (int)(game.getHeight() * 0.8);
            if (width <= 500) {
                width = 500;
            }
            int x = (game.getWidth() - width) / 4;
            int y = (game.getHeight() - height) / 2;
            //TODO replace w/h with relative
            Graph.drawDarkBackgroundRectangle(g, x, y, width, height, squareColor, 40, Graph.BG_DARK);
            //g.drawImage(Graph.guimg.get("pauseScreen"), 0, 0,game.getWidth(),game.getHeight(), game);
            
            //Draw thumbnail
            g.drawImage(Graph.thumbnails.get(Params.waitingMessages[randClass][0]), x + 50, y, game);

            //TODO pretty text
            g.setColor(Color.white);
            Tools.drawMultilineString(g, Params.waitingMessages[randClass][1], x + 25, y + 400, 20);
            //g.drawString(Params.waitingMessages[randClass][1], x+25, y+400);
            g.setColor(Color.black);
        }
        this.computeButtons();
        if (!resuming) {
            // Draw buttons
            //System.out.println("I'm drawin' I'm drawin' !");

            // Draw victory
            if (displayVictory) {
                g.setColor(Graph.BG_WHITETRANSLUSCENT);
                g.fillRect(0, 0, game.getWidth(), game.getHeight());
                g.setColor(Color.black);
                Color squareColor = Graph.DEFAULT_SQUARE_COLOR;
                if (winner != null) {
                    squareColor = winner.getFirstPlayer().getColor();
                }

                int width = (int)(game.getWidth() * 0.70);
                int height = (int)(game.getHeight() * 0.70);
                int x = (game.getWidth() - width) / 2;
                int y = (game.getHeight() - height) / 2;
                Graph.drawDarkBackgroundRectangle(g, x, y, width, height, squareColor, 40, Graph.BG_DARK);

                Graphics2D g2d = (Graphics2D)g;
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(message);
                //TODO draw nicer string
                g.drawString(message, x + (width - textWidth) / 2, y + 20);

                if (winner != null) {
                    ArrayList<Player> players = winner.getPlayersInThisTeam();
                    for (int j = 0; j < players.size(); j++) {
                        //TODO player placement (center and space)
                        players.get(j).paintStick(g, x + 20 + j * 30, y + 100);
                    }
                }
            }
        }
    }

    public String toString() {
        return null;
    }

    public void setResuming(boolean resuming) {
        this.resuming = resuming;
        computeButtons();
    }

    public boolean isResuming() {
        return resuming;
    }

    public void setDisplayVictory(boolean displayVictory) {
        this.displayVictory = displayVictory;
        // Remove Button
        computeButtons();
    }

    public boolean isDisplayVictory() {
        return displayVictory;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
    }

    public Team getWinner() {
        return winner;
    }

    public void setAdvMode(boolean advMode) {
        this.advMode = advMode;
        computeButtons();
    }

    public boolean isAdvMode() {
        return advMode;
    }

    // Buttons management

    private void resumeGame() {
        game.pauseGame();
        game.releaseFocus();
        game.initFocus();
    }

    private void gotoMainMenu() {
        TheFrame frame = (TheFrame)Fapplication.getFrame();
        MainMenu panel = new MainMenu(frame, true);

        frame.changePanel(panel);
    }

    private void gotoWorldMap() {
        TheFrame frame = (TheFrame)Fapplication.getFrame();
        Game panel = Fapplication.getWorldMap();
        Fapplication.getWorldMap().pauseGame(true);
        frame.changePanel(panel);
    }

    private void computeButtons() {
        int offX = 0;
        int offY = 0;
        int increment = 0;
        if(this.displayVictory){
            // Placements for victoryDisplay mode
            offX = (int)(game.getWidth() * (1 - 0.15) - Constants.buttonSize.getWidth() + 15./4);
            offY = (int)(game.getHeight() * (1 - 0.15) + (game.getHeight() * (0.15) - Constants.buttonSize.getHeight()) / 2);
            increment = (int)(1.2 * Constants.buttonSize.getWidth());
        }
        else{   // Regular pause mode
            offX = (int)(game.getWidth() - 200 - 35);
            offY = (int)(game.getHeight() * (1 - 0.1) - Constants.buttonSize.getHeight() - 15./4 );
            increment = (int)(1.2 * Constants.buttonSize.getHeight());
        }
        //Remove button
        game.remove(this.btnMainMenu);
        game.remove(this.btnResume);
        game.remove(this.btnWorldMap);
        
        if (!resuming) {
            int nbuttons = 1; // for gotoMainMenu
            if (this.advMode && game.getMap().getFileID() != 0) {
                nbuttons++; // for gotoWorldMap
            }
            if (!this.displayVictory) {
                nbuttons++; // for resume
            }

            // Buttons placement and adding
            int i = 0;
            //Resume
            if (!this.displayVictory) {
                btnResume.setLocation(offX, offY - (i) * increment);
                i++;
                game.add(btnResume);
            }
            //World map
            if (this.advMode && game.getMap().getFileID() != 0) {
                if(this.displayVictory){
                    btnWorldMap.setLocation(offX - (i) * increment, offY);
                }
                else{
                    btnWorldMap.setLocation(offX, offY - (i) * increment);
                }
                i++;
                game.add(btnWorldMap);
            }
            // MainMenu
            if(this.displayVictory){
                btnMainMenu.setLocation(offX - (i) * increment, offY);
            }
            else{
                btnMainMenu.setLocation(offX, offY - (i) * increment);
            }
            game.add(btnMainMenu);
            i++;
        }
    }
}
