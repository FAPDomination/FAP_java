package fap_java;

import animations.*;

import characters.*;

import gui.Constants;

import gui.Fapplication;
import gui.GameSave;
import gui.NeedingFocus;
import gui.Displayer;

import gui.FAPanel;
import gui.PlayerSelect;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;

import java.awt.Graphics2D;

import java.io.Serializable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;


import java.util.ArrayList;

import java.util.HashMap;

import java.util.Map;

import javax.swing.JPanel;

import npcs.NPCWMBlocking;
import npcs.NPCWMStarting;

import npcs.actions.*;

public class Game extends JPanel implements Serializable{

    @SuppressWarnings("compatibility:-5515731280068496856")
    private static final long serialVersionUID = 1L;

    /**
     * The map of this Game. Contains a grid of cells and a set of methods to play with them
     */
    private CMap map;
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
    private ArrayList<Player> players = new ArrayList<Player>();
    
    /**
     * The scorebars manager
     */
    private ScoreBar scoreHandler;
    /**
     * Contains every team of the game
     */
    private ArrayList<Team> teams = new ArrayList<Team>();
    
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
    
    private boolean pauseNPC;
    
    private transient Displayer displayer;

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
     * Initializes a LAN Waiting Room
     */
    public Game(){
        //Map ID 19 is the LAN waiting room
        this("0","0","0","0",true,19,0,0,0,2);
        
        teams = new ArrayList<Team>();
        for(int i=0;i<Params.maxPlayers;i++){
            teams.add(new Team());
        }
    }
    
    /**
     * Initializes a game based on the waiting room, and the new map to load
     * @param game
     * @param nMap
     */
    public Game(Game game, int nMap, int victScore){

        this.victScore = victScore;
        this.victTile = 0;
        this.victTime = 0;
        initGame(nMap);
        this.randStart = true;
        this.adv = 0;
        
        players = new ArrayList<Player>();
        //teams = (ArrayList<Team>)game.getTeams().clone();
        teams = new ArrayList<Team>();
        displayer = game.getDisplayer();
        
        for(int i=0;i<game.getTeams().size();i++){
            Team orig = game.getTeams().get(i);
            if(orig.getPlayersInThisTeam().size() != 0){
                teams.add(new Team());
            }
            else{
                teams.add(null);
            }
        }
        
        for(int i=0;i<game.getPlayers().size();i++){
            Player p = game.getPlayers().get(i);
            int ai = 0;
            if(p.getFsm() != null){
                ai = p.getFsm().getLevel();
            }
            Team t = teams.get(game.getTeams().indexOf(p.getTeam()));
            Player q = this.generatePlayer(p.getPc(), p.getId(), this.getStartCell(p.getId()), t, ai, 0, this);
            players.add(q);
        }
        
        teams = Tools.removeNull(teams);
        //displayer.setGame(this);
        
        //TODO place player
        
        scoreHandler = new ScoreBar(this);
        
        displayer.repaint();
    }
    
