package fap_java;

public class FSM_Event {
    private int value;
    public FSM_Event(int v) {
        this.value = v;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
