package ru.eltech.utest.diagram.type;

import java.util.ArrayList;
import java.util.List;

/**
 * Отношение обобщения.
 */
public class GeneralizationRelationship {
	private List<Class> basicClass = new ArrayList<Class>(); // главный класс
	private List<Class> relativClass = new ArrayList<Class>(); // зависимый класс
	
	public GeneralizationRelationship(Class first, Class second) {
		basicClass.add(first);
		relativClass.add(second);
	}
	
	public void addGeneralizationRelationship(Class first, Class second) {
		basicClass.add(first);
		relativClass.add(second);
	}
	
	public Class getBasicClass(int i) {
		return basicClass.get(i);
	}
	
	public Class getRelativClass(int i) {
		return relativClass.get(i);
	}
}
