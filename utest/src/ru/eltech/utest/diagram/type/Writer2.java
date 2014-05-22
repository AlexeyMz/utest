package ru.eltech.utest.diagram.type;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import com.thoughtworks.xstream.XStream;

public class Writer2 {
	public static void main(String[] args) throws FileNotFoundException {
		Method getPhoto= new Method("getPhoto", "void");
		Method getSoundBite= new Method("addParam", "void");
		getPhoto.addParam("p","Photo");
		Atribut name = new Atribut("name", "Name");
		Atribut employeelD = new Atribut("employeelD", "integer");
		Class Person = new Class("Person");
		Person.addMethod(getPhoto);
		Person.addMethod(getSoundBite);
		Person.addAttribute(employeelD);
		Person.addAttribute(name);
		
		Atribut address = new Atribut("address", "String");
		Class contactInformation = new Class("contact Information");
		contactInformation.addAttribute(address);
		
		 DependencyRelationship s1 = new  DependencyRelationship(Person, contactInformation);
		 
		 Atribut nameDepartament = new Atribut("name", "Name");
		 Class department = new Class("department");
		 department.addAttribute(nameDepartament);
		 
		 AssociationRelationship s2 = new AssociationRelationship(department, Person);
     
        //Объект-сериализатор
        XStream xstream = new XStream();
        //xstream.alias("class", Class.class);
        String xml = xstream.toXML(s1);
                System.out.print(xml);
        String xml1 = xstream.toXML(s1);
        PrintWriter out = new PrintWriter(new FileOutputStream("classDiagram.xml"));
        out.println(xml);
        out.println(xml1);
        out.close();
    }
}