    /**
     * Init common game-related stuff, such as the map, thread, and pauses the game
     * @param nmap The number of the file the map is in
     */
    public void initGame(int nmap){
        this.displayer = new Displayer(this);
        this.listNPCs = new ArrayList<NPC>();
        // Parse the map
        map = new CMap(this,nmap);
        map = XMLparser.parseMap(nmap,this);
        
        //Parse ParamTable
        XMLparser.parseParams();
        
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
        if(adv <2){
            // Repaint the scoreHandler
            this.scoreHandler.paintComponent(g);
        }
        // Repaint the objects (such as arrows)
        for(int j=0;j<objects.size();j++){
            objects.get(j).paintComponent(g);
        }
        
        /*
        if(map.getFileID() == 0){
            // if world map, paint the npcs with green line
            for(int i=0;i<listNPCs.size(); i++){
                NPC npc = listNPCs.get(i);
                if(npc instanceof NPCWMStarting){
                    npc.paintComponent(g);
                }
            }
        }*/
        
        /*
        // Paint black screen if the game is paused
        if(!thread.getRunning() && !pauseNPC){
            g.drawImage(Graph.guimg.get("pauseScreen"), 0, 0,this.getWidth(),this.getHeight(), this);
        }
        */
        // Paint the animations (warps, explosions, bitches,...)
        for(int j=0;j<anims.size();j++){
            if(thread.getRunning() || anims.get(j) instanceof PauseCountDown || anims.get(j) instanceof NPCMessage){
                anims.get(j).paintComponent(g);
            }
        }
        
        //System.out.println(System.currentTimeMillis() - startTime);
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
            ArrayList<Cell> myMap = map.getMyMap();
            for (int j = 0; j < myMap.size(); j++) {
                Cell c = myMap.get(j);
                c.refreshHealthPoints(this);
            }

            for (int i = 0; i < players.size(); i++) {
                Player p = players.get(i);
                Cell c = p.getCurrent();
                c.activateCell(p);
            }
        }
    }

    /**
     * Checks is the designated cell is occupied (someone stands on it)
     * @param c The cell that needs to be tested
     * @return the player that stand on it (or null)
     */
    public Player isOccupied(Cell c) {
        Player p = null;
        // Check if the cell is indeed in the map first
        if (c != null && map.containsCell(c) != -1) {
            // Check for each player
            for (int i = 0; i < players.size(); i++) {
                Player q = players.get(i);
                // Check position
                if (q.getI() == c.getI() && q.getJ() == c.getJ()) {
                    p = q;
                    break;      // Escape !
                }
            }
        }
        return p;
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
        ArrayList<Cell> cells = map.getMyMap();
        for (int i = 0; i < cells.size(); i++) {
            Cell c = cells.get(i);
            // Get the owner
            if (c.getOwner() != null) {
                // Update owner's value
                Team te = c.getOwner();
                te.setNCells(te.getNCells() + 1);
            }
        }

    }


    public ScoreBar getScoreHandler() {
        return scoreHandler;
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
        for(int j=0;j<anims.size();j++){
            Animation a = anims.get(j);
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
                p.getFsm().executeMethod();
            }
        }
    }

    
    /**
     * Tests if the victory was reached by a Team. if yes, returns it
     * @return the winner team (or null)
     */
    public Team testVictory() {
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
                    p = te;
                }
            }
            //}
            if (victTime != 0 && this.getThread().getCount() > victTime * 1000) {
                System.out.println("Time out !");
                endGame(null);
            }
        }
        return p;
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
            displayer.repaint();
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
                    ((PauseScreen)e).setResuming(true);
                }
            }
            new PauseCountDown(Params.pauseDuration,thread);
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
                deleteObject(e);
            }
        }
    }
    
    /**
     * Ends the game and displays winner
     * @param winner the team who won (or null, that would mean tie or lost (for adventure))
     */
    public void endGame(Team winner){
        pauseGame();
        PauseScreen victoryScreen = null;
        for(int j=0;j<objects.size();j++){
            Element e = objects.get(j);
            if(e instanceof PauseScreen){
                victoryScreen = (PauseScreen) e;
            }
        }
        victoryScreen.setDisplayVictory(true);
        victoryScreen.setWinner(winner);
        gameEnded = true;
        // versus mode
        if(adv == 0){
            victoryScreen.setAdvMode(false);
            if(winner == null){
                System.out.println("Tie !");
                //victoryScreen.setMessage("Match Nul !");
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
                Map<Integer,Integer> mapValues = gameSave.getMapValues();
                mapValues.put(map.getFileID(),2);
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
        displayer.repaint();
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
    public Cell getStartCell(int pid){
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
                } while (this.isOccupied(c) != null);
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
        //TODO put linking in an external file ?
        Params.mapList.put(20, new Cell(18,11));
        Params.mapList.put(21, new Cell(18,10));
        Params.mapList.put(22, new Cell(17,9));
        Params.mapList.put(23, new Cell(17,10));
        Params.mapList.put(24, new Cell(16,10));
        Params.mapList.put(25, new Cell(18,9));
    
        Map<Integer,Game> gameList = new HashMap<Integer,Game>();
        gameList.put(25, new Game(25));
        gameList.put(20, new Game("1,1","0,1","0,1","0,2",false,20,500,0,0,1));
        gameList.put(21, new Game("1,5","0,1","0,1","0,2",false,21,1000,0,0,1));
        gameList.put(22, new Game("1,1,1","0,1,1","0,1,1","0,1,1",false,22,1000,0,0,1));
        gameList.put(23, new Game("1,1","0,1","0,1","0,2",false,23,1000,0,0,1));
        gameList.put(24, new Game("1,1,1","0,1,2","0,1,1","0,1,2",false,24,1000,0,0,1));
        
        //Test
        Game ga = gameList.get(20);
        try {
            FileOutputStream fileOut = new FileOutputStream("game.ga");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(ga);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
        //-----------

        // Get list of conquered cells
        this.computeWorldMap();
        ArrayList<Integer> listJustAvailableCells = getListOfWMCells(1);
        ArrayList<Integer> listConqueredCells = getListOfWMCells(2);
        ArrayList<Integer> listNotAvailableCells = getListOfWMCells(0);
        /*System.out.println("Just Av : "+listJustAvailableCells);
        System.out.println("Not Av : "+listNotAvailableCells);
        System.out.println("Conquered : "+listConqueredCells);*/
        // Create NPCs to cover the designated cells
        for(int i=0;i<listNotAvailableCells.size();i++){
            int mapID = listNotAvailableCells.get(i);
            Cell indexes = Params.mapList.get(mapID);
            Cell pos = this.map.getCell((int)indexes.getI(), (int)indexes.getJ());
            this.addNPC(new NPCWMBlocking(pos));
        }
        
        for(int i=0;i<listJustAvailableCells.size();i++){
            int mapID = listJustAvailableCells.get(i);
            //if(Tools.intTableContains(Constants.listAdvMaps, mapID)){
            Cell indexes = Params.mapList.get(mapID);
            Cell pos = this.map.getCell((int)indexes.getI(), (int)indexes.getJ());
                this.addNPC(new NPCWMStarting(pos,false,this,gameList.get(mapID)));
            //}
        }
        
        for(int i=0;i<listConqueredCells.size();i++){
            int mapID = listConqueredCells.get(i);
            //if(Tools.intTableContains(Constants.listAdvMaps, mapID)){
            Cell indexes = Params.mapList.get(mapID);
            Cell pos = this.map.getCell((int)indexes.getI(), (int)indexes.getJ());
                this.addNPC(new NPCWMStarting(pos,true,this,gameList.get(mapID)));
            //}
        }
    }
    
    public void computeWorldMap(){
        //Load game :
        this.gameSave = Tools.loadGame();
        // Init map parents
        //TODO put parents in an external file ?
        Map<Integer,ArrayList<Integer>> mapParents = new HashMap<Integer,ArrayList<Integer>>();
        for(int i=0;i<Constants.listAdvMaps.length;i++){
            mapParents.put(Constants.listAdvMaps[i], new ArrayList<Integer>());
        }
        // Fill map parents
        /*mapParents[20] = [0];
        mapParents[21] = [0];
        mapParents[22] = [1,21,20];
        mapParents[23] = [1,21,22];
        mapParents[24] = [2,23,20];
        mapParents[25] = [0];*/
        
        mapParents.get(22).add(20);
        
        mapParents.get(23).add(21);
        mapParents.get(23).add(22);
        
        mapParents.get(24).add(23);
        // This line doesn't do much, does it ?
        mapParents.get(24).add(20);
        
        // Get conquered cell
        ArrayList<Integer> listConqueredCells = getListOfWMCells(2);
        Map<Integer, Integer> mapValues = gameSave.getMapValues();
        for (int i = 0; i < Constants.listAdvMaps.length; i++) {
            int mapID = Constants.listAdvMaps[i];
            int mapConquest = mapValues.get(mapID);
            ArrayList<Integer> parents = mapParents.get(mapID);
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
                gameSave.getMapValues().put(mapID, 1);
            }
        }
        
        //Unlock characters
        for(int i=1;i<=9;i++){
            gameSave.getUnlockedChars().set(i, gameSave.getGameSwitches().get(i));
        }
        Tools.saveGame(this.gameSave);
    }
    
    public ArrayList<Integer> getListOfWMCells(int value){
        Map<Integer, Integer> mapValues = gameSave.getMapValues();
        ArrayList<Integer> list = new ArrayList<Integer>();
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

    public void initListNPCs(int nmap) {
        this.pauseNPC = false;
        if(this.listNPCs.size()>0){
            for(int i=0;i<listNPCs.size();i++){
                listNPCs.get(i).reInit();
            }
        }
        else{
            
        }
            //TODO Initialize the list of all NPCs according to the nmap
            switch(nmap){
            case 0:
                initWorldMap();
                break;
            case 21:
                ArrayList<Action> list3 = new ArrayList<Action>();
                list3.add(new ADisplayMessage("Willkommen dans l'aventure, jeune fougeux !"));
                list3.add(new ADisplayMessage("Appuie sur SKILL pour continuer"));
                list3.add(new ADisplayMessage("Ah mais chui con ! Tu viens de le faire !"));
                NPC npc21 = new NPC(list3,this);
                this.addNPC(npc21);
                break;
            case 25:
                ArrayList<Action> list = new ArrayList<Action>();
                list.add(new AAsk("Ya want da switch ?","Yeah","Nup", new ADisplayMessage("Wat a sheime")));
                list.add(new ASetSwitch(0,true));
                list.add(new ADisplayMessage("I'ma doing it right now !"));
                NPC npc = new NPC(map.getCell(10, 10),false,false,Graph.list.get("NPC_sample"),this,6,-17,list);
                this.addNPC(npc);
                
                
                ADisplayMessage ad = new ADisplayMessage("Ya do not interrest-a me");
                ArrayList<Action> list2 = new ArrayList<Action>();
                list2.add(new ATestSwitch(0,ad));
                list2.add(new ADisplayMessage("Ye have da switch !! Gloria !"));
                list2.add(new AModifyCell(map.getCell(17,10),"100"));
                NPC npc2 = new NPC(map.getCell(15, 8),false,false,Graph.list.get("NPC_sample"),this,6,-17,list2);
                this.addNPC(npc2);
                
                ArrayList<Action> list4 = new ArrayList<Action>();
                list4.add(new ASetSwitch(1,true));
                list4.add(new ADisplayMessage("I'll activate the knight for ya"));
                NPC npc4 = new NPC(map.getCell(20, 9),false,false,Graph.list.get("NPC_sample"),this,6,-17,list4);
                this.addNPC(npc4);
                
                ArrayList<Action> list5 = new ArrayList<Action>();
                list5.add(new ASetSwitch(5,true));
                list5.add(new ADisplayMessage("I'll activate the Archeeerrr for ya"));
                NPC npc5 = new NPC(map.getCell(27, 19),false,false,Graph.list.get("NPC_sample"),this,6,-17,list5);
                this.addNPC(npc5);
                break;
            default:
                //this.listNPCs = new ArrayList<NPC>();
                System.out.println("Couldn't find NPC list for map no "+nmap);
                break;
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

    public void setAnims(ArrayList<Animation> anims) {
        this.anims = anims;
    }

    public ArrayList<Animation> getAnims() {
        return anims;
    }

    public void setDisplayer(Displayer displayer) {
        this.displayer = displayer;
    }

    public Displayer getDisplayer() {
        return displayer;
    }
}
