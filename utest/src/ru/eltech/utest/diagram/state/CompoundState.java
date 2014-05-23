package ru.eltech.utest.diagram.state;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Составное состояние
 */
public class CompoundState {
	private String name; // имя
	private List<Action> actions = new ArrayList<Action>(); // список действий
	private List<State> simpleStates = new ArrayList<State>(); // список простых состояний

	public CompoundState(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public List<State> getStates() {
		return Collections.unmodifiableList(simpleStates);
	}

	public void addState(State newState) {
		simpleStates.add(newState);
	}
	
	public List<Action> getActions() {
		return Collections.unmodifiableList(actions);
	}
	
	public void addAction(Action action) {
		actions.add(action);
	}
}