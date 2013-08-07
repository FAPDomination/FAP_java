package fap_java;

import characters.Miner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import pathFinder.pathFinder;

public class FSM{

    private FSM_State currentState;
    private FSM_State prevState;
    private FSM_Event currentEvent;
    
    private Cell nextCell;
    private boolean cellWasTaken;
    
    private Player body;
    
    private boolean fsmGo;
    private Object fsm_param;
    private Object fsm_secParam;
    
    private int nRings = 2;
    
    //States
    public static FSM_State picking = new FSM_State(0,"pickCell");
    public static FSM_State shifting = new FSM_State(1,"shiftToPicked");
    public static FSM_State analysing = new FSM_State(2,"analyseCurCell");
    public static FSM_State waiting = new FSM_State(3,"waitForIt");
    public static FSM_State staying = new FSM_State(4,"stay");
    public static FSM_State pathDefine = new FSM_State(5,"definePath");
    public static FSM_State pathFollow = new FSM_State(6,"followPath");
    //Events
    public static FSM_Event ev_done = new FSM_Event(0);
    public static FSM_Event ev_error = new FSM_Event(1);
    public static FSM_Event ev_secDone = new FSM_Event(2);
    public static FSM_Event ev_thirdDone = new FSM_Event(3);
    

    public FSM(Player p) {
    //    super(id, c, game, pc, t);
        fsmGo = true;
        currentState = picking;
        body = p;
        initFSM();
    }

    public void getSkill() {
    }

    // FSM Configuration
    /*
    private void fsm_configure_transition(Object fstate, Object fevent, Object nextState){
            fsm_transition_table[fstate][fevent] = nextState;
    }

    private void fsm_configure_action(Object fstate, Object action){
            fsm_action_table[fstate] = action;
    }
    */

    public String toString() {
        return "fsm";
    }

    public void pickCell() {
        ArrayList<Cell> neighborHoodList = this.body.getGame().getMap().surroundingCells(this.body.getCurrent());
        Map<Cell, Integer> weights = new HashMap<Cell, Integer>();
        for(int i=0;i<neighborHoodList.size();i++){
            Cell c = neighborHoodList.get(i);
            if(c!=null){
                int w = this.getWeight(c);
                weights.put(c, w);
            }
        }
        ArrayList<Integer> weightList = new ArrayList(weights.values());
        Collections.sort(weightList);

        ArrayList<Cell> bestCells = new ArrayList<Cell>();
        for(int k=0;k<neighborHoodList.size();k++){
            Cell c = neighborHoodList.get(k);
            if(c!= null && this.getWeight(c) == weightList.get(weightList.size()-1)){
                bestCells.add(c);
            }
        }
        if(bestCells.size()==0){
            this.fsm_receive_event(ev_error, "No walkable cell");
        }
        else{
            Cell c = bestCells.get(Tools.randRange(0, bestCells.size()-1));
            nextCell = c;
            this.fsm_receive_event(ev_done, null);
        }
    }

    public void shiftToPicked() {
        body.shiftStick(0,0);
        if(prevState == pathFollow){
            this.fsm_receive_event(ev_secDone, fsm_param);
        }
        else{
            this.fsm_receive_event(ev_done, null);
        }
    }
    
