package ru.eltech.utest.diagram.state;

import ru.eltech.utest.diagram.type.Method;

/**
 * Действие
 */
public class Action {
	private String name; //название
	private String condition; //предусловие
	private String postcondition; //постусловие
	private Method action; //действие
	private String name2; //конечное состояние
	
	public Action(String name, String condition, String postcondition, String name2, Method action){
    	this.name = name;
    	this.name2 = name2;
    	this.condition = condition;
    	this.postcondition = postcondition;
    	this.action = action;
    }
	
	public String getName() {
        return name;
    }
	
    public void setName(String name) {
        this.name = name;
    }
}