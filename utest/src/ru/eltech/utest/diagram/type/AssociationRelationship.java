package ru.eltech.utest.diagram.type;

import java.util.ArrayList;
import java.util.List;

/**
 * Отношение ассоциации.
 */
public class AssociationRelationship {
	private List<Class> firstClass = new ArrayList<Class>();
	private List<Class> secondClass = new ArrayList<Class>(); //классы, связанные отношением ассоциации
	
	public AssociationRelationship(Class first, Class second) {
		firstClass.add(first);
		secondClass.add(second);
	}
	
	public void addAssociationRelationship(Class first, Class second) {
		firstClass.add(first);
		secondClass.add(second);
	}
	
	public Class getFirstClass(int i) {
		return firstClass.get(i);
	}
	
	public Class getSecondClass(int i) {
		return secondClass.get(i);
	}
}
