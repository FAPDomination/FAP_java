package gui;

import fap_java.Params;

public class ThreadGUI implements Runnable{
    
    private AnimPanel panel;
    private boolean running;
    private int delay = Params.delay;     // in ms
    private int count;          // Counts the number of ms of the game
    /**
     * Initialize Thread
     * @param myGame : the game to control
     */
    public ThreadGUI(AnimPanel panel) {
        this.panel = panel;
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
        while(running){        // infinite loop
                // Execute tasks
                panel.executeAnim();
                try{
                    // ait for "delay" ms
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

}
