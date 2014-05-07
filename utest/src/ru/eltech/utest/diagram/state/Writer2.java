package ru.eltech.utest.diagram.state;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import test.Lll;

import com.thoughtworks.xstream.*;

public class Writer2 {

	public static void main(String[] args) {
		Lll e = new Lll();

		// Объект-сериализатор
		XStream xstream = new XStream();
		xstream.alias("person", Lll.class);
		String xml = "<Lll>   <name>Jack</name> <num>20</num> <id>10</id> </Lll>";
		Lll newL = (Lll) xstream.fromXML(xml);
		System.out.print(newL.getName());

		// Write to a file in the file system
		/*
		 * try { FileOutputStream fs = new
		 * FileOutputStream("D:/employeedata.txt"); XStream.toXML(e, fs); }
		 * catch (FileNotFoundException e1) { e1.printStackTrace(); }
		 */
	}
}