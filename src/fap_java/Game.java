package fap_java;


import animations.Animation;
import animations.NPCMessage;
import animations.PauseCountDown;

import characters.*;

import gui.Constants;
import gui.Fapplication;
import gui.GameSave;
import gui.NeedingFocus;
import gui.PlayerSelect;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.util.ArrayList;
import java.util.Map;

import javax.swing.JPanel;

import npcs.NPCWMBlocking;
import npcs.NPCWMStarting;

import pathFinder.pathFinder;


public class Game extends JPanel implements NeedingFocus {

    /**
     * The map of this Game. Contains a grid of cells and a set of methods to play with them
     */
    private transient CMap map;
    /**
     * The thread permanently refreshes the game. Updates healthpoints, positions of players, animations, etc...
     */
    private transient TheComputingThread thread;
    /**
     * The thread permanently refreshes the graphics game. Draws map, players, etc...
     */
    private transient TheGraphicalThread Gthread;
    /**
     * This contains all the player of this game. See also fap_java.Player
     */
    private transient ArrayList<Player> players = new ArrayList<Player>();
    /**
     * The Key Listener that will handle player displacements and pause
     */
    private transient KListener kl;
    /**
     * The scorebars manager
     */
    private transient ScoreBar scoreHandler;
    /**
     * Contains every team of the game
     */
    private transient ArrayList<Team> teams = new ArrayList<Team>();
    
    /**
     * How much points does it take to win the game
     */
    private int victScore;
    /**
     * How much percent of total cells of the map does it take to win the game
     */
    private double victTile;    // in percent
    /**
     * Time before automatically losing the game
     */
    private int victTime; // in sec
    
    /**
     * A boolean indicating if the game is still on or if someone won
     */
    private boolean gameEnded;
    
    /**
     * The Objects of the game, such as arrows
     */
    private ArrayList<Element> objects = new ArrayList<Element>();
    /**
     * The animations of the game, such as explosions
     */
    private ArrayList<Animation> anims = new ArrayList<Animation>();
    
    //Parameters to be given when starting a new game
    /**
     * A String indicating wich player is in the game and their skills. Ex : "1,8,0,4"
     */
    private String whoIsPlaying; // This is linked with skill, here player 1 is magician
    /**
     * The no of team the corresponding player is in. Ex : "0,1,1,0"
     */
    private String wichTeam; // Here player no2 is in team no0
    /**
     * Indicating wich player is FSM. Ex : "0,1,1,0"
     */
    private String isFSM;   // 0 for player, other for FSM level
    /**
     * Wich controler (set of keys) controls this player if it's not a FSM
     */
    private String controlers;
    /**
     * If the starting points of the map are mixed or fixed
     */
    private boolean randStart;
    
    /* ----- Adventure Related stuff */
    /**
     * Tells if the game is in adventure mode
     * 0 : battle mode (conquest and stuff) launched from versus
     * 1 : battle mode launched from adventure
     * 2 : full adventure map (town without battle)
     */
    private int adv;
    
    private GameSave gameSave;
    
    private ArrayList<NPC> listNPCs = new ArrayList<NPC>();
    private ArrayList<NPC> listTriggerNPCs = new ArrayList<NPC>();
    
    private boolean pauseNPC;
    
    private ArrayList<FSMThread> fsmThread = new ArrayList<FSMThread>();
    
    private boolean quickPlay = false;
    
    private String WMcellHash;
    
    // performances
    public long min=-1,max=-1,moy=0,c=1,cez=0;

    /**
     * Initializes a game. extends JPanel so it draws everything that is game-related. It initalizes the teams, 
     * the players, the map, the cells, the scores and victory condition, and handle everything.
     * Permanently refreshed by TheThread.
     * @param whoIsPlaying A String indicating wich player is in the game and their skills. Ex : "1,8,0,4"
     * @param wichTeam The no of team the corresponding player is in. Ex : "0,1,1,0"
     * @param controlers Wich controler (set of keys) controls this player if it's not a FSM
     * @param isFSM Indicating wich player is FSM. Ex : "0,1,1,0"
     * @param randStart If the starting points of the map are mixed or fixed
     * @param nmap The number of file the map is in. Maps are stored in "resources/Maps/map"+nmap+".xml"
     * @param victScore How much points does it take to win the game
     * @param victTile How much percent of total cells of the map does it take to win the game
     * @param victTime Time before automatically losing the game
     */
    public Game(String whoIsPlaying, String wichTeam, String controlers, String isFSM, boolean randStart, int nmap, int victScore, double victTile, int victTime, int adv) {
        System.out.println("Creating game");
        this.victScore = victScore;
        this.victTile = victTile;
        this.victTime = victTime;
        this.whoIsPlaying = whoIsPlaying;
        this.wichTeam = wichTeam;
        this.isFSM = isFSM;
        this.randStart = randStart;
        this.controlers = controlers;
        this.adv = adv;
        
        initGame(nmap);
        
        initTeams();
        
        initPlayers();
        
        // Init scores
        if(adv < 2){
            scoreHandler = new ScoreBar(this);
        }
    }

