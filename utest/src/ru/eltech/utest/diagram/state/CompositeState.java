package ru.eltech.utest.diagram.state;

import java.util.ArrayList;
import java.util.List;

/**
 * Составное состояние
 */
public class CompositeState {
	private String name; // имя
	private List<Action> action = new ArrayList<Action>(); // список действий
	private List<State> stateList = new ArrayList<State>(); // список простых состояний

	public CompositeState(String name) {
		this.name = name;
	}
	
	public void AddAction(Action action1) {
		action.add(action1);
	}

	public void addState(State newState) {
		stateList.add(newState);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}