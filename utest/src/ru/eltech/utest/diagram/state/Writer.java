package ru.eltech.utest.diagram.state;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import com.thoughtworks.xstream.*;

public class Writer {

	public static void main(String[] args) throws FileNotFoundException {
		Action pop = new Action("pop()", "top > 1 & top < max - 2", "",
				"Неполный");
		Action push1 = new Action("push(k)", "top = max - 2", "", "Полный");
		Action pop1 = new Action("pop()", "", "", "Неполный");
		Action pop3 = new Action("pop()", "top=1", "", "Пустой");
		Action push = new Action("push(k)", "top < max - 2", "", "Неполный");
		Action push2 = new Action("push(k)", "top = max - 2", "", "Полный");
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
		e.addState(state3);

		// Объект-сериализатор
		XStream xstream = new XStream();
		xstream.alias("state", CompositeState.class);
		String xml = xstream.toXML(e);
		System.out.print(xml);

		PrintWriter out = new PrintWriter(new FileOutputStream("out.xml"));
		out.println(xml);
		out.close();
		// Write to a file in the file system
		/*
		 * try { FileOutputStream fs = new
		 * FileOutputStream("D:/employeedata.txt"); XStream.toXML(e, fs); }
		 * catch (FileNotFoundException e1) { e1.printStackTrace(); }
		 */
	}
}