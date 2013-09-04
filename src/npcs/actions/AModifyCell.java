package npcs.actions;

import fap_java.Cell;
import fap_java.MapHandler;
import fap_java.NPC;

public class AModifyCell implements Action {
    private Cell c;
    private String newGenerator;
    public AModifyCell(Cell c, String newGenerator) {
        super();
        this.c = c;
        this.newGenerator = newGenerator;
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
        if(whoLaunches != null && whoLaunches.getIterator() <= whoLaunches.getActions().size()){
            whoLaunches.execute();
        }
    }

    public void reinit() {
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
}
