package gui;

import fap_java.Game;
import fap_java.Params;
import fap_java.Player;
import fap_java.Tools;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;


public class CharacterSelection extends FAPanel implements NeedingFocus, AnimPanel {
    private JButton btnNext = new JButton();
    private ArrayList<CharacterDisplay> charList;
    private ArrayList<ArrowSelect> arrowList;
    private ArrayList<PlayerSelect> players;
    private ArrayList<Integer> timers;
    private StandardKListener kl;

    private ThreadGUI theThread;

    // Displaying
    private static int characDisplayOrigX = 0;
    private static int characDisplayOrigY=250;
    private static int characDisplayIncrement = 100;

    private int arroSelectOrigY = -40;

    private Game advGame;
    
    private boolean continuing=false;
    
    private ArrayList<Integer> listUnlockedCharsID = new ArrayList<Integer>();

    public CharacterSelection(TheFrame theFrame, JPanel jPanel) {
        super(theFrame, jPanel);

        players = ((PlayerSelection)prevPanel).getPlayers();
        advGame = null;
        initEverything();

    }

    public CharacterSelection(TheFrame theFrame, JPanel jPanel, Game game) {
        super(theFrame, jPanel);
        players = new ArrayList<PlayerSelect>();
        this.advGame = game;
        for (int i = 0; i < advGame.getPlayers().size(); i++) {
            Player p = advGame.getPlayers().get(i);
            PlayerSelect ps = new PlayerSelect(null);
            ps.setControler(p.getControler());
            ps.setId(i);
            if (p.getFsm() != null) {
                ps.setIsFSM(1);
            }
            players.add(ps);
        }
        this.initEverything();
    }

    public void initEverything() {
        swordX = minxS;
        cloudsX = minxC;

        this.setLayout(null);
        this.setSize(Constants.frameDimension);

        btnGoBack.setText("Retour");
        btnGoBack.setSize(120,60);
        btnGoBack.setLocation(origX-5, origY-5);

        btnNext.setText("Suivant");
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

        if (advGame == null) {
            this.add(btnGoBack);
        }
        this.add(btnNext);
        this.validate();
        this.repaint();

        charList = new ArrayList<CharacterDisplay>();
        arrowList = new ArrayList<ArrowSelect>();


        
        //Check unlocked
        if (advGame != null) {
            GameSave gs = Tools.loadGame();
            ArrayList<Boolean> listUnlockedChars = gs.getUnlockedChars();
            boolean only7 = true;
            for (int i = 0; i < listUnlockedChars.size(); i++) {
                if (i != 7 && listUnlockedChars.get(i)) {
                    only7 = false;
                }
            }
            if (only7) {
                players.get(0).setPc(7);
                this.continuing = true;
                return;
            }
        }



        int k = 1;
        for (int i = 1; i < 10; i++) {
            boolean m = i != 2 && i != 7;
            boolean b = true;
            if (advGame != null) {
                GameSave gs = Tools.loadGame();
                ArrayList<Boolean> listUnlockedChars = gs.getUnlockedChars();
                if (!listUnlockedChars.get(i)) {
                    b = false;
                }
            }
            if(m){
                charList.add(new CharacterDisplay(this.characDisplayOrigX + k * this.characDisplayIncrement, characDisplayOrigY + Tools.randRange(0, 50), i, b, this));
                if(b){
                    listUnlockedCharsID.add(i);
                }
                k++;
            }
        }

        //System.out.println(listUnlockedCharsID);

        timers = new ArrayList<Integer>();
        for (int r = 0; r < Params.nPlayersOn1Computer; r++) {
            arrowList.add(null);
            timers.add(0);
        }

        for (int j = 0; j < players.size(); j++) {
            PlayerSelect ps = players.get(j);
            if (ps.getIsFSM() == 0) {
                if (advGame == null) {
                    int newPC=Tools.randRange(1, 9, Params.excludedChars);
                    boolean b;
                    do{
                        b = false;
                        newPC=Tools.randRange(1, 9, Params.excludedChars);
                        for(int m=0;m<players.size();m++){
                            PlayerSelect ps2 = players.get(m);
                            
                            if(ps.getControler() != ps2.getControler() && ps2.getIsFSM() == 0 && ps2.getTeam() == ps.getTeam()){
                                if(ps2.getPc() == newPC){
                                    b= true;
                                }
                                else{
                                    break;
                                }
                            }
                        }
                    }while(b);
                    ps.setPc(newPC);
                } else {
                    if(listUnlockedCharsID.size()>0){
                        int rand = Tools.randRange(0,listUnlockedCharsID.size()-1);
                        ps.setPc(listUnlockedCharsID.get(rand));
                    }
                    else{
                        
                    }
                }
                ArrowSelect as = new ArrowSelect(ps, this);
                arrowList.set(ps.getControler(), as);
            }
        }


        

        //initFocus();

        //launchThread();
    }
    