    /**
     * Initializes a game from the Versus menu. cf other Game constructor
     * @param playerSelect
     * @param randStart
     * @param mapNumber
     * @param victScore
     * @param victTile
     * @param victTime
     */
    public Game(ArrayList<PlayerSelect> playerSelect, boolean randStart, int mapNumber, int victScore, double victTile, int victTime, int adv) {
        this.victScore = victScore;
        this.victTile = victTile;
        this.victTime = victTime;
        initGame(mapNumber);
        this.randStart = randStart;
        this.adv = adv;
        
        initTeams(playerSelect);
        initPlayers(playerSelect);
        
        if(adv < 2){
            scoreHandler = new ScoreBar(this);
        }
    
    }
    
    /**
     * Initializes a full adventure map (whitout battling and stuff), such as a town, qhere the player can peacefully
     * walk and talk to NPCs.
     * @param nmap The ID of the map where it's played
     */
    public Game(int nmap){
        this("7","0","0","0",false,nmap,0,0,0,2);
        // Detect World Map
        initListNPCs(nmap);
    }
    
    /**
     * Init common game-related stuff, such as the map, thread, and pauses the game
     * @param nmap The number of the file the map is in
     */
    public void initGame(int nmap){
        // Panel related stuff 
        this.setLayout(null);
        this.setBackground(Color.white);
        this.listNPCs = new ArrayList<NPC>();
        // Parse the map
        map = new CMap(this,nmap);
        map = XMLparser.parseMap(nmap,this);
        map.initDirts();
        
        //Parse ParamTable
        XMLparser.parseParams();
        this.setSize(Constants.frameDimension);
        
        // Initialize thread
        thread = new TheComputingThread(this);
        thread.setRunning(false);
        new Thread(this.thread).start();
        thread.setRunning(true);
        
        // Initialize thread
        Gthread = new TheGraphicalThread(this);
        Gthread.setRunning(false);
        new Thread(this.Gthread).start();
        Gthread.setRunning(true);
        
        gameEnded = false;
        
        if(adv > 0){
            this.gameSave = Tools.loadGame();
        }
        
        //Pause it (so it can be automatically un-paused afterwards)
        //if(adv < 2){
            pauseGame();
        //}
    }

    /**
     * Paint the panel. takes care of background, map, players and other components
     * @param g Graphical thing that no one really understands
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //long startTime = System.currentTimeMillis();
        // Background
        Graphics2D g2d = (Graphics2D)g;
        
        int w = this.getWidth();
        int h = this.getHeight();

        // Paint a gradient from top to bottom
        GradientPaint gp = new GradientPaint(0, 0, Constants.top, 0, h, Constants.bottom);

        g2d.setPaint(gp);
        //TODO Better background ??
        g2d.fillRect(0, 0, w, h);
        // --- End BG
        
        // Repaint the map
        map.paintComponent(g);
        PauseScreen ps = null;
        // Repaint the objects (such as arrows)
        for(int j=0;j<objects.size();j++){
            Element e = objects.get(j);
            if(e instanceof PauseScreen){
                ps = (PauseScreen) e;
            }
            else{
                objects.get(j).paintComponent(g);
            }
        }
        if(adv <2){
            // Repaint the scoreHandler
            this.scoreHandler.paintComponent(g);
        }
        
        if(ps != null){
            ps.paintComponent(g);
        }
        // Paint the animations (warps, explosions, bitches,...)
        for(int j=0;j<anims.size();j++){
            if(thread.getRunning() || anims.get(j) instanceof PauseCountDown || anims.get(j) instanceof NPCMessage){
                anims.get(j).paintComponent(g);
            }
        }
        
        //Timing :
        /*
        long timeMeasure = System.currentTimeMillis() - startTime;
        if(min == -1 || timeMeasure<min){
            min = timeMeasure;
        }
        if(max == -1 || timeMeasure>max){
            max = timeMeasure;
        }
        
        if(timeMeasure >40){
            moy+=timeMeasure;
            c++;
        }
        cez++;
        //System.out.println(System.currentTimeMillis() - startTime);
        */
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public CMap getMap() {
        return map;
    }