    public void analyseCurCell() {
        //currentEvent = null;
        //System.out.println(body.getCurrent());
        //Analyses
        //check area
        int areaWeight = this.areaWeight(body.getCurrent(), nRings);
        //Weight toggle for changin area
        if(areaWeight <4){
            //Find good Cell system
            if(body instanceof Miner){
                body.getSkill();
                body.keyLow(4);
                this.fsm_receive_event(ev_secDone, 3*Params.fsmReactionTime);
            }
            else{
                //Ersatz system : find Cell with ennemy
                Cell k = body.getGame().getPlayers().get(0).getCurrent();
                fsm_param = k;
                this.fsm_receive_event(ev_thirdDone, fsm_param);
            }
        }
        else{
            boolean skillWorth = false;
            int ts = body.getGame().getThread().getCount() - body.getLastSkill();
            Cell c = body.getCurrent();
            ArrayList<Cell> neighbour = body.getGame().getMap().surroundingCells(c);
            int averageHP = 0;
            int nCells = 0;
            if (ts >= body.getSkillTime()) {
                switch(body.getPc()){
                case 1:         //Knight
                    //Get the average HP of the tiles surrounding
                    for (int i = 0; i < neighbour.size(); i++) {
                        Cell k = neighbour.get(i);
                        if (k!= null && k.getType() == 1 && (k.getOwner() != body.getTeam() || k.getOwner() == null)) {
                            int hpAmount = (int)k.getHp();
                            if (k.getOwner() == null) {
                                hpAmount = 90;
                            }
                            averageHP += hpAmount;
                            nCells++;
                        }
                    }
                    if (nCells > 0) {
                        averageHP /= nCells;
                    }
                    //Calculate the amount of damage he can do
                    double randError = ((double)Tools.randRange(-100, 100)) / 10;
                    int dammage = (int)(Params.warriorDammage * Math.pow((ts + randError), 2));
                    //Triggah'
                    //Totally arbitrary : should also depend on levels
                    int triggNCells = 4;
                    if (nCells >= triggNCells && dammage >= averageHP) {
                        skillWorth = true;
                    }
                break;
                //Note : for the Miner (pc = 3) see above
                case 4 :        //Warlock
                Cell k = body.getCurrent();
                    neighbour = body.getGame().getMap().surroundingCells(k);
                    averageHP = 0;
                    nCells = 0;
                    for (int i = 0; i < neighbour.size(); i++) {
                        if (neighbour.get(i)!= null && neighbour.get(i).getType() == 1) {
                            nCells++;
                        }
                    }
                    //Totally arbitrary : should also depend on levels
                    if (nCells < 5) {
                        skillWorth = true;
                    }
                break;
                case 8:
                    Game game = body.getGame();
                    int err = Tools.randRange(-1, 1);
                    int nRg = Params.howManyRingsIstheMagicianActive+err;
                    Map<Integer, ArrayList<Cell>> ringsOfCells = game.getMap().ringsSurrounding(c,nRg);
                    for(int i = 0;i<=nRg;i++){
                        ArrayList<Cell> theRing = ringsOfCells.get(i);
                        for(int j=0;j<theRing.size();j++){
                            Cell a = theRing.get(j);
                            //c.setOwner(this.getTeam());
                            //c.setHp(this.getInitHP());
                            Player p = game.isOccupied(a);
                            if(p != null){
                                skillWorth = true;
                            }
                        }
                    }
                break;
                default:
                    break;
                }
            }
            
            if(skillWorth){
            System.out.println("sop");
                body.getSkill();
                this.fsm_receive_event(ev_secDone, Params.fsmReactionTime);
            }
            else{
                //Define if it's time to go :
                c = body.getCurrent();
                if(c.getOwner() != null && c.getOwner() != body.getTeam()){
                    cellWasTaken = true;
                }
                else{
                    if(cellWasTaken){
                        this.fsm_receive_event(ev_secDone, Params.fsmReactionTime);
                    }
                    else{
                        this.fsm_receive_event(ev_done, null);
                    }
                    cellWasTaken = false;
                }
            }
        }
    }
    
    public void waitForIt(){
        body.makeHimWait((Integer)fsm_param);
        this.fsm_receive_event(ev_done, null);
    }
    
    public void definePath(){
        //Dis is da path, yo
        Cell c = (Cell) fsm_param;
        Cell s = body.getCurrent();
        ArrayList<Cell> map = body.getGame().getMap().getMyMap();
        ArrayList<Cell> path = pathFinder.findPath(map, c, s);
        
        fsm_param = path;
        if(path.size() > 0){
            this.fsm_receive_event(ev_done, fsm_param);
        }
        else{
            this.fsm_receive_event(ev_error, "You shall not path");
        }
        //have to define if the path will be direct (no waiting on cell for conquering) or  not
    }
    
