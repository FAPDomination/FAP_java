package gui;

import fap_java.Cell;

import java.awt.Color;
import java.awt.Dimension;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    
    public static final String configFileName="config.fap";
    
    public static Dimension frameDimension = new Dimension(900, 730);
    
    public static int maxxS = -80;
    public static int minxS = -720; 
    public static int maxxC = 130;
    public static int minxC = -740;
    // Gradient colors
    //TODO better colors ?
    public static Color top = new Color(80, 140, 255);
    public static Color bottom = new Color(175, 240, 255);
    
    // Game datas
    public static final int numberOfAdvSwitches = 25;
    public static final int numberOfAdvChars = 10;
    
    public static String[] listAdvMaps = {"18,11","18,10","17,9","17,10","16,10","18,9"};
    
    public static String savegameFile = "SAVEGAME_FILE";
    public static String controlersFile = "CONTROLS_FILE";
    public static String pictureSet = "GRAPH_SET";
    
    public static String wmGameConfigMap = "resources/wmGCM.adv";
    public static String wmParentCells = "resources/wmPC.adv";
    
    public static Cell initialCellWorldMap = new Cell(18,10);
    
    public static Dimension buttonSize = new Dimension(180,60);
    
    public static String[] charNames = {"Knight","OldMagician","Miner","Warlock","Archer","Vampire","NoCharacterGuy","Magician","Booster"};

    public static Map<String,String> c = new HashMap<String,String>();
}
