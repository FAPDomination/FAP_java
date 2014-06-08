package fap_java;

import java.awt.Color;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is just storing static and "final" parameters that will be used all over the game.
 * Simply modifying stuff here can change the entire game.
 */
public class Params {
    /**
     * The offset on the x-axis to draw the player
     * @see fap_java.Player
     */
    public static final int OFFX = 10;
    /**
     * The offset on the y-axis to draw the player
     * @see fap_java.Player
     */
    public static final int OFFY = -20;
    /**
     * Activates or not the game of life thing. There are rules saying that if a cell is lonely (ie has under
     * nNeighboursConway neighbours, it will slowly die.
     */
    public static boolean gameOfLife = true;
    /**
     * Min number of neighbours that a cell should have to survive (see above, gameOfLife)
     */
    public static int nNeighboursConway = 1;
    /**
     * The max HP a special tile "Healthy healthy" can reach. These special tiles don't stop at "maxHP" HealthPoints
     * but go on up to higherMaxHP.
     */
    public static final int higherMaxHP = 200;
    /**
     * The rate at wich the healthy healthy tile recovers
     */
    public static final double gainLifeFactorMultiplier = 1.5;    //For healthy healthy
    /**
     * The time the modification given by a frozen cell will be active
     */
    public static final int frozenTime = 4*1000;
    /**
     * The displacement speed factor ( /3 ) that a player will have if frozen by a frozen cell
     */
    public static final double frozenFac = 3;
    /**
     * The delay (in ms) between each Thread clock-tick.
     * @see fap_java.TheComputingThread
     */
    public static final int delay = 40;
    public static final int Gdelay = 4*delay;
    /**
     * The amount of HP a cell loses when it is a timer-cell
     */
    public static double decLifeCountDown = 1;
    /**
     * The ID of the first High-Blocking DID.
     * This represents the first Design ID of cell that is blocking and high, like a rock
     * @see fap_java.Cell
     * @see fap_java.MapHandler
     */
    public static final int idBlockingHigh = 100;
    /**
     * The ID of the first Low-Blocking DID.
     * This represents the first Design ID of cell that is blocking and low, like a river
     * @see fap_java.Cell
     * @see fap_java.MapHandler
     */
    public static final int idblockingLow = 200;
    /**
     * Amount of time (in seconds) before the score get updated
     * @see fap_java.ScoreBar
     */
    public static final int giveScore = 2;
    /**
     * How much point per cell the player receives
     * @see fap_java.ScoreBar
     */
    public static final int rPoints = 2;
    
    /**
     * The duration of the pause countdown after resuming the game
     */
    public static final int pauseDuration = 3600;
    
    //Skill-Related Parameters :
    /**
     * Special Parameter for the Magician.
     * Sets how much time the player will be blocked by the magician's skill
     * @see characters.Magician
     */
    public static int howLongBlockingMagician;
    /**
     * Special Parameter for the magician
     * The number of cell rings around the thrower where enemies will be frozen
     * @see characters.Magician
     */
    public static int howManyRingsIstheMagicianActive;
    /**
     * Special Parameter for the warlock and trap-cells
     * How much cells will be blasted by the trap cell
     * @see characters.Warlock
     */
    public static int nBlastedTiles;
    /**
     * Special Parameter for the vampire
     * The number of cell rings around the thrower where cells will be vampirized
     * @see characters.Vampire
     */
    public static int ringsVampirismTakes;
    /**
     * Special Parameter for the vampire
     * Factor setting how much HP the vampire will suck
     * @see characters.Vampire
     */
    public static int rateVampirismGains;
    /**
     * Special Parameter for the archer
     * The speed of an arrow
     * @see fap_java.Arrow
     * @see characters.Archer
     */
    public static int arrowSpeed;
    /**
     * Special Parameter for the booster
     * The speed of the booster
     * @see characters.Booster
     */
    public static int boosterSpeed;
    /**
     * Special Parameter for the booster
     * The amount of time in ms the booster's skill will be active
     * @see characters.Booster
     */
    public static int boosterTime; // in ms
    /**
     * Special Parameter for the archer
     * The amount of dammage the arrow does to ennemy cells
     * @see characters.Archer
     */
    public static int archerDammage;
    /**
     * Special Parameter for the warlock
     * Sets if the warlock can disable his own traps
     * @see characters.Warlock
     */
    public static boolean warlockCanDisableTraps = true;
    /**
     * Special Parameter for the warrior
     * How much dammage he does around when smaaaashing
     * @see characters.Knight
     */
    public static double warriorDammage;
    /**
     * Special Parameter for the miner
     * The number of random cells he can choose between before teleporting
     * @see characters.Miner
     */
    public static int minerNCells;
    
    /**
     * The time someone should stand waiting after throwing a skill
     */
    public static final int timeAfterSkill = 1000; //in m-second
    
    /**
     * The forbidden characters ID.
     * They correspond to the Old Magician (2), was implemented in the flash version but isn't anymore
     * And to the NoCharacterGuy (7) wich ill be used in the adventure mode
     */

    public static int[] excludedChars = {2,7};
    
    /**
     * The Big Table OF Parameters.
     * Stores the parameters for each character. including 
     * - Skilltime : time between each use of the skill
     * - dispSpeed : the displacement speed of this character
     * - decLifeForced : the amount of hp it takes to cells when conquering
     * - specParam : one or several skill-related parameters
     * - maxHP : the max amount of HPs his cells can get to
     * - recovLifeAuto : the rate at wich his cells recover
     * 
     * @see fap_java.ParamTableHandler
     */
    public static Map<String, double[]> paramTable = new HashMap<String, double[]>();
    
    /**
     * Stores the colors of the players, by id.
     * First red, second blue, etc...
     */
    public static Color[] colorList = {Color.red,Color.blue, Color.yellow,new Color(0, 200, 0), new Color(0,220,235), new Color(234,0,220), new Color(255,132,0), new Color(156,0,255)};
    
    public static String[] colorName = {"rouge","bleue","jaune","verte","azur","rose","orange","violette"};
    /**
     * A factor that defines the reaction time of the Artificial Intelligence.
     * Between each displacements it waits, to seem more "human"
     * @see fap_java.FSM
     */
    public static final int fsmReactionTime = 10;       //10
    /**
     * The default amount of points a player should have to win a game
     */
    public static final int defaultVictoryScore = 1000;
    /**
     * Maximum number of human players playing on one computer (->one keyboard)
     */
    public static final int nPlayersOn1Computer = 3;
    /**
     * Maximum number of players in one game
     */
    public static final int maxPlayers = 8;
    
    /**
     * The code of the key used to pause the game
     */
    public static int pauseKey = 27;
    
    public static int numberOfKeys = 5;
    /**
     * Stores the sets of keys for the human players. Parsed from the options.xml file
     * @see fap_java.OptionsParser
     * 0 : UP
     * 1 : DOWN
     * 2 : RIGHT
     * 3 : LEFT
     * 4 : Skill
     */
    public static int[][] controlsList = new int[nPlayersOn1Computer][numberOfKeys];
    
    public static int timeForSelection = 100;
    
    public static Map<Integer,Cell> mapList = new HashMap<Integer,Cell>();
    
    public static String[][] waitingMessages = new String[1][2];
}
