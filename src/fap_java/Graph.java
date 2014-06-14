package fap_java;

import gui.PreLoadingScreen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

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
    protected static Map<Integer, Dimension> offsetsCells = new HashMap<Integer, Dimension>();
    public static Map<String, BufferedImage> characters = new HashMap<String, BufferedImage>();
    public static Map<String, BufferedImage> thumbnails = new HashMap<String, BufferedImage>();

    // Important factors for drawing cells
    public static double facW = CMap.TW / ((double)(97 - 20));
    public static double facH = CMap.TH / ((double)(73 - 36));
    // key : String, the name of the cell
    // entry : the cell image

    protected static Image basicCellImage;

    public Graph() {
    } 
    
    // Colors :
    public static final Color BG_Blue = Color.blue;
    public static final Color BG_Red = Color.red;
    public static final Color MENU_TEXT_BORDER_TRANSLUSCENT = new Color(255,255,255,160);
    public static final Color DEFAULT_SQUARE_COLOR=new Color(100,100,100);
    public static final Color NPC_SQUARE_COLOR = new Color(200,200,200);
    public static final Color BG_TRANSPARENT = new Color(0, 0, 0, 0);
    public static final Color BG_DARKTRANSLUSCENT = new Color(0,0,0,140);
    public static final Color BG_DARK = new Color(0,0,0,190);
    public static final Color BG_WHITETRANSLUSCENT = new Color(255,255,255,(int)(255*0.3));
    public static final Color MINIMAP_SELECTED_COLOR = Color.BLUE;
    
    public static final Color BTN_COLOR = new Color(255,255,255,220);
    public static final Color BTN_COLOR_HOVER = new Color(250,250,250,255);
    
    // Borders :
    public static final Border NO_BORDER = BorderFactory.createEmptyBorder(0, 0, 0, 0);
    
    // Fonts :
    public static final Font BTN_MENU_FONT = new Font("Calibri", Font.BOLD, 24);
    //public static final Font BTN_SAMPLE_FONT = new Font("Calibri", Font.BOLD, 24);
    public static final Font REGULAR_FONT = new Font("Calibri", Font.PLAIN, 12);
    public static final Font PAUSECOUNTDOWN_FONT = new Font("Verdana", Font.PLAIN, 150);

    public static void load(PreLoadingScreen pls) {
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
            // High Blocking
            cells.put(100, ImageIO.read(new File("resources/images/default/cells/blocking/rock.png")));
            cells.put(102, ImageIO.read(new File("resources/images/default/cells/blocking/tree_1.png")));
            cells.put(103, ImageIO.read(new File("resources/images/default/cells/blocking/collumn.png")));
            cells.put(104, ImageIO.read(new File("resources/images/default/cells/blocking/collumn_broken.png")));
            // Low Blocking
            cells.put(200, ImageIO.read(new File("resources/images/default/cells/blocking/river.png")));
            cells.put(201, ImageIO.read(new File("resources/images/default/cells/blocking/river_lava.png")));
            // Offsets
            /*
             * Note : although it's a Dimension array, they are just used as a pair of Integers.
             * width is the X offset
             * height is the Y offset
             * /!\ they are both supposed to be negative
             */
            offsetsCells.put(0, new Dimension(-9, -17));
            offsetsCells.put(1, new Dimension(-9, -17));
            offsetsCells.put(2, new Dimension(-9, -17));
            offsetsCells.put(3, new Dimension(-9, -17));
            offsetsCells.put(4, new Dimension(-9, -17));
            offsetsCells.put(5, new Dimension(-9, -17));
            offsetsCells.put(6, new Dimension(-9, -17));
            offsetsCells.put(7, new Dimension(-9, -17));
            offsetsCells.put(8, new Dimension(-9, -17));
            offsetsCells.put(9, new Dimension(-9, -17));
            offsetsCells.put(13, new Dimension(-9, -20));
            offsetsCells.put(10, new Dimension(-9, -17));
            offsetsCells.put(11, new Dimension(-9, -17));
            offsetsCells.put(12, new Dimension(-9, -17));
            offsetsCells.put(100, new Dimension(-9, -16));
            offsetsCells.put(102, new Dimension(-34, -122));
            offsetsCells.put(103, new Dimension(-9, -113));
            offsetsCells.put(104, new Dimension(-9, -113));
            offsetsCells.put(200, new Dimension(-9, -17));
            offsetsCells.put(201, new Dimension(-9, -17));
            // Other images
            /*
            list.put("arrow", Toolkit.getDefaultToolkit().getImage(load.getResource("images/arrow/arrowL.png")));
            list.put("NPC_sample", Toolkit.getDefaultToolkit().getImage(load.getResource("images/NPCs/sample.png")));
            */
            list.put("arrow", ImageIO.read(new File("resources/images/arrow/arrowL.png")));
            list.put("NPC_sample", ImageIO.read(new File("resources/images/NPCs/sample.png")));
            list.put("warp", ImageIO.read(new File("resources/images/default/animations/teleport.png")));
            list.put("frozen", ImageIO.read(new File("resources/images/default/animations/frozen.png")));
            //First Lightning
            int nbLightnings = Params.NLIGHTNING_EFFECTS;
            int nbLghtImages = 10;
            for(int j=0;j<nbLightnings;j++){
                for(int i=0;i<nbLghtImages;i++){
                    list.put("Lght"+(j+1)+"_"+(i+1), ImageIO.read(new File("resources/images/default/animations/Warlocks_Lightnings/Lght_"+(j+1)+"/Lght_"+(j+1)+"_"+(i+1)+".png")));
                }
            }
            
            
            //Fake images to load to see what it's like a big loading time
            /*
            list.put("warp", ImageIO.read(new File("resources/images/fake/01.jpg")));
            list.put("warp", ImageIO.read(new File("resources/images/fake/02.png")));
            list.put("warp", ImageIO.read(new File("resources/images/fake/03.jpg")));
            list.put("warp", ImageIO.read(new File("resources/images/fake/04.jpg")));
            list.put("warp", ImageIO.read(new File("resources/images/fake/05.jpg")));
            list.put("warp", ImageIO.read(new File("resources/images/fake/06.png")));
            */
            //list.put("warp", Toolkit.getDefaultToolkit().getImage(load.getResource("images/animations/teleport.png")));
            //Gui
            guimg.put("MM_sword", ImageIO.read(new File("resources/images/gui/mainMenu/sword.png")));
            guimg.put("MM_clouds", ImageIO.read(new File("resources/images/gui/mainMenu/clouds.png")));
            guimg.put("btn_add", ImageIO.read(new File("resources/images/gui/buttons/btn_Add.png")));
            guimg.put("btn_add_hover", ImageIO.read(new File("resources/images/gui/buttons/btn_Add_hover.png")));
            guimg.put("btn_remove", ImageIO.read(new File("resources/images/gui/buttons/btn_Remove.png")));
            guimg.put("btn_remove_hover", ImageIO.read(new File("resources/images/gui/buttons/btn_Remove_hover.png")));

            File location = new File("resources/images/default/cells/regular.png");
            basicCellImage = Tools.getImageToFilter(ImageIO.read(location));
            
            // Add base character files
            //Right
            characters.put(Params.colorName[0]+"right", ImageIO.read(new File("resources/images/default/characters/base/Red_Right.png")));
            characters.put(Params.colorName[1]+"right", ImageIO.read(new File("resources/images/default/characters/base/Blue_Right.png")));
            characters.put(Params.colorName[2]+"right", ImageIO.read(new File("resources/images/default/characters/base/Yellow_Right.png")));
            // Diag0 (Up left + Bottom Right)
            characters.put(Params.colorName[0]+"diag0", ImageIO.read(new File("resources/images/default/characters/base/Red_Diag0.png")));
            characters.put(Params.colorName[1]+"diag0", ImageIO.read(new File("resources/images/default/characters/base/Blue_Diag0.png")));
            characters.put(Params.colorName[2]+"diag0", ImageIO.read(new File("resources/images/default/characters/base/Yellow_Diag0.png")));
            //left
            characters.put(Params.colorName[0]+"left", ImageIO.read(new File("resources/images/default/characters/base/Red_Left.png")));
            characters.put(Params.colorName[1]+"left", ImageIO.read(new File("resources/images/default/characters/base/Blue_Left.png")));
            characters.put(Params.colorName[2]+"left", ImageIO.read(new File("resources/images/default/characters/base/Yellow_Left.png")));
            //Diag1 (top right + bottom left)
            characters.put(Params.colorName[0]+"diag1", ImageIO.read(new File("resources/images/default/characters/base/Red_Diag1.png")));
            characters.put(Params.colorName[1]+"diag1", ImageIO.read(new File("resources/images/default/characters/base/Blue_Diag1.png")));
            characters.put(Params.colorName[2]+"diag1", ImageIO.read(new File("resources/images/default/characters/base/Yellow_Diag1.png")));
            
            int limitColor = 3;
            //Knight
            for(int j=0;j<limitColor;j++){
                for(int i=0;i<6;i++){
                    characters.put("Knight"+i+"_"+Params.colorName[j], ImageIO.read(new File("resources/images/default/characters/Knight/Knight"+i+"_"+Params.colorName[j]+".png")));
                }
            }
            //NoCharacterGuy
            for(int j=0;j<limitColor;j++){
                for(int i=0;i<6;i++){
                    characters.put("NoCharacterGuy"+i+"_"+Params.colorName[j], ImageIO.read(new File("resources/images/default/characters/NoCharacterGuy/NoCharacterGuy"+i+"_"+Params.colorName[j]+".png")));
                }
            }
            //Magician
            for(int j=0;j<limitColor;j++){
                for(int i=0;i<6;i++){
                    characters.put("Magician"+i+"_"+Params.colorName[j], ImageIO.read(new File("resources/images/default/characters/Magician/Magician"+i+"_"+Params.colorName[j]+".png")));
                }
            }
            //Miner
            for(int j=0;j<limitColor;j++){
                for(int i=0;i<6;i++){
                    characters.put("Miner"+i+"_"+Params.colorName[j], ImageIO.read(new File("resources/images/default/characters/Miner/Miner"+i+"_"+Params.colorName[j]+".png")));
                }
            }
            
            //Shadow
            characters.put("shadow", ImageIO.read(new File("resources/images/default/characters/shadow.png")));
            
            //Thumbnails
            thumbnails.put("Big_Knight", ImageIO.read(new File("resources/images/default/characters/Thumbnails/Big_Knight.png")));
            thumbnails.put("Big_NoCharacterGuy", ImageIO.read(new File("resources/images/default/characters/Thumbnails/Big_NoCharacterGuy.png")));
            thumbnails.put("Big_Magician", ImageIO.read(new File("resources/images/default/characters/Thumbnails/Big_Magician.png")));
            thumbnails.put("Big_Miner", ImageIO.read(new File("resources/images/default/characters/Thumbnails/Big_Miner.png")));
            
            
            //Thread.sleep(2000);
            pls.endAnim();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("An error occured while trying to load the images.");
            e.printStackTrace();
        }
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
    
    public static void drawBorderedString(Graphics g,int x,int y, String label, Color borderColor){
        Color c = g.getColor();
        g.setColor(borderColor);
        g.drawString(label, x-1, y- 1);
        g.drawString(label, x-1, y+ 1);
        g.drawString(label, x+1, y- 1);
        g.drawString(label, x+1, y+ 1);
        g.setColor(c);
        g.drawString(label, x,y);
    }
    
    public static void drawDarkBackgroundRectangle(Graphics g, int x, int y, int w, int h, Color SquareColor, int SquareSize, Color backgrounColor){
        Color c = g.getColor();
        g.setColor(backgrounColor);
        g.fillRect(x, y, w, h);
        if(SquareColor != null){
            g.setColor(SquareColor);
            g.fillRect(x+w-(3*SquareSize/4), y-(1*SquareSize/4), SquareSize,SquareSize);
        }
        g.setColor(c);
    }
}