    public TheComputingThread getThread() {
        return thread;
    }

    /**
     * Refreshes healthpoints for each and every cell by calling the speciefied method
     * Also activate the cell each player is on (killing it, etc...)
     */
    public void refreshHealthPoints() {
        if (adv < 2) {
            //ArrayList<Cell> myMap = map.getMyMap();
            Cell c = map.getFirstCell();
            for (int j = 0; j < map.getMapSize(); j++) {
                c.refreshHealthPoints(this);
                c = c.getNextInMap();
            }

            
        }
    }

    /**
     * Updates the number of cell each team has
     */
    public void updateCellsByOwner() {
        // Reset numbers
        for (int j = 0; j < teams.size(); j++) {
            Team te = teams.get(j);
            te.setNCells(0);
        }
        // Read the map
        //ArrayList<Cell> cells = map.getMyMap();
        Cell c = map.getFirstCell();
        for (int i = 0; i < map.getMapSize(); i++) {
            // Get the owner
            if (c.getOwner() != null) {
                // Update owner's value
                Team te = c.getOwner();
                te.setNCells(te.getNCells() + 1);
            }
            c = c.getNextInMap();
        }

    }


    public ScoreBar getScoreHandler() {
        return scoreHandler;
    }

    public int getRWidth() {
        return this.getWidth();
    }

    /**
     * Initializes teams with the wichTeam string
     */
    private void initTeams() {
        // read the string
        for (int i = 0; i < wichTeam.length(); i += 2) {
            int idT = Integer.parseInt(""+wichTeam.charAt(i));
            // Create new team if needed
            if(idT>=teams.size()){
                Team te = new Team();
                teams.add(idT, te);
            }
        }
    }
    
    /**
     * Initializes teams with the values given by the versus menus
     * @param playerSelect the players selected in versus menus
     */
    private void initTeams(ArrayList<PlayerSelect> playerSelect) {
        // Read info
        for (int i = 0; i < playerSelect.size(); i ++) {
            int idT = playerSelect.get(i).getTeam();
            // Create team if needed
            if(idT>=teams.size()){
                Team te = new Team();
                teams.add(idT, te);
            }
        }
    }

    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    /**
     * Tells the objects of the game to execute. Ex : make the arrow move
     */
    public void computeObjects() {
        // Get objects
        for(int j=0;j<objects.size();j++){
            Element e = objects.get(j);
            // Make them execute action
            if(e instanceof Arrow){
                Arrow a = (Arrow)e;
                a.effect();
            }
        }
    }
    
    /**
     * Executes the animations
     */
    public void computeAnimations(){
        // Get animations
        Animation npcmessage = null;
        for(int j=0;j<anims.size();j++){
            Animation a = anims.get(j);
            
            // Detect double NPC messages
            if(a instanceof NPCMessage){
                npcmessage = a;
            }
            // Execute them
            if(a.isRunning()){
                a.executeAnimation();
            }
        }
    }
    
    /**
     * Tells the players to handle their keys. This method makes them move according to what key is pressed
     */
    public void playerHandleKeys(){
        for(int i=0;i<players.size();i++){
            players.get(i).handleKeys();
        }
    }
    
    /**
     * Add object to the Game
     * @param e the object
     */
    public void addObject(Element e){
        objects.add(e);
    }
    
    /**
     * Add animation to the Game
     * @param e the animation
     */
    public void addAnim(Animation e){
        anims.add(e);
    }
    
    /**
     * Remove an existing object from the game
     * @param e the object
     */
    public void deleteObject(Element e){
        if(objects.contains(e)){
            objects.remove(e);
        }
    }
    
    /**
     * Remove an existing animation from the game
     * @param e the animation
     */
    public void deleteAnim(Animation e){
        if(anims.contains(e)){
            anims.remove(e);
        }
    }

