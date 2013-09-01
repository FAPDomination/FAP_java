package npcs.actions;

import fap_java.NPC;

public class AAsk implements Action {
    private String message;
    private String yesOption;
    private String noOption;
    private boolean choice;
    private int iterator = 0;
    public AAsk(String message, String yes, String no) {
        super();
        this.message = message;
        this.yesOption = yes;
        this.noOption = no;
        choice = true;
    }

    public void execute(NPC whoLaunches) {
        if(iterator == 0){
            System.out.println(message);
            iterator++;
            whoLaunches.setIterator(whoLaunches.getIterator()-1);
            System.out.println(choice);
        }
        else{
            //Loop
            if(whoLaunches != null && whoLaunches.getIterator() <= whoLaunches.getActions().size()){
                this.iterator = 0;
                whoLaunches.execute();
            }
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setYesOption(String yesOption) {
        this.yesOption = yesOption;
    }

    public String getYesOption() {
        return yesOption;
    }

    public void setNoOption(String noOption) {
        this.noOption = noOption;
    }

    public String getNoOption() {
        return noOption;
    }

    public void setIterator(int iterator) {
        this.iterator = iterator;
    }

    public int getIterator() {
        return iterator;
    }

    public void setChoice(boolean choice) {
        this.choice = choice;
    }

    public boolean isChoice() {
        return choice;
    }

}