    private void launchThread(){
        theThread = new ThreadGUI(this);
        new Thread(this.theThread).start();
    }

    private void initKListener() {
        kl = new StandardKListener();
        this.addKeyListener(kl);
    }

    public void initFocus() {
        if(!continuing){
            initKListener();
            launchThread();
            this.setFocusable(true);
            requestFocus();
        }
        else{
            this.nextFrame();
            this.continuing = false;
        }
    }

    public void nextFrame() {
        // Set skills for FSMs :
        for (int i = 0; i < players.size(); i++) {
            PlayerSelect ps = players.get(i);
            if (ps.getIsFSM() != 0) {
                if(advGame != null){
                    ps.setPc(advGame.getPlayers().get(i).getPc());
                }
                else{
                    int newPC=Tools.randRange(1, 9, Params.excludedChars);
                    boolean b;
                    do{
                        b = false;
                        newPC=Tools.randRange(1, 9, Params.excludedChars);
                        for(int m=0;m<players.size();m++){
                            PlayerSelect ps2 = players.get(m);
                            
                            if(ps.getControler() != ps2.getControler() && ps2.getIsFSM() == 0 && ps2.getTeam() == ps.getTeam()){
                                if(ps2.getPc() == newPC){
                                    b= true;
                                }
                                else{
                                    break;
                                }
                            }
                        }
                    }while(b);
                    ps.setPc(newPC);
                }
            }
            if (advGame != null) {
                Player formerP = advGame.getPlayers().get(i);
                int fsmLevel;
                formerP.getTeam().getPlayersInThisTeam().remove(formerP);
                if (formerP.getFsm() != null) {
                    fsmLevel = formerP.getFsm().getLevel();
                } else {
                    fsmLevel = 0;
                }
                Player p =
                    Game.generatePlayer(ps.getPc(), i, formerP.getCurrent(), formerP.getTeam(), fsmLevel, formerP.getControler(),
                                        advGame);
                advGame.getPlayers().set(i, p);
            }
        }

        if(theThread != null){
            this.theThread.setRunning(false);
        }
        // Proceeding to next panel
        JPanel nextPanel;
        if (advGame != null) {
            this.remove(this.btnGoBack);
            nextPanel =
                    new LoadingScreen(this.parent, advGame, new MainMenu(this.parent, true), advGame.getMap().getFileID());
        } else {
            nextPanel = new MapSelect(parent, this);
        }
        parent.changePanel(nextPanel);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < charList.size(); i++) {
            charList.get(i).paintComponent(g);
        }
        for (int j = 0; j < arrowList.size(); j++) {
            ArrowSelect ar = arrowList.get(j);
            if (ar != null) {
                ar.paintComponent(g);
            }
        }
    }

    public static void setCharacDisplayOrigX(int characDisplayOrigX) {
        CharacterSelection.characDisplayOrigX = characDisplayOrigX;
    }

    public static int getCharacDisplayOrigX() {
        return characDisplayOrigX;
    }

    public static void setCharacDisplayIncrement(int characDisplayIncrement) {
        CharacterSelection.characDisplayIncrement = characDisplayIncrement;
    }

    public static int getCharacDisplayIncrement() {
        return characDisplayIncrement;
    }

    public void setCharList(ArrayList<CharacterDisplay> charList) {
        this.charList = charList;
    }

    public ArrayList<CharacterDisplay> getCharList() {
        return charList;
    }

    public void setPlayers(ArrayList<PlayerSelect> players) {
        this.players = players;
    }

    public ArrayList<PlayerSelect> getPlayers() {
        return players;
    }

    public void executeAnim() {
        for (int i = 0; i < timers.size(); i++) {
            timers.set(i, timers.get(i) + 1);
            ArrowSelect ar = this.arrowList.get(i);
            if (ar != null && ar.isNeedAnim()) {
                ar.executeAnim();
            }
        }

        ArrayList<Integer> pressed = kl.getPressed();
        for (int i = 0; i < pressed.size(); i++) {
            int code = pressed.get(i);
            for (int j = 0; j < arrowList.size(); j++) {
                ArrowSelect ar = arrowList.get(j);
                if (ar != null && timers.get(j) > 10) {
                    for (int k = 0; k <= 4; k++) {
                        if (code == Params.controlsList[ar.getPs().getControler()][k]) {
                            timers.set(j, 0);
                            int pc = ar.getPs().getPc();
                            CharacterDisplay cd = null;
                            for (int l = 0; l < charList.size(); l++) {
                                CharacterDisplay charD = charList.get(l);
                                if (charD.getPc() == pc) {
                                    int id=0;
                                    // Pressing Key RIGHT
                                    if (k == 2) {
                                        // Looking for next character
                                        id = (l + 1) % charList.size();
                                        boolean b;
                                        // This loop is used to search for the next character if this one is unavailable
                                        // and so on...
                                        do{
                                            b=false;
                                            // Get the character
                                            int newPc = charList.get(id).getPc();
                                            // If not adventure Mode
                                            if(advGame == null){
                                                // Check if a human player on the same team already has this character
                                                for(int m=0;m<players.size();m++){
                                                    PlayerSelect ps = players.get(m);
                                                    if(ps.getTeam() == ar.getPs().getTeam() && ps.getIsFSM()==0 && ps.getPc()==newPc){
                                                        // If yes activate re-search
                                                        b=true;
                                                    }
                                                }
                                            }
                                            // if adventure mode
                                            else{
                                                // Just check if this character is unlocked
                                                if(!this.listUnlockedCharsID.contains(newPc)){
                                                    // If yes activate re-search
                                                    b=true;
                                                }
                                            }
                                            
                                            if(b){
                                                // Retry
                                                id = (id+1)%charList.size();
                                            }
                                        }while(b);
                                        
                                        // Pressing Key  LEFT
                                    } else if (k == 3) {
                                        // Looking for prev character
                                        id = (l - 1);
                                        if(l == 0){
                                            id = charList.size() - (1);
                                        }
                                        boolean b;
                                        // This loop is used to search for the prev character if this one is unavailable
                                        // and so on...
                                        do{
                                            b=false;
                                            // Get the character
                                            int newPc = charList.get(id).getPc();
                                            // If not adventure Mode
                                                    if(advGame == null){
                                                        // Check if a human player on the same team already has this character
                                                        for(int m=0;m<players.size();m++){
                                                            PlayerSelect ps = players.get(m);
                                                            if(ps.getTeam() == ar.getPs().getTeam() && ps.getIsFSM()==0 && ps.getPc()==newPc){
                                                                // If yes activate re-search
                                                                b=true;
                                                            }
                                                        }
                                                    }
                                                    // if adventure mode
                                                    else{
                                                        // Just check if this character is unlocked
                                                        if(!this.listUnlockedCharsID.contains(newPc)){
                                                            // If yes activate re-search
                                                            b=true;
                                                        }
                                                    }
                                            if(b){
                                                // Retry
                                                id = id-1;
                                                if(id < 0){
                                                    id = charList.size() - (1);
                                                }
                                            }
                                        }while(b);
                                    }
                                    cd = charList.get(id);
                                    break;
                                }
                            }
                            // Set new pc  value
                            if (cd != null) {
                                ar.getPs().setPc(cd.getPc());
                                ar.computeWantedPosition();
                                ar.computeSpeed();
                                ar.setNeedAnim(true);
                            }
                        }
                    }
                }
            }
        }


        this.repaint();
    }

    public void endAnim() {
        theThread.setRunning(false);
    }

    public void setArroSelectOrigY(int arroSelectOrigY) {
        this.arroSelectOrigY = arroSelectOrigY;
    }

    public int getArroSelectOrigY() {
        return arroSelectOrigY;
    }

    public void releaseFocus() {
        this.removeKeyListener(kl);
    }
}
