package fap_java;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

public class Graph {
    
    // used for loading files in this resource class : do "load.getResource(relative path from 'resources')".
    private static final ClassLoader load = Graph.class.getClassLoader();

    /* ******************************************************
     * ********************* IMAGES *************************
     * *****************************************************/

    // Cells images
    protected static Map<String, Image> cells = new HashMap<String, Image>();
    public static Map<String, Image> guimg = new HashMap<String, Image>();
    // key : String, the name of the cell
    // entry : the cell image
    static {
        try {
            cells.put("normal", Toolkit.getDefaultToolkit().getImage(load.getResource("images/cells/cell.png")));
            cells.put("dirt", Toolkit.getDefaultToolkit().getImage(load.getResource("images/cells/dirt.png")));
            cells.put("bridge", Toolkit.getDefaultToolkit().getImage(load.getResource("images/cells/walkable/bridge.png")));
            cells.put("grass", Toolkit.getDefaultToolkit().getImage(load.getResource("images/cells/walkable/grass.png")));
            cells.put("paved", Toolkit.getDefaultToolkit().getImage(load.getResource("images/cells/walkable/paved.png")));
            cells.put("river", Toolkit.getDefaultToolkit().getImage(load.getResource("images/cells/walkable/river.png")));
            cells.put("little_rocks", Toolkit.getDefaultToolkit().getImage(load.getResource("images/cells/walkable/little_rocks.png")));
            cells.put("trail", Toolkit.getDefaultToolkit().getImage(load.getResource("images/cells/walkable/trail.png")));
            cells.put("rock", Toolkit.getDefaultToolkit().getImage(load.getResource("images/cells/blocking/rock.png")));
            cells.put("tree_1", Toolkit.getDefaultToolkit().getImage(load.getResource("images/cells/blocking/tree_1.png")));
            guimg.put("MM_sword", Toolkit.getDefaultToolkit().getImage(load.getResource("images/gui/mainMenu/sword.png")));
        } catch (Exception e) {
            System.out.println("An error occured while trying loading the cell images.");
        }
    }
    
    public Graph() {
    }
}
