package ru.eltech.utest.diagram.state;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StateDiagram {
	private List<State> simpleStates = new ArrayList<>();
	private List<CompoundState> compoundStates = new ArrayList<>();
	
	public List<State> getSimpleStates() {
		return Collections.unmodifiableList(simpleStates);
	}
	
	public void addSimpleState(State state) {
		simpleStates.add(state);
	}
	
	public List<CompoundState> getCompoundStates() {
		return Collections.unmodifiableList(compoundStates);
	}
	
	public void addCompoundState(CompoundState state) {
		compoundStates.add(state);
	}
}
