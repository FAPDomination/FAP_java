package npcs.actions;

import fap_java.Cell;
import fap_java.Game;
import fap_java.MapHandler;
import fap_java.NPC;


public class AModifyCell extends Action {
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = -8946279837403401252L;
    private transient Cell c;
    private String cellHash;
    private String newGenerator;
    
    /**
     * Creates an NPC action that will replace a cell of the map by another (or create it). Useful to make a tile blocking for example
     * @param hash the hash of the cell that shall be modified. Ex : "18,5"
     * @param newGenerator the new value for the cell. did and type will be computed from it
     * @param next the next action to be executed by the NPC
     */
    public AModifyCell(String hash, String newGenerator, Action next) {
        super(next);
        this.cellHash = hash;
        this.newGenerator = newGenerator;
    }

    public void execute(NPC whoLaunches) {
        //Modify cell

        // Compute new type and properties
        String[] tabNewCell = newGenerator.split(",", 2);
        String param = "";
        int did = Integer.parseInt(tabNewCell[0]);
        if (tabNewCell.length > 1) {
            param = tabNewCell[1];
        }

        int t = MapHandler.setTypeWithDid(did, param);
        // If the cell doesn't exist (void in map), create it
        if(c == null){
            String[] tab = cellHash.split(",");
            c = new Cell(Integer.parseInt(tab[0]),Integer.parseInt(tab[1]),t,param,did,whoLaunches.getGame());
            whoLaunches.getGame().getMap().insertCell(c);
            c = whoLaunches.getGame().getMap().getCell(cellHash);
        }
        
        c.setAddParam(param);
        c.setDid(did);

        c.setType(t);
        
        //Loop
        whoLaunches.gotoNextAction();
        whoLaunches.execute();

    }

    public void setTransientValues(Game g) {
        this.c = g.getMap().getCell(this.cellHash);
    }
}
