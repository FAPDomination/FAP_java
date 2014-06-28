package npcs.actions;

import fap_java.Cell;
import fap_java.MapHandler;
import fap_java.NPC;

import java.io.Serializable;

public class AModifyCell implements Action {
    @SuppressWarnings("compatibility:5766958403485029236")
    private static final long serialVersionUID = -8946279837403401252L;
    private transient Cell c;
    private String newGenerator;    
    private Action next;
    private Action origNext;
    public AModifyCell(Cell c, String newGenerator, Action next) {
        super();
        this.c = c;
        this.newGenerator = newGenerator;        
        this.next = next;
        this.origNext = next;
    }

    public void execute(NPC whoLaunches) {
        //Modify cell

        String[] tabNewCell = newGenerator.split(",", 2);
        String param = "";
        int did = Integer.parseInt(tabNewCell[0]);
        if (tabNewCell.length > 1) {
            param = tabNewCell[1];
        }
        //System.out.println(did);

        // Compute new type and properties
        int t = MapHandler.setTypeWithDid(did, param);
        c.setAddParam(param);
        c.setDid(did);

        c.setType(t);
        //Loop
        
        whoLaunches.gotoNextAction();
        whoLaunches.execute();

    }

    public void reinit() {
        next = origNext;
    }

    public void setC(Cell c) {
        this.c = c;
    }

    public Cell getC() {
        return c;
    }


    public void setNewGenerator(String newGenerator) {
        this.newGenerator = newGenerator;
    }

    public String getNewGenerator() {
        return newGenerator;
    }
    
    
    public void setNext(Action next) {
        this.next = next;
    }

    public Action getNext() {
        return next;
    }

    public void setOrigNext(Action origNext) {
        this.origNext = origNext;
    }

    public Action getOrigNext() {
        return origNext;
    }
}
