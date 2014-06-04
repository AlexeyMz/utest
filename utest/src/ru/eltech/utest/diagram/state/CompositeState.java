package ru.eltech.utest.diagram.state;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * —оставное состо€ние
 */
public class CompositeState extends State {
	private List<State> children = new ArrayList<State>(); // список вложенных состо€ний

	public CompositeState(String name, String invariant) {
		super(name, invariant);
	}
	
	public List<State> getChildren() {
		return Collections.unmodifiableList(children);
	}

	public void addChild(State newState) {
		children.add(newState);
	}
}