    /**
     * Tells the FSMs to compute states, move, etc...
     */
    public void executeFSMs() {
        for(int i=0;i<players.size();i++){
            Player p = players.get(i);
            // If this player has a FSM, tell it to execute
            if(p.getFsm() != null){
                //fsmThread
                fsmThread.add(new FSMThread(this,p));
                fsmThread.get(fsmThread.size()-1).start();
                //p.getFsm().executeMethod();
            }
        }
    }

    /**
     * Initializes the Key Listener of the game
     */
    private void initKListener() {
        deleteKListener();
        kl = new KListener(this);
        this.addKeyListener(kl);
    }
    
    private void deleteKListener(){
        this.removeKeyListener(kl);
        kl = new KListener(this);
    }

    /**
     * Request the focus so that the K listener works
     */
    public void initFocus() {
        initKListener();
        this.setFocusable(true);
        this.initListNPCs(map.getFileID());
        this.requestFocus();
    }
    
    public void releaseFocus(){
        deleteKListener();
        thread.setRunning(false);
        this.setFocusable(false);
    }
    
    /**
     * Tests if the victory was reached by a Team. if yes, returns it
     * @return the winner team (or null)
     */
    public void testVictory() {
        Team p= null;
        //test for each player
        for (int i = 0; i < teams.size(); i++) {
            Team te = teams.get(i);
            //place here victory condition
            int score = te.getScore();
            int tilesOwned = te.getNCells();
            int totalTile = map.getTakableCells().size();
            //pass the time test
            //if(victTime!=0 && dateG <= victTime*fpsa){
            //Pass the score test
            if (score != 0 && score >= victScore) {
                //Pass the tile test
                if (((double)tilesOwned) / totalTile >= victTile) {
                    if(p!=null){
                        endGame(null);
                        return;
                    }else{
                        p = te;
                    }
                }
            }
            //}
            if (victTime != 0 && this.getThread().getCount() > victTime * 1000) {
                System.out.println("Time out !");
                endGame(null);
                return;
            }
        }
        
        
        if(p != null){
            endGame(p);
            return;
        }
    }
    
    /**
     * Pauses or un-pauses the game
     */
    
    public void pauseGame(){
        pauseGame(false);
    }
    
    public void pauseGame(boolean isNPC){
        this.pauseNPC = isNPC;
        // If running, pause
        if(thread.getRunning()){
            thread.setRunning(false);
            this.repaint();
            // Display pause
            if(!isNPC){
            PauseScreen pauseScreen = new PauseScreen(false, this);
                if(adv > 0){
                    pauseScreen.setAdvMode(true);
                } 
                this.addObject(pauseScreen);
            }
        }
        // Else un-pause if the game is still not finished
        else if(!gameEnded && !isNPC){
            // display countdown
            for(int j=0;j<objects.size();j++){
                Element e = objects.get(j);
                if(e instanceof PauseScreen){
                    PauseScreen ps = (PauseScreen)e;
                    if(!ps.isResuming()){
                        ps.setResuming(true);
                        new PauseCountDown(Params.pauseDuration,thread);
                    }
                }
            }
            
        }
        else if(isNPC){
            // Get animations
            for(int j=0;j<anims.size();j++){
                Animation a = anims.get(j);
                // Execute them
                if(a instanceof NPCMessage){
                    a.endAnimation();
                }
            }            
            removePauseScreen();
            thread.setRunning(true);
        }
    }
    
    public void removePauseScreen(){
        for(int j=0;j<objects.size();j++){
            Element e = objects.get(j);
            if(e instanceof PauseScreen){
                ((PauseScreen) e).setResuming(true);
                ((PauseScreen) e).exit();
                deleteObject(e);
            }
        }
    }
    
