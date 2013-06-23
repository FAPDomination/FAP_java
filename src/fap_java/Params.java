package fap_java;

import java.util.HashMap;
import java.util.Map;

public class Params {
    public static final int OFFX = 10;
    public static final int OFFY = -20;
    public static boolean gameOfLife = true;
    public static int nNeighboursConwell = 1;
    public static int higherMaxHP = 200;
    public static double gainLifeFactorMultiplier = 1.5;
    public static int delay = 40;
    public static double decLifeCountDown = 1;
    public static int idBlockingHigh = 100;
    public static int idblockingLow = 200;
    public static int giveScore = 2;
    public static int rPoints = 2;
    
    public static Map<String, double[]> paramTable = new HashMap<String, double[]>();
    
    //Skills
    public static double warriorDammage = 120/Math.pow(4.5*1000, 2);
}
