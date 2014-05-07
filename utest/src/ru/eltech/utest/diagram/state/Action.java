package ru.eltech.utest.diagram.state;

import ru.eltech.utest.diagram.type.Method;

/**
 * ��������
 */
public class Action {
	private String name; //��������
	private String condition; //�����������
	private String postcondition; //�����������
	private Method action; //��������
	private String name2; //�������� ���������
	
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