package fap_java;

import java.awt.Color;

import java.util.HashMap;
import java.util.Map;

public class Params {
    public static final int OFFX = 10;
    public static final int OFFY = -20;
    public static boolean gameOfLife = true;
    public static int nNeighboursConway = 1;
    public static int higherMaxHP = 200;
    public static int frozenTime = 4*1000;
    public static double frozenFac = 3;
    public static double gainLifeFactorMultiplier = 1.5;    //For healthy healthy
    public static int delay = 40;
    public static double decLifeCountDown = 1;
    public static int idBlockingHigh = 100;
    public static int idblockingLow = 200;
    public static int giveScore = 2;
    public static int rPoints = 2;
    
    
    //skills
    public static int howLongBlockingMagician;
    public static int howManyRingsIstheMagicianActive;
    public static int nBlastedTiles;
    public static int ringsVampirismTakes;
    public static int rateVampirismGains;
    public static int arrowSpeed;
    public static int boosterSpeed;
    public static int boosterTime; // in ms
    public static int archerDammage;
    public static boolean warlockCanDisableTraps = true;
    public static double warriorDammage;
    public static int minerNCells;
    
    public static int timeAfterSkill = 1000; //in m-second
    
    public static Map<String, double[]> paramTable = new HashMap<String, double[]>();
    
    //FSM
    public static int fsmReactionTime = 40;
    
    // Colors
    public static Color[] colorList = {Color.red,Color.blue, Color.yellow,new Color(0, 200, 0), new Color(0,220,235), new Color(234,0,220), new Color(255,132,0), new Color(156,0,255)};
}
