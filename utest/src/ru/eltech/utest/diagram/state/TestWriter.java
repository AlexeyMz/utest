package ru.eltech.utest.diagram.state;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

import ru.eltech.utest.diagram.type.Method;

import com.thoughtworks.xstream.*;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class TestWriter {
	
	public static StateDiagram createStackDiagram() {
		State nonFullState = new State("Неполный", "Top #> 0, Top #< Max - 1");
		State fullState = new State("Полный", "Top #> 0, Top #= Max - 1");
		State emptyState = new State("Пустой", "Top #= 0, Top #< Max");
		
		Method popMethod = new Method("pop", "void");
    	Method pushMethod = new Method("push", "void");
    	
    	nonFullState.addAction(new Action("pop()", "Top #> 1, Top #< Max - 2", "", popMethod, nonFullState));
    	nonFullState.addAction(new Action("push(k)", "Top #= Max - 2", "", pushMethod, fullState));
    	
    	fullState.addAction(new Action("pop()", "", "", popMethod, nonFullState));
    	
    	emptyState.addAction(new Action("push(k)", "Top #< Max - 2", "", pushMethod, nonFullState));
    	emptyState.addAction(new Action("push(k)", "Top #= Max - 2", "", pushMethod, fullState));
    	
    	CompoundState nonEmptyCompound = new CompoundState("Непустой");
    	nonEmptyCompound.addState(nonFullState);
    	nonEmptyCompound.addState(fullState);
    	nonEmptyCompound.addAction(new Action("pop()", "Top #= 1", "", popMethod, emptyState));
    	
    	StateDiagram diagram = new StateDiagram();
    	diagram.addSimpleState(emptyState);
    	diagram.addSimpleState(nonFullState);
    	diagram.addSimpleState(fullState);
    	diagram.addCompoundState(nonEmptyCompound);
    	
    	return diagram;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
        XStream xstream = new XStream(new DomDriver());
        xstream.setMode(XStream.ID_REFERENCES);
//        xstream.alias("action", Action.class);
//        xstream.alias("state", State.class);
//        xstream.alias("diagram", StateDiagram.class);
//        xstream.alias("method", Method.class);
        String xml = xstream.toXML(createStackDiagram());
        System.out.print(xml);
        
//        PrintWriter out = new PrintWriter(new FileOutputStream("statechartDiagram.xml"));
//        out.println(xml);
//        out.println(xml1);
//        out.close();
        //Write to a file in the file system
//        try {
//            FileOutputStream fs = new FileOutputStream("D:/employeedata.txt");
//            XStream.toXML(e, fs);
//        } catch (FileNotFoundException e1) {
//            e1.printStackTrace();
//        }
	}
}