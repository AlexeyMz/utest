package ru.eltech.utest.diagram.type;

import java.util.ArrayList;
import java.util.List;

public class Method {
	private String name;
	private String returnType;
	private List<String> params = new ArrayList<String>();
	private List<String> typeParams = new ArrayList<String>();

	public Method(String name, String returnType) {
		this.name = name;
		this.returnType = returnType;
	}

	public void addParam(String param, String type) {
		params.add(param);
		typeParams.add(type);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
}