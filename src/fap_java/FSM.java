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
    
    private Player body;
    
    private boolean fsmGo;
    
    //States
    public static FSM_State picking = new FSM_State(0,"pickCell");
    public static FSM_State shifting = new FSM_State(1,"shiftToPicked");
    public static FSM_State analysing = new FSM_State(2,"analyseCurCell");
    //Events
    public static FSM_Event ev_done = new FSM_Event(0);

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
        
        Cell c = bestCells.get(Tools.randRange(0, bestCells.size()-1));
        nextCell = c;
        this.fsm_receive_event(ev_done, null);
    }

    public void shiftToPicked() {
        body.shiftStick(0,0);
        this.fsm_receive_event(ev_done, null);
    }
    
    public void analyseCurCell() {
        this.fsm_receive_event(ev_done, null);
    }
    
    public void fsm_receive_event(FSM_Event ev, Object param){
        currentEvent = ev;
                //trace(currentState+"-"+currentEvent);
                prevState = currentState;
                FSM_State nextState = currentState.getNextState(ev);
                //trace(currentState+"-"+currentEvent+"-"+nextState);
                currentState=nextState;
                //trace("transition "+currentState);
                //fsmGo = 1;
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
        picking.addTransition(ev_done, shifting);
        shifting.addTransition(ev_done, analysing);
        analysing.addTransition(ev_done, picking);
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
}
