package ru.eltech.utest.diagram.state;

import java.util.ArrayList;
import java.util.List;

/**
 * ��������� ���������
 */
public class CompositeState {
	private String name; // ���
	private List<Action> action = new ArrayList<Action>(); // ������ ��������
	private List<State> stateList = new ArrayList<State>(); // ������ ������� ���������

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