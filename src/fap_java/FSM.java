package fap_java;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FSM{

    private FSM_State currentState;
    private FSM_State prevState;
    private FSM_Event currentEvent;
    
    private Cell nextCell;
    private boolean cellWasTaken;
    
    private Player body;
    
    private boolean fsmGo;
    private Object fsm_param;
    
    //States
    public static FSM_State picking = new FSM_State(0,"pickCell");
    public static FSM_State shifting = new FSM_State(1,"shiftToPicked");
    public static FSM_State analysing = new FSM_State(2,"analyseCurCell");
    public static FSM_State waiting = new FSM_State(3,"waitForIt");
    public static FSM_State staying = new FSM_State(4,"stay");
    //Events
    public static FSM_Event ev_done = new FSM_Event(0);
    public static FSM_Event ev_error = new FSM_Event(1);
    public static FSM_Event ev_secDone = new FSM_Event(2);
    

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
        this.fsm_receive_event(ev_done, null);
    }
    
    public void analyseCurCell() {
        //System.out.println(body.getCurrent());
        //Analyses
        //check area
        int areaWeight = this.areaWeight(body.getCurrent(), 2);
        //Weight toggle for changin area
        if(areaWeight <4){
            //Find good Cell system
        }
        //Define if it's time to go :
        Cell c = body.getCurrent();
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
    
    public void waitForIt(){
        body.makeHimWait((Integer)fsm_param);
        this.fsm_receive_event(ev_done, null);
    }
    
    public void stay(){
        System.out.println("An error occured, FSM crashed");
        System.out.println((String)fsm_param);

        fsmGo=false;
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
        //ev_error
        picking.addTransition(ev_error, staying);
        shifting.addTransition(ev_error, staying);
        analysing.addTransition(ev_error, staying);
        //ev_secDone
        analysing.addTransition(ev_secDone, waiting);
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
                case 10:
                    w=3;
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
