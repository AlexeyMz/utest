package ru.eltech.utest.diagram.state;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

import ru.eltech.utest.diagram.type.Method;

import com.thoughtworks.xstream.*;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class TestWriter {
	
	public static StateDiagram createStackDiagram() {
		State nonFullState = new State("Неполный", "top < max - 1");
		State fullState = new State("Полный", "top = max - 1");
		State emptyState = new State("Пустой", "top = 0, top < max");
		
		Method popMethod = new Method("pop", "void");
    	Method pushMethod = new Method("push", "void");
    	
    	nonFullState.addAction(new Action("pop()", "top > 1, top < Max - 2", "", popMethod, nonFullState));
    	nonFullState.addAction(new Action("push(k)", "Top = Max - 2", "", pushMethod, fullState));
    	
    	fullState.addAction(new Action("pop()", "", "", popMethod, nonFullState));
    	
    	emptyState.addAction(new Action("push(k)", "Top #< Max - 2", "", pushMethod, nonFullState));
    	emptyState.addAction(new Action("push(k)", "Top #= Max - 2", "", pushMethod, fullState));
    	
    	CompositeState nonEmptyCompound = new CompositeState("Непустой", "top > 0");
    	nonEmptyCompound.addChild(nonFullState);
    	nonEmptyCompound.addChild(fullState);
    	nonEmptyCompound.addAction(new Action("pop()", "top = 1", "", popMethod, emptyState));
    	
    	CompositeState root = new CompositeState("", "");
    	root.addChild(emptyState);
    	root.addChild(nonEmptyCompound);
    	
    	return new StateDiagram(root);
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