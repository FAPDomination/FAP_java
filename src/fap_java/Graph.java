package fap_java;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class Graph {
    
    // used for loading files in this resource class : do "load.getResource(relative path from 'resources')".
    public static final ClassLoader load = Graph.class.getClassLoader();

    /* ******************************************************
     * ********************* IMAGES *************************
     * *****************************************************/

    // Cells images
    protected static Map<Integer, BufferedImage> cells = new HashMap<Integer, BufferedImage>();
    protected static Map<String, BufferedImage> list = new HashMap<String, BufferedImage>();
    protected static Map<String, BufferedImage> guimg = new HashMap<String, BufferedImage>();
    protected static Map<Integer,Dimension> offsetsCells = new HashMap<Integer,Dimension>();
    
    // Important factors for drawing cells
    protected static double facW = CMap.TW/((double)(97-20));
    protected static double facH = CMap.TH/((double)(73-36));
    // key : String, the name of the cell
    // entry : the cell image
    
    protected static Image basicCellImage;
    static {
        try {
            cells.put(0, ImageIO.read(new File("resources/images/default/cells/dirt.png")));
            cells.put(1, ImageIO.read(new File("resources/images/default/cells/cell.png")));
            cells.put(2, ImageIO.read(new File("resources/images/default/cells/walkable/trail.png")));
            cells.put(3, ImageIO.read(new File("resources/images/default/cells/walkable/little_rocks.png")));
            cells.put(4, ImageIO.read(new File("resources/images/default/cells/walkable/paved.png")));
            cells.put(5, ImageIO.read(new File("resources/images/default/cells/walkable/bridge.png")));
            cells.put(6, ImageIO.read(new File("resources/images/default/cells/walkable/grass.png")));
            cells.put(7, ImageIO.read(new File("resources/images/default/cells/walkable/rock_dark.png")));
            cells.put(8, ImageIO.read(new File("resources/images/default/cells/walkable/lava_rock.png")));
            cells.put(9, ImageIO.read(new File("resources/images/default/cells/walkable/ice.png")));
            cells.put(13, ImageIO.read(new File("resources/images/default/cells/walkable/minerSelect.png")));
            cells.put(14, ImageIO.read(new File("resources/images/default/cells/walkable/minerSelectCursor.png")));
            cells.put(10, ImageIO.read(new File("resources/images/default/cells/walkable/warp.png")));
            cells.put(11, ImageIO.read(new File("resources/images/default/cells/walkable/switch.png")));
            cells.put(12, ImageIO.read(new File("resources/images/default/cells/walkable/npc_exit.png")));
            cells.put(100, ImageIO.read(new File("resources/images/default/cells/blocking/rock.png")));
            cells.put(102, ImageIO.read(new File("resources/images/default/cells/blocking/tree_1.png")));
            cells.put(200, ImageIO.read(new File("resources/images/default/cells/blocking/river.png")));
            cells.put(201, ImageIO.read(new File("resources/images/default/cells/blocking/river_lava.png")));
            // Offsets
            /*
             * Note : although it's a Dimension array, they are just used as a pair of Integers.
             * width is the X offset
             * height is the Y offset
             * /!\ they are both supposed to be negative
             */
            offsetsCells.put(0, new Dimension(-9,-17));
            offsetsCells.put(1, new Dimension(-9,-17));
            offsetsCells.put(2, new Dimension(-9,-17));
            offsetsCells.put(3, new Dimension(-9,-17));
            offsetsCells.put(4, new Dimension(-9,-17));
            offsetsCells.put(5, new Dimension(-9,-17));
            offsetsCells.put(6, new Dimension(-9,-17));
            offsetsCells.put(7, new Dimension(-9,-17));
            offsetsCells.put(8, new Dimension(-9,-17));
            offsetsCells.put(9, new Dimension(-9,-17));
            offsetsCells.put(13, new Dimension(-9,-20));
            offsetsCells.put(10, new Dimension(-9,-17));
            offsetsCells.put(11, new Dimension(-9,-17));
            offsetsCells.put(12, new Dimension(-9,-17));
            offsetsCells.put(100, new Dimension(-9,-16));
            offsetsCells.put(102, new Dimension(-34,-122));
            offsetsCells.put(200, new Dimension(-9,-17));
            offsetsCells.put(201, new Dimension(-9,-17));
            // Other images
            /*
            list.put("arrow", Toolkit.getDefaultToolkit().getImage(load.getResource("images/arrow/arrowL.png")));
            list.put("NPC_sample", Toolkit.getDefaultToolkit().getImage(load.getResource("images/NPCs/sample.png")));
            */
            list.put("arrow", ImageIO.read(new File("resources/images/arrow/arrowL.png")));
            list.put("NPC_sample", ImageIO.read(new File("resources/images/NPCs/sample.png")));
            list.put("warp", ImageIO.read(new File("resources/images/default/animations/teleport.png")));
            //list.put("warp", Toolkit.getDefaultToolkit().getImage(load.getResource("images/animations/teleport.png")));
            //Gui
            guimg.put("MM_sword", ImageIO.read(new File("resources/images/gui/mainMenu/sword.png")));
            guimg.put("MM_clouds", ImageIO.read(new File("resources/images/gui/mainMenu/clouds.png")));
            guimg.put("minimapBG", ImageIO.read(new File("resources/images/gui/versus/mapSelect/minimapBackground.png")));
            guimg.put("LS_BGteam", ImageIO.read(new File("resources/images/gui/loadingScreen/BG_team.png")));
            guimg.put("LS_BG8team", ImageIO.read(new File("resources/images/gui/loadingScreen/BG_8Teams.png")));
            guimg.put("pauseScreen", ImageIO.read(new File("resources/images/gui/pauseScreen.png")));
            guimg.put("victoryScreen", ImageIO.read(new File("resources/images/gui/victoryScreen.png")));
            guimg.put("npcDisplayMessage", ImageIO.read(new File("resources/images/gui/npcDisplayMessage.png")));
            
            File location = new File("resources/images/default/cells/regular.png");
            basicCellImage = Tools.getImageToFilter(ImageIO.read(location));
            
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (Exception e) {
                System.out.println("An error occured while trying loading the cell images.");
            }
    }
    
    public Graph() {
    }


    public static void setOffsetsCells(Map<Integer, Dimension> offsetsCells) {
        Graph.offsetsCells = offsetsCells;
    }

    public static Map<Integer, Dimension> getOffsetsCells() {
        return offsetsCells;
    }

    public static void setFacW(double facW) {
        Graph.facW = facW;
    }

    public static double getFacW() {
        return facW;
    }

    public static void setFacH(double facH) {
        Graph.facH = facH;
    }

    public static double getFacH() {
        return facH;
    }

    public static void setBasicCellImage(Image basicCellImage) {
        Graph.basicCellImage = basicCellImage;
    }

    public static Image getBasicCellImage() {
        return basicCellImage;
    }

    public static Map<String, BufferedImage> getList() {
        return list;
    }

    public static Map<String, BufferedImage> getGuimg() {
        return guimg;
    }
}