    /**
     * Ends the game and displays winner
     * @param winner the team who won (or null, that would mean tie or lost (for adventure))
     */
    public void endGame(Team winner){
        /*
        System.out.println("------- Statistics -------");
        System.out.println("Min, Moy, Max, count>40, countTotal");
        System.out.println(this.thread.min+","+this.thread.moy/this.thread.c+","+this.thread.max+","+this.thread.c+","+this.thread.cez);
        System.out.println("Refresh HP : "+this.thread.timeRefresh+" : "+((double)this.thread.timeRefresh)/this.thread.max);
        System.out.println("Update Cells : "+this.thread.timeUpdateCellsByOwner+" : "+((double)this.thread.timeUpdateCellsByOwner)/this.thread.max);
        System.out.println("Keys : "+this.thread.timeHandleKeys+" : "+((double)this.thread.timeHandleKeys)/this.thread.max);
        System.out.println("------- Graphical");
        System.out.println("Min, Moy, Max, count>40, countTotal");
        System.out.println(""+this.min+","+this.moy/this.c+","+this.max+","+this.c+","+this.cez);
        */
        pauseGame();
        PauseScreen victoryScreen = null;
        for(int j=0;j<objects.size();j++){
            Element e = objects.get(j);
            if(e instanceof PauseScreen){
                victoryScreen = (PauseScreen) e;
            }
        }
        victoryScreen.setDisplayVictory(true);
        victoryScreen.setQuickPlayMode(this.quickPlay);
        victoryScreen.setWinner(winner);
        gameEnded = true;
        
        //Reset pathfinding static map
        pathFinder.setTheMap(null);
        
        // versus mode
        if(adv == 0){
            victoryScreen.setAdvMode(false);
            if(winner == null){
                System.out.println("Tie !");
                victoryScreen.setMessage("Match Nul !");
            }
            else{
                victoryScreen.setMessage("Fin du match !");
                //System.out.println("Winner : "+winner);
            }
        }
        else{
            victoryScreen.setAdvMode(true);
            // adventure mode : if the winner isn't the player, then display lost
            Team thePlayer = teams.get(0);
            if(thePlayer == winner){
                // Change and save gameDatas
                this.gameSave = Tools.loadGame();
                Map<String,Integer> mapValues = gameSave.getMapValues();
                mapValues.put(this.WMcellHash,2);
                Tools.saveGame(this.gameSave);
                Fapplication.getWorldMap().initWorldMap();
                // display victory : (wich will have its own back to world map)
                victoryScreen.setMessage("Victoire !");
                //System.out.println("Yay, Victory !");
            }
            else{
                // Display defeat :(wich will have its own back to world map)
                victoryScreen.setMessage("Perdu...");
                victoryScreen.setWinner(null);
                //System.out.println("You lost");
                // Don't change anything
            }
            //new AStartGame(Fapplication.getWorldMap()).execute(null);
        }
        this.repaint();
    }
    
    /**
     * Initializes the players according to the WhoIsPlaying String and friends (isFSM, etc...)
     */
    private void initPlayers() {
        for (int i = 0; i < whoIsPlaying.length(); i += 2) {
            //Note : the number in "isPlaying" is also the character of the player. If 0, the player is disabled
            int charac = Integer.parseInt(""+whoIsPlaying.charAt(i));
            int controler = Integer.parseInt(""+controlers.charAt(i));
            boolean isPlaying = charac!=0;
            if (isPlaying) {
                // Initializes ID
                int pid = i / 2;
                // Initializes startCell
                Cell c = getStartCell(pid);
                // Get team (previously created)
                Team team = teams.get(Integer.parseInt(""+wichTeam.charAt(i)));
                // Parse fsm level if there is
                int ai = Integer.parseInt(""+isFSM.charAt(i));
                // Create player
                Player p = generatePlayer(charac,pid,c,team,ai,controler,this);
                // Add it
                players.add(p);
            }
        }
    }
    
    /**
     * Initializes the players according to the datas received from the versus menus
     */
    private void initPlayers(ArrayList<PlayerSelect> playerSelect) {
        // Get info
        for (int i = 0; i < playerSelect.size(); i++) {
            PlayerSelect ps = playerSelect.get(i);
            //Note : the number in "isPlaying" is also the character of the player. If 0, the player is disabled
            int charac = ps.getPc();
            int pid = players.size();
            Cell c = getStartCell(pid);
            Team team = teams.get(ps.getTeam());
            int ai = ps.getIsFSM();
            Player p = generatePlayer(charac, pid, c, team, ai, ps.getControler(),this);

            players.add(p);
        }
    }
    
