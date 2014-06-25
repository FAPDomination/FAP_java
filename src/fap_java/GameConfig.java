package fap_java;

public class GameConfig {
    
    /**
     * How much points does it take to win the game
     */
    private int victScore;
    /**
     * How much percent of total cells of the map does it take to win the game
     */
    private double victTile;    // in percent
    /**
     * Time before automatically losing the game
     */
    private int victTime; // in sec
    
    //Parameters to be given when starting a new game
    /**
     * A String indicating wich player is in the game and their skills. Ex : "1,8,0,4"
     */
    private String whoIsPlaying; // This is linked with skill, here player 1 is magician
    /**
     * The no of team the corresponding player is in. Ex : "0,1,1,0"
     */
    private String wichTeam; // Here player no2 is in team no0
    /**
     * Indicating wich player is FSM. Ex : "0,1,1,0"
     */
    private String isFSM;   // 0 for player, other for FSM level
    /**
     * Wich controler (set of keys) controls this player if it's not a FSM
     */
    private String controlers;
    /**
     * If the starting points of the map are mixed or fixed
     */
    private boolean randStart;
    
    
    /**
     * Tells if the game is in adventure mode
     * 0 : battle mode (conquest and stuff) launched from versus
     * 1 : battle mode launched from adventure
     * 2 : full adventure map (town without battle)
     */
    private int adv;
    
    private int nmap;
    
    public GameConfig(String whoIsPlaying, String wichTeam, String controlers, String isFSM, boolean randStart, int nmap, int victScore, double victTile, int victTime, int adv) {
        this.victScore = victScore;
        this.victTile = victTile;
        this.victTime = victTime;
        this.whoIsPlaying = whoIsPlaying;
        this.wichTeam = wichTeam;
        this.isFSM = isFSM;
        this.randStart = randStart;
        this.controlers = controlers;
        this.adv = adv;
        
        this.nmap = nmap;
    }
    
    public GameConfig(int nmap){
        this("7","0","0","0",false,nmap,0,0,0,2);
    }
}
