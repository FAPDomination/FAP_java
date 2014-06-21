package gui;


import fap_java.Game;

import fap_java.Params;

import javax.swing.JButton;
import javax.swing.JPanel;

import fap_java.Tools;

import java.awt.BorderLayout;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;


public class MainMenu extends FAPanel implements AnimPanel {
    private JButton btnAdventure = new JButton();
    private JButton btnVersus = new JButton();
    private JButton btnQuickPlay = new JButton();
    private JButton btnOptions = new JButton();
    private JButton btnCredits = new JButton();
    private JButton btnQuit = new JButton();

    private ThreadGUI theThread;

    private JPanel nextPanel;

    public MainMenu(TheFrame fr, boolean animBack) {
        super(fr, null);
        nextPanel = null;
        try {
            jbInit(animBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit(boolean animBack) throws Exception {
        this.setLayout(null);
        this.setSize(Constants.frameDimension);

        //------ Buttons
        
        initMenuBtn(btnAdventure, "Aventure", 32,0);
        initMenuBtn(btnVersus, "Versus", 70,50);
        initMenuBtn(btnQuickPlay, "Partie Rapide", 38,100);
        initMenuBtn(btnOptions, "Options", 0,150);
        initMenuBtn(btnCredits, "Credits", 68,200);
        initMenuBtn(btnQuit, "Quitter", 50,250);
        
        // Effects
        btnQuickPlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startQuickPlay();
                ((Button_MainMenuUI)btnQuickPlay.getUI()).setHover(false);
            }
        });
        btnQuit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
                ((Button_MainMenuUI)btnQuit.getUI()).setHover(false);
            }
        });
        btnOptions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnOption_actionPerformed();
                ((Button_MainMenuUI)btnOptions.getUI()).setHover(false);
            }
        });
        btnVersus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnVersus_actionPerformed();
                ((Button_MainMenuUI)btnVersus.getUI()).setHover(false);
            }
        });
        btnAdventure.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnAdv_actionPerformed();
                ((Button_MainMenuUI)btnAdventure.getUI()).setHover(false);
            }
        });
        btnCredits.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnCred_actionPerformed();
                ((Button_MainMenuUI)btnCredits.getUI()).setHover(false);
            }
        });
        
        btnAdventure.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                //LectureFichierSon.lire(Design.sonChtk);
                ((Button_MainMenuUI)btnAdventure.getUI()).setHover(true);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                ((Button_MainMenuUI)btnAdventure.getUI()).setHover(false);
            }
        });
        
        btnVersus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                //LectureFichierSon.lire(Design.sonChtk);
                ((Button_MainMenuUI)btnVersus.getUI()).setHover(true);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                ((Button_MainMenuUI)btnVersus.getUI()).setHover(false);
            }
        });
        
        btnQuickPlay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                //LectureFichierSon.lire(Design.sonChtk);
                ((Button_MainMenuUI)btnQuickPlay.getUI()).setHover(true);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                ((Button_MainMenuUI)btnQuickPlay.getUI()).setHover(false);
            }
        });
        
        btnOptions.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                //LectureFichierSon.lire(Design.sonChtk);
                ((Button_MainMenuUI)btnOptions.getUI()).setHover(true);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                ((Button_MainMenuUI)btnOptions.getUI()).setHover(false);
            }
        });
        
        btnCredits.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                //LectureFichierSon.lire(Design.sonChtk);
                ((Button_MainMenuUI)btnCredits.getUI()).setHover(true);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                ((Button_MainMenuUI)btnCredits.getUI()).setHover(false);
            }
        });
        
        btnQuit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                //LectureFichierSon.lire(Design.sonChtk);
                ((Button_MainMenuUI)btnQuit.getUI()).setHover(true);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                ((Button_MainMenuUI)btnQuit.getUI()).setHover(false);
            }
        });

        //-------------- Images

        // init location
        this.cloudsX = maxxC;
        this.swordX = maxxS;

        // Adding buttons
        if (!animBack) {
            addButtons();
        } else {
            this.startSliding(false);
        }

        this.validate();
        this.repaint();

        //this.startSliding(false);
    }
    
    private void initMenuBtn(JButton jbutt, String label, int offx, int offy){
        int origX = 460;
        int origY = 150;
        jbutt.setUI(new Button_MainMenuUI());
        ((Button_MainMenuUI)jbutt.getUI()).setHover(false);
        jbutt.setText(label);
        jbutt.setSize(120, 40);
        jbutt.setLocation(origX + offx, origY + offy);
        jbutt.setOpaque(false);
    }

    private void btnAdv_actionPerformed() {
        GameSave gs = Tools.loadGame();
        // If the game is'nt new
        Game game = Fapplication.getWorldMap();
        //game.getThread().setRunning(true);
        game.pauseGame(true);
        // If not, start a new game (cutscene)
        //TODO cutscene ? Or just force the starting of the first adventure map ?
        //----
        nextPanel = game;
        this.startSliding(true);
    }

    private void btnOption_actionPerformed() {
        nextPanel = new OptionMenu(parent, this);
        this.startSliding(true);
    }

    private void btnCred_actionPerformed() {
        nextPanel = new CreditsPanel(parent, this);
        this.startSliding(true);
    }

    private void btnVersus_actionPerformed() {
        nextPanel = new PlayerSelection(parent, this);
        this.startSliding(true);
    }

    private void startQuickPlay() {
        // init map
        //TODO keep lis of maps updated
        int[] possibleMaps = { 5, 6, 8, 10, 11, 12, 13 };
        int nmap = possibleMaps[Tools.randRange(0, possibleMaps.length - 1)];
        //nmap = 5;
        // init skill
        int pcP = Tools.randRange(1, 9, Params.excludedChars);
        int pcF = Tools.randRange(1, 9, Params.excludedChars);

        Game game =
            new Game("" + pcP + "," + pcF, "0,1", "0,1", "0,1", false, nmap, Params.defaultVictoryScore, 0, 0, 0);

        nextPanel = new LoadingScreen(parent, game, this, nmap);
        this.startSliding(true);
    }

    private void addButtons() {
        // Adding
        this.add(btnAdventure);
        this.add(btnVersus);
        this.add(btnQuickPlay);
        this.add(btnOptions);
        this.add(btnCredits);
        this.add(btnQuit);
    }

    private void removeButtons() {
        this.remove(btnAdventure);
        this.remove(btnVersus);
        this.remove(btnQuickPlay);
        this.remove(btnOptions);
        this.remove(btnCredits);
        this.remove(btnQuit);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public void executeAnim() {
        swordX += speed;
        cloudsX += speed * 1.35;
        if ((speed < 0 && swordX <= minxS) || (speed > 0 && swordX >= maxxS)) {
            endAnim();
        }
        repaint();
    }

    public void startSliding(boolean toTheLeft) {
        this.removeButtons();
        speed = 25;
        if (toTheLeft) {
            speed *= -1;
            swordX = maxxS;
            cloudsX = maxxC;
        } else {
            swordX = minxS;
            cloudsX = minxC;
        }

        theThread = new ThreadGUI(this);
        theThread.setRunning(false);
        new Thread(this.theThread).start();
        theThread.setRunning(true);
    }

    public void endAnim() {
        theThread.setRunning(false);
        if (nextPanel != null) {
            parent.changePanel(nextPanel, BorderLayout.CENTER);
        } else {
            addButtons();
        }
    }

    public void goBack() {
    } // Not needed

    public void setNextPanel(JPanel nextPanel) {
        this.nextPanel = nextPanel;
    }

    public JPanel getNextPanel() {
        return nextPanel;
    }
}
