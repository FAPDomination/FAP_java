package gui;

import fap_java.Cell;

import java.awt.Color;
import java.awt.Dimension;

public class Constants {
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
    public static final int numberOfAdvSwitches = 10;
    public static final int numberOfAdvChars = 10;
    
    public static int[] listAdvMaps = {20,21,22,23,24,25};
    
    public static String savegameFile = "savegame.leo";
    public static String controlersFile = "resources/keys.leo";
    
    public static Cell initialCellWorldMap = new Cell(18,10);
    
    public static Dimension buttonSize = new Dimension(180,60);
    
    public static String[] charNames = {"Knight","OldMagician","Miner","Warlock","Archer","Vampire","NoCharacterGuy","Magician","Booster"};
}
