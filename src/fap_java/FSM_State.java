package fap_java;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;

public class FSM_State implements Serializable{
    
    private int value;
    private Map<FSM_Event, FSM_State> transitionTable = new HashMap<FSM_Event, FSM_State>();
    private String associatedMethod;
    
    /**
     * Creates a new State that the FSM could take
     * @param v
     * @param method
     */
    public FSM_State(int v, String method) {
        value = v;
        this.associatedMethod = method;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    /**
     * Add a next state to take if the FSM encounters a designated event
     * @param ev : the event
     * @param st : the new state
     */
    public void addTransition(FSM_Event ev,FSM_State st){
        transitionTable.put(ev, st);
    }

    public void setAssociatedMethod(String associatedMethod) {
        this.associatedMethod = associatedMethod;
    }

    public String getAssociatedMethod() {
        return associatedMethod;
    }
    
    /**
     * Get the next state to take from this state with the param event
     * @param ev
     * @return : the new state
     */
    public FSM_State getNextState(FSM_Event ev){
        return this.transitionTable.get(ev);
    }

    public String toString(){
        return this.associatedMethod;
    }
}