    /**
     * Get a start cell for the designated player id
     * @param pid the id of the player to be placed
     * @return the start Cell for this player
     */
    private Cell getStartCell(int pid){
        ArrayList<Cell> startCellsAL = map.getStartCells();
        Cell c;
        // If the start cells are randomly mixed
        if (randStart) {
            // Get a cell from the list
            if (startCellsAL.size() > 0) {
                int rand = Tools.randRange(0, startCellsAL.size() - 1);
                c = startCellsAL.get(rand);
                startCellsAL.remove(rand);
            }
            // if no more, find randomly a cell
            /*
             * This should not be used ! All map should have 8 start cells defined (but this is safe)
             */
            else {
                ArrayList<Cell> takable = map.getTakableCells();
                do {
                    c = takable.get(Tools.randRange(0, takable.size() - 1));
                } while (c.getOccupied() != null);
            }
        } else {
            // The start cells are for a designated player, get him his cell
            c = startCellsAL.get(pid);
        }
        return c;
    }
    
    /**
     * Generate a player according to diverses parameters
     * @param charac The class (skill, character) of the player
     * @param pid An ID
     * @param c The start cell
     * @param team His team
     * @param ai His Artificial Intelligence level (0 if human)
     * @param controler His keyboard set
     * @return The created player
     */
    public static Player generatePlayer(int charac, int pid, Cell c, Team team, int ai, int controler, Game game){
        Player p;
        switch(charac){
            case 1:
                p = new Knight(pid, c, game,team,ai,controler);
                break;
            case 3:
                p = new Miner(pid, c, game,team,ai,controler);
                break;
            case 4:
                p = new Warlock(pid, c, game,team,ai,controler);
                break;
            case 5:
                p = new Archer(pid, c, game,team,ai,controler);
                break;
            case 6:
                p = new Vampire(pid, c, game,team,ai,controler);
                break;
            case 7:
                p = new NoCharacter(pid, c, game,team,ai,controler);
                break;
            case 8:
                p = new Magician(pid, c, game,team,ai,controler);
                break;
            case 9:
                p = new Booster(pid, c, game,team,ai,controler);
                break;
        default:
            p = new Knight(pid, c, game,team,ai,controler);
            break;
        }
        return p;
    }
    
// The game, you just lost

    public void setAdv(int adv) {
        this.adv = adv;
    }

    public int getAdv() {
        return adv;
    }

    public void initWorldMap() {
        this.listNPCs = new ArrayList<NPC>();
        // Linking maps to Cells
        
        Params.mapList = Tools.loadWMGameConfigMap();


        // Get list of conquered cells
        this.computeWorldMap();
        ArrayList<String> listJustAvailableCells = getListOfWMCells(1);
        ArrayList<String> listConqueredCells = getListOfWMCells(2);
        ArrayList<String> listNotAvailableCells = getListOfWMCells(0);
        /*System.out.println("Just Av : "+listJustAvailableCells);
        System.out.println("Not Av : "+listNotAvailableCells);
        System.out.println("Conquered : "+listConqueredCells);*/
        // Create NPCs to cover the designated cells
        for(int i=0;i<listNotAvailableCells.size();i++){
            String cellHash = listNotAvailableCells.get(i);
            this.addNPC(new NPCWMBlocking(cellHash));
        }
        
        for(int i=0;i<listJustAvailableCells.size();i++){
            String cellHash = listJustAvailableCells.get(i);
            //if(Tools.intTableContains(Constants.listAdvMaps, mapID)){
                this.addNPC(new NPCWMStarting(cellHash,false,Params.mapList.get(cellHash)));
            //}
        }
        
        for(int i=0;i<listConqueredCells.size();i++){
            String cellHash = listConqueredCells.get(i);
            //if(Tools.intTableContains(Constants.listAdvMaps, mapID)){
                this.addNPC(new NPCWMStarting(cellHash,true,Params.mapList.get(cellHash)));
            //}
        }
        
        this.triggerTriggerNPCs();
    }
    
    public void computeWorldMap(){
        //Load game :
        this.gameSave = Tools.loadGame();
        // Init map parents
        
        Map<String,ArrayList<String>> mapParents = Tools.loadWMParentCells();
        
        
        
        // Get conquered cell
        ArrayList<String> listConqueredCells = getListOfWMCells(2);
        Map<String, Integer> mapValues = gameSave.getMapValues();
        for (int i = 0; i < Constants.listAdvMaps.length; i++) {
            String cellHash = Constants.listAdvMaps[i];
            int mapConquest = mapValues.get(cellHash);
            ArrayList<String> parents = mapParents.get(cellHash);
            boolean available = true;
            if (parents != null && parents.size() > 0) {
                for (int j = 0; j < parents.size(); j++) {
                    if (!listConqueredCells.contains(parents.get(j))) {
                        available = false;
                        break;
                    }
                }
            }
            if(available && mapConquest == 0){
                gameSave.getMapValues().put(cellHash, 1);
            }
        }
        
        //Unlock characters
        for(int i=1;i<=9;i++){
            gameSave.getUnlockedChars().set(i, gameSave.getGameSwitches().get(i));
        }
        Tools.saveGame(this.gameSave);
    }
    
