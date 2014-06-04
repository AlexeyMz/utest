package ru.eltech.utest.diagram.state;

public class StateDiagram {
	private CompositeState root;
	
	public StateDiagram() {
		this(null);
	}
	
	public StateDiagram(CompositeState root) {
		this.root = root;
	}
	
	public CompositeState getRoot() {
		return root;
	}
	
	public void setRoot(CompositeState root) {
		this.root = root;
	}
}
