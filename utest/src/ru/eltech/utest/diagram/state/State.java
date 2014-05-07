package ru.eltech.utest.diagram.state;

import java.util.ArrayList;
import java.util.List;

/**
 * ���������
 */
public class State {
	private String name;
	private String invariant;
	/**
	 * ������ ��������
	 */
	private List<Action> actionList = new ArrayList<Action>();

	public State(String name) {
		this.name = name;
	}
	
	public void addAction(Action action1) {
		actionList.add(action1);
	}

	public String getName() {
		return name;
	}

	public void setName(String name, String invariant) {
		this.name = name;
		this.invariant = invariant;
	}
}