package fap_java;

public class FSM_Event {
    private int value;

    /**
     * Creates a new event that the FSM could encounter
     * @param v
     */
    public FSM_Event(int v) {
        this.value = v;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return "" + value;
    }
}