    public ArrayList<String> getListOfWMCells(int value){
        Map<String, Integer> mapValues = gameSave.getMapValues();
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < Constants.listAdvMaps.length; i++) {
            int mapConquest = mapValues.get(Constants.listAdvMaps[i]);
            //System.out.println(mapConquest);
            if(mapConquest == value){
                list.add(Constants.listAdvMaps[i]);
            }
        }
        return list;
    }

    public void setGameSave(GameSave gameSave) {
        this.gameSave = gameSave;
    }

    public GameSave getGameSave() {
        return gameSave;
    }

    public void setListNPCs(ArrayList<NPC> listNPCs) {
        this.listNPCs = listNPCs;
    }

    public ArrayList<NPC> getListNPCs() {
        return listNPCs;
    }

    public void addNPC(NPC e){
        if(!this.listNPCs.contains(e)){
            listNPCs.add(e);
        }
    }
    public void addTriggerNPC(NPC e){
        if(!this.listTriggerNPCs.contains(e)){
            listTriggerNPCs.add(e);
        }
    }

    public void initListNPCs(int nmap) {
        this.pauseNPC = false;
        if(this.listNPCs.size()>0){
            for(int i=0;i<listNPCs.size();i++){
                listNPCs.get(i).reInit();
            }
        }
        else{
            
        }
        
        if(nmap == 0){
            initWorldMap();
        }


            if(Params.npcBank == null){
                Params.npcBank = Tools.loadWMNPCBank();
                Params.npcBacklogBank = Tools.loadWMNPCBLBank();
            }
            ArrayList<NPC> secList = Params.npcBank.get(nmap);
            if(secList == null && adv>0){
                System.err.println("Couldn't find NPC list for map no "+nmap);
            }
            else{
                for(int i=0;i<secList.size();i++){
                    this.addNPC(secList.get(i));
                }
                ArrayList<NPC> secBList = Params.npcBacklogBank.get(nmap);
                if(secBList != null){
                    for(int i=0;i<secBList.size();i++){
                        this.addTriggerNPC(secBList.get(i));
                    }
                }
            //this.listNPCs = new ArrayList<NPC>();
            }
            
        
        for(int i=0;i<listNPCs.size();i++){
            this.listNPCs.get(i).setTransientValues(this);
        }
        for(int i=0;i<listTriggerNPCs.size();i++){
            this.listTriggerNPCs.get(i).setTransientValues(this);
        }
    }

    public void setPauseNPC(boolean pauseNPC) {
        this.pauseNPC = pauseNPC;
    }

    public boolean isPauseNPC() {
        return pauseNPC;
    }

    public void setObjects(ArrayList<Element> objects) {
        this.objects = objects;
    }

    public ArrayList<Element> getObjects() {
        return objects;
    }

    public void setKl(KListener kl) {
        this.kl = kl;
    }

    public KListener getKl() {
        return kl;
    }

    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public void setQuickPlay(boolean quickPlay) {
        this.quickPlay = quickPlay;
    }

    public boolean isQuickPlay() {
        return quickPlay;
    }

    public void setWMcellHash(String WMcellHash) {
        this.WMcellHash = WMcellHash;
    }

    public String getWMcellHash() {
        return WMcellHash;
    }

    public void setListTriggerNPCs(ArrayList<NPC> listTriggerNPCs) {
        this.listTriggerNPCs = listTriggerNPCs;
    }

    public ArrayList<NPC> getListTriggerNPCs() {
        return listTriggerNPCs;
    }
    
    public void triggerTriggerNPCs(){
        for(int i=0;i<listTriggerNPCs.size();i++){
            NPC trig = listTriggerNPCs.get(i);
            if(!trig.isExecuting()){
                trig.execute();
            }
        }
    }
}
