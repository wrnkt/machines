import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FiniteAutomaton {
    private State firstState = null;
    private State currentState = null;

    public FiniteAutomaton(State firstState) {
        this.firstState = firstState;
        this.currentState = this.firstState;
    }

    public void updateState(Character c) {
        System.out.println("Current State: " + currentState.getStateName());
        System.out.println("Updating...");
        var nextState = currentState.getNextState(c);
        if( nextState == null ) {
            System.out.println("Next state not available.");
        } else {
            this.currentState = nextState;
        }
        System.out.println("Updated State: " + currentState.getStateName());
        System.out.println("-----------");
    }

    public void evaluate(List<Character> characters) {
        for( Character c : characters ) {
            System.out.println("Input: " + c);
            updateState(c);
        }
    }

    public void printCurrentState() {
        System.out.println(currentState.getStateName());
    }

    public static void main(String... args) {

        State S0 = new State();
        S0.setStateName("S0");
        State S1 = new State();
        S1.setStateName("S1");
        State S2 = new State();
        S2.setStateName("S2");
        State S3 = new State();
        S3.setStateName("S3");

        
        Map<Character, State> map = new HashMap<>();
        map.put('0', S0);
        map.put('1', S1);
        S0.setStateTransitionMap(map);

        map = new HashMap<>();
        map.put('0', S2);
        map.put('1', S1);
        S1.setStateTransitionMap(map);

        map = new HashMap<>();
        map.put('0', S0);
        map.put('1', S3);
        S2.setStateTransitionMap(map);

        map = new HashMap<>();
        map.put('0', S3);
        map.put('1', S3);
        S3.setStateTransitionMap(map);


        FiniteAutomaton fa = new FiniteAutomaton(S0);

        List<Character> chars = "0101010"
              .chars()
              .mapToObj(e -> (char)e)
              .collect(Collectors.toList());

        fa.evaluate(chars);

    }
}

class State {
    private String stateName = "";
    private Map<Character, State> stateTransitionMap = new HashMap<Character, State>();

    public State() {}
    public void setStateName(String name) { this.stateName = name; }
    public String getStateName() { return stateName; }
    
    public void setStateTransitionMap(Map<Character, State> map) {
        this.stateTransitionMap = map;
    }
    public State getNextState(Character c) {
        return stateTransitionMap.get(c);
    }
}
