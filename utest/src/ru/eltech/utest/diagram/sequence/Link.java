package ru.eltech.utest.diagram.sequence;

import java.util.ArrayList;
import java.util.List;
import ru.eltech.utest.diagram.type.Class;

public class Link {
	// private String link;
	private List<Class> firstObject = new ArrayList<Class>();
	private List<Class> secondObject = new ArrayList<Class>();
	private Message message;
	private String invariant;

	Link(/* String link, */Class first, Class second) {
		// this.link = link;
		firstObject.add(first);
		secondObject.add(second);
	}

	/*
	 * public void setLink(String link){ this.link = link; }
	 * 
	 * public String getLinke(){ return link; }
	 */
	public void addLink(Class first, Class second) {
		firstObject.add(first);
		secondObject.add(second);
	}

	public Class getfirstObject(int i) {
		return firstObject.get(i);
	}

	public Class getRelativClass(int i) {
		return secondObject.get(i);
	}
}
