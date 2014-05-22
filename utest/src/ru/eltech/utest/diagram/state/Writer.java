package ru.eltech.utest.diagram.state;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

import ru.eltech.utest.diagram.type.Method;

import com.thoughtworks.xstream.*;

public class Writer {

	public static void main(String[] args) throws FileNotFoundException {
    	Method pop_ = new Method("pop()", "void");
    	Method push_ = new Method("push()", "void");
    	Action pop = new Action("pop()", "top > 1 & top < max - 2","", "Неполный", pop_);
    	Action push1 = new Action("push(k)", "top = max - 2","", "Полный", push_);
    	Action pop1 = new Action("pop()", "","", "Неполный", pop_);
    	Action pop3 = new Action("pop()", "top=1", "", "Пустой", pop_);
    	Action push = new Action("push(k)", "top < max - 2", "", "Неполный", push_);
    	Action push2 = new Action("push(k)", "top = max - 2", "", "Полный", push_);
    	CompositeState e = new CompositeState("Непустой");
    	e.AddAction(pop3);
    	State state1 = new State("Неполный");
    	state1.addAction(pop);
    	state1.addAction(push1);
    	e.addState(state1);
    	State state2 = new State("Полный");
    	state2.addAction(pop1);
    	e.addState(state2);
    	State state3 = new State("Пустой");
    	state3.addAction(push);
    	state3.addAction(push2);
    	//e.addState(state3);
    	
        XStream xstream = new XStream();
        xstream.alias("state", CompositeState.class);
        String xml = xstream.toXML(e);
        String xml1 = xstream.toXML(state3);
        System.out.print(xml);
        
        PrintWriter out = new PrintWriter(new FileOutputStream("statechartDiagram.xml"));
        out.println(xml);
        out.println(xml1);
        out.close();
        //Write to a file in the file system
//        try {
//            FileOutputStream fs = new FileOutputStream("D:/employeedata.txt");
//            XStream.toXML(e, fs);
//        } catch (FileNotFoundException e1) {
//            e1.printStackTrace();
//        }
	}
}