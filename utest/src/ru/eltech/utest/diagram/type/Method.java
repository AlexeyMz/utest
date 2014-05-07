package ru.eltech.utest.diagram.type;

import java.util.ArrayList;
import java.util.List;

public class Method {
	private String name;
	private List<String> params = new ArrayList<String>();

	public Method(String name) {
		this.name = name;
	}

	public void addParam(String param) {
		params.add(param);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}