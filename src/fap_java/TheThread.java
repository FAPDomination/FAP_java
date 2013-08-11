package fap_java;

public class TheThread implements Runnable{
    
    private Game myGame;
    private boolean running;
    private int delay = Params.delay;     // in ms
    private int count;          // Counts the number of ms of the game
    /**
     * Initialize Thread
     * @param myGame : the game to control
     */
    public TheThread(Game myGame) {
        this.myGame = myGame;
        this.running = false;
        count = 0;
    }
    
    /**
     * Toggle the running of the thread
     * @param running : boolean value
     */
    public void setRunning(boolean running){
        this.running = running;
    }
    
    /**
     * Find if the thread is running
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
     * Get the delay between two clock tick
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
            if(running){
                // Execute tasks
                execute();
                try{
                    // ait for "delay" ms
                    Thread.sleep(delay);
                } catch (InterruptedException ie){
                    ie.printStackTrace();
                }
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
        count += delay;
        int frame = count / delay;      // clock ticks
        myGame.refreshHealthPoints();
        myGame.repaint();
        // Every 4 frames
        if(frame % 4 == 0){
            // Count the number of cells a Player has
            myGame.updateCellsByOwner();
        }
        // When it's time to check scores
        if(count % (1000 * Params.giveScore) == 0){
            // Scores are updated
            myGame.getScoreHandler().computeScores();
        }
        
        //Execute other objects actions
        myGame.computeObjects();
        myGame.executeFSMs();
    }
}
