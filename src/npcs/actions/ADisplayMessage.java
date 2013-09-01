package npcs.actions;

import fap_java.NPC;

public class ADisplayMessage implements Action {
    private String message;
    private int iterator = 0;
    public ADisplayMessage(String message) {
        super();
        this.message = message;
        this.iterator =0;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void execute(NPC whoLaunches) {
        if(iterator == 0){
            System.out.println(message);
            iterator++;
            whoLaunches.setIterator(whoLaunches.getIterator()-1);
        }
        else{
            //Loop
            if(whoLaunches != null && whoLaunches.getIterator() <= whoLaunches.getActions().size()){
                this.iterator = 0;
                whoLaunches.execute();
            }
        }
    }

    public void setIterator(int iterator) {
        this.iterator = iterator;
    }

    public int getIterator() {
        return iterator;
    }
}
