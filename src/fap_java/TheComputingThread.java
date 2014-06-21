package fap_java;

import java.io.Serializable;

import java.util.ArrayList;

/**
 * This thread will handle everything in updating the Game.
 * it will take care of player displacements, FSM computations, repainting the panel, refreshing HealthPoints
 * of cells, ...
 * It is running constantly except if someone pauses the game.
 * @see Game
 */
public class TheComputingThread implements Runnable{
    
    /**
     * The game where all is played
     */
    private Game myGame;
    /**
     * Sets if the thread is running (and btw the game)
     */
    private boolean running;
    /**
     * The delay between each clock-tick
     */
    private int delay = Params.delay;     // in ms
    /**
     * Counts the number of ms of the game
     */
    private int count;
    
    private boolean lanMode;   
    
    public static long min=-1,max=-1,moy=0,c=1;
    public static long timeRefresh=0,timeUpdateCellsByOwner=0,timeHandleKeys=0,timeFSMs=0;
    
    /**
     * Initializes Thread
     * @param myGame : the game to control
     */
        
    public TheComputingThread(Game myGame) {
        lanMode = false;
        this.myGame = myGame;
        this.running = false;
        count = 0;
    }
    
    /**
     * Toggles the running of the thread
     * @param running : boolean value
     */
    public void setRunning(boolean running){
        this.running = running;
    }
    
    /**
     * Finds if the thread is running
     * @return : boolean value
     */
    public boolean getRunning(){
        return this.running;
    }
    
    /**
     * Sets the delay between two clock tick
     * @param delay : number of ms
     */
    public void setDelay(int delay) {
        this.delay = delay;
    }

    /**
     * Get the delay between two clock ticks
     * @return : delay in ms
     */
    public int getDelay() {
        return delay;
    }
    
    /**
     * Handle animation and controls of the game
     */
    public void run(){
        while(true){        // infinite loop
            // if the thread is running
            if(running){
                // Execute tasks (generate a clock-tick)
                execute(true);
            }
            else if(myGame.getAdv()>0 && myGame.isPauseNPC()){
                // Execute some tasks (key handling)
                execute(false);
            }
            // Always execute the animations
            myGame.computeAnimations();
            try{
                // wait for "delay" ms
                Thread.sleep(delay);
            } catch (InterruptedException ie){
                ie.printStackTrace();
            }
        }
    }

    /**
     * Get the number of ms since the game started
     * @return : an integer with the number of ms
     */
    public int getCount() {
        return count;
    }
    
    /**
     * Execute actions in the game, such as updating values
     */
    private void execute(boolean full){
        long startTime = System.currentTimeMillis();
        long timeHP=0;
        long timeUpdateCells=0;
        long timeKeys=0;
        // Update the time of the game
        count += delay;
        // Counts the clockTicks
        int frame = count / delay;  
        if(!lanMode && count == 2*delay){       // actual first frame of the game since 1*delay is init
            if(myGame.getAdv()>0){
                NPC npc = Tools.checkAutoTriggerNPC(myGame);
                if(npc !=null){
                    npc.execute();
                }
            }
            myGame.executeFSMs();
        }
        if(full){
            timeHP = System.currentTimeMillis();
            // commands to refresh healthPoints
            myGame.refreshHealthPoints();
            timeHP = System.currentTimeMillis() - timeHP;
            
            timeUpdateCells = System.currentTimeMillis();
            for (int i = 0; i < myGame.getPlayers().size(); i++) {
                Player p = myGame.getPlayers().get(i);
                Cell c = p.getCurrent();
                c.activateCell(p);
            }
            timeUpdateCells = System.currentTimeMillis() - timeUpdateCells;
            
            // Every 4 frames
            if (!lanMode && frame % 4 == 0) {
                // Count the number of cells a Player has
                myGame.updateCellsByOwner();
            }
            

            // When it's time to check scores
            if (!lanMode && myGame.getAdv() < 2 && count % (1000 * Params.giveScore) == 0) {
                // Scores are updated
                myGame.getScoreHandler().computeScores();
            }

            myGame.computeObjects();
            //myGame.executeFSMs();

            // Testing
        }
        //Execute other objects actions
        if (!lanMode && count % 2 == 0) {
            myGame.playerHandleKeys();
        }
        timeKeys = System.currentTimeMillis() - timeKeys;
        
        //Timing :
        long timeMeasure = System.currentTimeMillis() - startTime;
        if(min == -1 || timeMeasure<min){
            min = timeMeasure;
        }
        if(max == -1 || timeMeasure>max){
            max = timeMeasure;
            this.timeHandleKeys = timeKeys;
            this.timeRefresh = timeHP;
            this.timeUpdateCellsByOwner = timeUpdateCells;
        }
        moy+=timeMeasure;
        if(timeMeasure >0){
            c++;
        }
        //System.out.println(timeMeasure);
    }

    public void setMyGame(Game myGame) {
        this.myGame = myGame;
    }

    public Game getMyGame() {
        return myGame;
    }

    public void setLanMode(boolean lanMode) {
        this.lanMode = lanMode;
    }

    public boolean isLanMode() {
        return lanMode;
    }
}
