package fap_java;

public class TheThread implements Runnable{
    
    private Game myGame;
    private boolean running;
    private int delay = Params.delay;     // in ms
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
                execute();
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
    
    private void execute(){
        count +=delay;
        int frame = count/delay;
        myGame.refreshHealthPoints();
        if(frame % 4 == 0){
            myGame.updateCellsByOwner();
        }
        if(count%(1000*Params.giveScore)==0){
            myGame.getScoreHandler().computeScores();
        }
        
    }
}