    public void followPath(){
        ArrayList<Cell> path = (ArrayList<Cell>) fsm_param;
        Cell c = path.get(path.size()-1);
        if(c != body.getCurrent()){     // Have to shift to cell
            // go to shiftCell
             nextCell = c;
            this.fsm_receive_event(ev_done, fsm_param);
        }
        else{
            if(path.size()<=1){         // Path is over
                // Escape to pickCell
                this.fsm_receive_event(ev_thirdDone, fsm_param);
            }
            else{                       // Cell is shifted
                
                path.remove(c);
                fsm_param = path;
                this.fsm_receive_event(ev_secDone, fsm_param);
            }
        }
    }
    
    public void stay(){
        System.out.println("An error occured, FSM crashed");
        System.out.println((String)fsm_param);

        fsmGo=false;    // 
    }
    
    public void fsm_receive_event(FSM_Event ev, Object param){
        currentEvent = ev;
                //trace(currentState+"-"+currentEvent);
                prevState = currentState;
                fsm_param = param;
                FSM_State nextState = currentState.getNextState(ev);
                //trace(currentState+"-"+currentEvent+"-"+nextState);
                currentState=nextState;
                //trace("transition "+currentState);
                //fsmGo = 1;
                //System.out.println(prevState+"-"+ev+"-"+currentState);
    }

    public void executeMethod() {
        if(fsmGo){
        try {
            Method method = this.getClass().getMethod(currentState.getAssociatedMethod());
            method.invoke(this);
        } catch (SecurityException e) {
            System.out.println("Security Exception");
        } catch (NoSuchMethodException e) {
            System.out.println("No such Method : " + currentState.getAssociatedMethod());
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        }
    }

    private void initFSM() {
        //ev_done
        picking.addTransition(ev_done, shifting);
        shifting.addTransition(ev_done, analysing);
        analysing.addTransition(ev_done, picking);
        waiting.addTransition(ev_done, picking);
        pathDefine.addTransition(ev_done, pathFollow);
        pathFollow.addTransition(ev_done, shifting);
        //ev_error
        picking.addTransition(ev_error, staying);
        shifting.addTransition(ev_error, staying);
        analysing.addTransition(ev_error, staying);
        pathDefine.addTransition(ev_error, staying);
        //ev_secDone
        analysing.addTransition(ev_secDone, waiting);
        pathFollow.addTransition(ev_secDone, pathFollow);
        shifting.addTransition(ev_secDone, pathFollow);
        //ev_thirdDone
        analysing.addTransition(ev_thirdDone, pathDefine);
        pathFollow.addTransition(ev_thirdDone, picking);
        
    }

    public void setNextCell(Cell nextCell) {
        this.nextCell = nextCell;
    }

    public Cell getNextCell() {
        return nextCell;
    }
    
    public int getWeight(Cell c){
            int w;
            if(!c.isWalkable()){
                    w = 0;
            }
            else if(c.getOwner() != null){
                if(c.getOwner() == body.getTeam()){
                    w = 2;
                }
                else{
                    w = 8;
                }
            }
            else{
                switch(c.getType()){
                case 1:
                    w=9;
                    break;
                case 10:    //It's a warp
                    
                int[] tab = new int[2];
                String[] tabS = new String[2];
                tabS = c.getAddParam().split(",");
                tab[0] = Integer.parseInt(tabS[0]);
                tab[1] = Integer.parseInt(tabS[1]);

                Cell warpedCell = body.getGame().getMap().getCell(tab);
                w = areaWeight(warpedCell, nRings+1);
                case 11:
                    w=3;
                    break;
                
                default:
                    w=0;
                break;
                }
            }
            return w;
    }
    
    public int areaWeight(Cell cell, int nRings){
        int average=0;
        int nCells = 0;
        Map<Integer, ArrayList<Cell>> ringsOfCells = this.body.getGame().getMap().ringsSurrounding(cell, nRings);
        for(int i = 0;i<=nRings;i++){
            ArrayList<Cell> theRing = ringsOfCells.get(i);
            for(int j=0;j<theRing.size();j++){
                Cell c = theRing.get(j);
                if(c != null){
                    average += this.getWeight(c);
                    nCells++;
                }
            }
        }
        average /= nCells;
        return average;
    }
}
