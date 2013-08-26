package fap_java;

/**
 * This thread will handle everything in updating the Game.
 * it will take care of player displacements, FSM computations, repainting the panel, refreshing HealthPoints
 * of cells, ...
 * It is running constantly except if someone pauses the game.
 * @see Game
 */
public class TheThread implements Runnable{
    
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
    /**
     * Initializes Thread
     * @param myGame : the game to control
     */
    public TheThread(Game myGame) {
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
                execute();
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
    private void execute(){
        // Update the time of the game
        count += delay;
        // Counts the clockTicks
        int frame = count / delay;
        // commands to refresh healthPoints
        myGame.refreshHealthPoints();
        
        myGame.repaint();
        
        // Every 4 frames
        if(frame % 4 == 0){
            // Count the number of cells a Player has
            myGame.updateCellsByOwner();
        }
        
        // When it's time to check scores
        if(myGame.getAdv() < 2 && count % (1000 * Params.giveScore) == 0){
            // Scores are updated
            myGame.getScoreHandler().computeScores();
        }
        
        //Execute other objects actions
        if(count%2 == 0){
            myGame.playerHandleKeys();
        }
        myGame.computeObjects();
        myGame.executeFSMs();
    }

    public void setMyGame(Game myGame) {
        this.myGame = myGame;
    }

    public Game getMyGame() {
        return myGame;
    }
}
