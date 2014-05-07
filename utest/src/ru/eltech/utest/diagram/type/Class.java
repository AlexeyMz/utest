package ru.eltech.utest.diagram.type;

import java.util.ArrayList;
import java.util.List;

public class Class {
	private String name;
	private List<Atribut> attributes = new ArrayList<Atribut>();
	private List<Method> methods = new ArrayList<Method>();

	public Class(String name) {
		this.name = name;
	}

	public void addAttribute(Atribut attribute) {
		attributes.add(attribute);
	}

	public void addMethod(Method method) {
		methods.add(method);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}