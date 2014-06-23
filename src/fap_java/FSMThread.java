package fap_java;

public class FSMThread extends Thread{
    private Player p;
    private Game game;
    public FSMThread(Game myGame, Player p) {
        super();
        this.p = p;
        this.game = myGame;
    }
    
    public void run(){
        while(!game.isGameEnded()){
            if(game.getThread().getRunning()){
                p.getFsm().executeMethod();
                
                try{
                    // wait for "delay" ms
                    Thread.sleep(game.getThread().getDelay());
                } catch (InterruptedException ie){
                    ie.printStackTrace();
                }
            }
        }
    }
}
