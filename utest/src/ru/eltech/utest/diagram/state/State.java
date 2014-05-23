package ru.eltech.utest.diagram.state;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Состояние
 */
public class State {
	private String name;
	private String invariant;
	private List<Action> actions = new ArrayList<>(); // список действий

	public State(String name, String invariant) {
		this.name = name;
		this.invariant = invariant;
	}

	public String getName() {
		return name;
	}
	
	public String getInvariant() {
		return invariant;
	}
	
	public List<Action> getActions() {
		return Collections.unmodifiableList(actions);
	}
	
	public void addAction(Action action) {
		actions.add(action);
	}
}