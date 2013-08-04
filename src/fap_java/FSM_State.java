package fap_java;

import java.util.HashMap;
import java.util.Map;

public class FSM_State {
    
    private int value;
    private Map<FSM_Event, FSM_State> transitionTable = new HashMap<FSM_Event, FSM_State>();
    private String associatedMethod;
    
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
    
    public void addTransition(FSM_Event ev,FSM_State st){
        transitionTable.put(ev, st);
    }

    public void setAssociatedMethod(String associatedMethod) {
        this.associatedMethod = associatedMethod;
    }

    public String getAssociatedMethod() {
        return associatedMethod;
    }
    
    public FSM_State getNextState(FSM_Event ev){
        return this.transitionTable.get(ev);
    }
    
    public String toString(){
        return this.associatedMethod;
    }
}
