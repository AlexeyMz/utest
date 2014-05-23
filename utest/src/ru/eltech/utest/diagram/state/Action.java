package ru.eltech.utest.diagram.state;

import ru.eltech.utest.diagram.type.Method;

/**
 * ��������
 */
public class Action {
	private String name; // ��������
	private String precondition; // �����������
	private String postcondition; // �����������
	private Method method; // ��������
	private State endState; // �������� ���������
	
	public Action(String name, String precondition, String postcondition, Method method, State endState){
    	this.name = name;
    	this.precondition = precondition;
    	this.postcondition = postcondition;
    	this.method = method;
    	this.endState = endState;
    }
	
	public String getName() {
        return name;
    }
    
    public String getPrecondition() {
    	return precondition;
    }
    
    public String getPostcondition() {
    	return postcondition;
    }
    
    public Method getMethod() {
    	return method;
    }
    
    public State getEndState() {
    	return endState;
    }
}