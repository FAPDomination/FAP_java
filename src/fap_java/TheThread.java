package fap_java;

public class TheThread implements Runnable{
    
    private Game myGame;
    private boolean running;
    private int delay = 50;     // in ms
    private int count;          // Counts the number of ms of the game
    /**
     * Initialize Thread
     * @param myGame
     */
    public TheThread(Game myGame) {
        this.myGame = myGame;
        this.running = false;
        count = 0;
    }
    
    public void setRunning(boolean running){
        this.running = running;
    }
    
    public boolean getRunning(){
        return this.running;
    }
    
    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }
    
    /**
     * Handle animation
     */
    public void run(){
        while(true){
            if(running){
                count +=delay;
                try{
                    Thread.sleep(delay);
                } catch (InterruptedException ie){
                    ie.printStackTrace();
                }
            }
        }
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
