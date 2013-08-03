package fap_java;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.HashMap;
import java.util.Map;

public class FSM{

    private FSM_State currentState;
    private FSM_State prevState;
    private FSM_Event currentEvent;

    private boolean fsmGo;
    
    //States
    public static FSM_State picking = new FSM_State(0,"pickCell");
    public static FSM_State shifting = new FSM_State(1,"shiftToPicked");
    public static FSM_State analysing = new FSM_State(2,"analyseCurCell");
    //Events
    public static FSM_Event ev_done = new FSM_Event(0);

    public FSM() {
    //    super(id, c, game, pc, t);
        fsmGo = true;
        currentState = picking;
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
        System.out.println("PickCell, sending Done");
        this.fsm_receive_event(ev_done, null);
    }

    public void shiftToPicked() {
        System.out.println("ShiftToPicked, sending Done");
        this.fsm_receive_event(ev_done, null);
    }
    
    public void analyseCurCell() {
        System.out.println("Analyse, sending Done");
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
}
