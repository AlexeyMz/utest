package ru.eltech.utest.diagram.type;

import java.util.ArrayList;
import java.util.List;

/**
 * ��������� �����������.
 */
public class DependencyRelationship {
	private List<Class> basicClass = new ArrayList<Class>(); // ������� �����
	private List<Class> relativClass = new ArrayList<Class>(); // ��������� �����
	
	public DependencyRelationship(Class first, Class second) {
		basicClass.add(first);
		relativClass.add(second);
	}
	
	public void addDependencyRelationship(Class first, Class second) {
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
