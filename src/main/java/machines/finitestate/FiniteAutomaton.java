package machines.finitestate;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

class State
{
    private String stateName = null;
    private Map<Character, State> stateTransitionMap = new HashMap<Character, State>();

    public State(String name)
    {
        this.stateName = name;
    }

    public String getStateName()
    {
        return stateName;
    }
    
    public void setStateTransitionMap(Map<Character, State> map)
    {
        this.stateTransitionMap = map;
    }

    public Map<Character, State> getStateTransitionMap()
    {
        return stateTransitionMap;
    }

    public State getNextState(Character c)
    {
        return stateTransitionMap.get(c);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Name:" + stateName + "\n");
        stateTransitionMap.forEach((character, state) -> sb.append(character + " " + state.getStateName() + "\n"));
        return sb.toString();
    }
}

class Transition
{
    Character input;
    State state;
    public Transition(Character input, State state)
    {
        this.input = input;
        this.state = state;
    }
    public State getState()
    {
        return state;
    }
    public Character getInput()
    {
        return input;
    }
}

class TransitionUpdater
{
    public void addTransition(List<State> availableStates, State state, Transition transition) throws IllegalStateException
    {
        if( !availableStates.contains(state) )
        {
            throw new IllegalStateException("Invalid Transition: State " + state.getStateName() + " doesn't exist.");
        }
        if( !availableStates.contains(transition.getState()) )
        {
            throw new IllegalStateException("Invalid Transition: State " + transition.getState().getStateName() + " doesn't exist.");
        }
        state.getStateTransitionMap().put(transition.getInput(), transition.getState());
    }
}

public class FiniteAutomaton
{
    private State firstState = null;
    private State currentState = null;
    private State successState = null;

    public FiniteAutomaton(State firstState)
    {
        setFirstState(firstState);
        addState(firstState);
    }

    private void setFirstState(State firstState)
    {
        this.firstState = firstState;
        this.currentState = this.firstState;
    }

    public void updateState(Character c)
    {
        System.out.println("Current State: " + currentState.getStateName());
        System.out.println("Updating...");
        var nextState = currentState.getNextState(c);
        if( nextState == null ) {
            System.out.println("Next state not available.");
        } else {
            this.currentState = nextState;
        }
        System.out.println("Updated State: " + currentState.getStateName());
        if( this.currentState == successState ) {
            System.out.println("Found pattern.");
        }
        System.out.println("-----------");
    }

    public void setSuccessState(State state)
    {
        this.successState = state;
    }

    public void evaluate(List<Character> characters)
    {
        var sb = new StringBuilder();
        for( Character c : characters ) {
            sb.append(c);
            System.out.println("Input: " + sb.toString() + "|");
            updateState(c);
        }
    }

    public void printCurrentState()
    {
        System.out.println(currentState.getStateName());
    }

    ArrayList<State> availableStates = new ArrayList<>();
    private TransitionUpdater tu = new TransitionUpdater();

    public void addTransition(State state, Transition transition) throws IllegalStateException
    {
        addState(state);
        try {
            tu.addTransition(availableStates, state, transition);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addTransition(State state, Transition... transitions)
    {
        List<Transition> list = Arrays.asList(transitions);
        list.forEach(transition -> addTransition(state, transition));
    }

    public boolean addState(State state)
    {
        var cState = state;
        if( cState == null || updateAvailableStates(cState) == false ) {
            return false;
        }
        
        Collection<State> linkedStates = cState.getStateTransitionMap().values();
        if ( linkedStates.isEmpty() ) {
            return true;
        }
        for( State linkedState : linkedStates ) {
            addState(linkedState);
        }
        return true;
    }

    public void addStates(State... states)
    {
        for( State state : states ) {
            addState(state);
        }
    }
    
    public boolean updateAvailableStates(State state)
    {
        if( availableStates.contains(state) ) return false;
        availableStates.add(state);
        return true;
    }

    public static void main(String... args)
    {
        State S0 = new State("S0");
        State S1 = new State("S1");
        State S2 = new State("S2");
        State S3 = new State("S3");

        FiniteAutomaton fa = new FiniteAutomaton(S0);
        fa.addStates(S1, S2, S3);
        fa.setSuccessState(S3);

        fa.addTransition(S0, new Transition('0', S0), new Transition('1', S1));
        fa.addTransition(S1, new Transition('0', S2), new Transition('1', S1));
        fa.addTransition(S2, new Transition('0', S0), new Transition('1', S3));
        fa.addTransition(S3, new Transition('1', S3), new Transition('1', S3));

        fa.availableStates.forEach(state -> System.out.println(state.toString()));

        List<Character> chars = "011001100101000"
              .chars()
              .mapToObj(e -> (char)e)
              .collect(Collectors.toList());

        fa.evaluate(chars);
    }
}

