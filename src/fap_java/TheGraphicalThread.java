package fap_java;

import java.util.ArrayList;


public class TheGraphicalThread implements Runnable{
    
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
    private int delay = Params.Gdelay;     // in ms
        
    /**
     * Initializes Thread
     * @param myGame : the game to control
     */
    public TheGraphicalThread(Game myGame) {
        this.myGame = myGame;
        this.running = false;
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
           
                try{
                    // wait for "delay" ms
                    Thread.sleep(delay);
                } catch (InterruptedException ie){
                    ie.printStackTrace();
                }
            
        }
    }

    
    /**
     * Execute actions in the game, such as updating values
     */
    private void execute(boolean full){
        myGame.repaint();
    }

    public void setMyGame(Game myGame) {
        this.myGame = myGame;
    }

    public Game getMyGame() {
        return myGame;
    }
}
