package ru.eltech.utest.predicate;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenSource;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.misc.Interval;

import parser.ocl.OclLexer;
import parser.ocl.OclParser;
import parser.ocl.OclParser.OclFileContext;
import ru.eltech.utest.diagram.state.Action;
import ru.eltech.utest.diagram.state.State;
import ru.eltech.utest.diagram.state.StateDiagram;
import ru.eltech.utest.diagram.state.TestWriter;
import jpl.Atom;
import jpl.Compound;
import jpl.JPL;
import jpl.PrologException;
import jpl.Query;
import jpl.Term;
import jpl.Util;
import jpl.Variable;

public final class Program {
	
	public static class Foo {
		public int bar(int j) {
			return 42 * j;
		}
		
		public Foo self() {
			return this;
		}
	}
	
	@SuppressWarnings("rawtypes")
	private static void testProlog() {
		Variable Y = new Variable("Y");
		Term goal = new Compound("java_test", new Term[] { new Atom("Y = S#self#self#bar(2)"), JPL.newJRef(new Foo()), Y });
		System.out.println("Goal: " + goal.toString());
		Query q = new Query(goal);
		while (q.hasMoreSolutions()) {
			Hashtable solution = q.nextSolution();
			System.out.println("Y = " + solution.get(Y.name));
		}
	}
	
	private static void testOcl() {
		try (FileInputStream fis = new FileInputStream("test.ocl")) {
			ANTLRInputStream is = new ANTLRInputStream(fis);
			OclLexer lexer = new OclLexer(is);
			TokenStream ts = new CommonTokenStream(lexer);
			OclParser parser = new OclParser(ts);
			
			OclFileContext context = parser.oclFile();
			System.out.println(context.toStringTree());
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RecognitionException e) {
			e.printStackTrace();
		}
	}
	
	private static void testStateAction() {
		StateDiagram diagram = TestWriter.createStackDiagram();
		Tester tester = new Tester(diagram, diagram.getSimpleStates().get(0),
				diagram.getSimpleStates().get(0).getActions().get(1));
		tester.run();
	}
	
	private static class Tester {
		private final StateDiagram diagram;
		private final State state;
		private final Action action;
		
		public Tester(StateDiagram diagram, State state, Action action) {
			this.diagram = diagram;
			this.state = state;
			this.action = action;
		}
		
		private static Term[] terms(Term... terms) {
			return terms;
		}
		
		private static Atom atom(String atom) {
			return new Atom(atom);
		}
		
		private static Compound compound(String head, Term... args) {
			return new Compound(head, args);
		}
		
		private static Term list(Term... elements) {
			Term result = new Atom("[]");
			for (int i = elements.length - 1; i >= 0; i--) {
				result = compound(".", elements[i], result);
			}
			return result;
		}
		
		private static <T> String join(Iterable<T> items, String separator) {
			StringBuilder builder = new StringBuilder();
			boolean first = true;
			for (T item : items) {
				if (first) {
					first = false;
				} else {
					builder.append(separator);
				}
				builder.append(item.toString());
			}
			return builder.toString();
		}
		
		@SuppressWarnings({ "rawtypes" })
		public void run() {
			List<Variable> arithmeticVars = new ArrayList<>();
			arithmeticVars.add(new Variable("Top"));
			arithmeticVars.add(new Variable("Max"));
			
			String vars = join(arithmeticVars, ", ");
			String search = String.format("[%s] ins 0..1000, %s, %s, label([%s])",
					vars, state.getInvariant(), action.getPrecondition(), vars);
			
			Term[] bindings = new Term[arithmeticVars.size()];
			for (int i = 0; i < arithmeticVars.size(); i++) {
				bindings[i] = compound("=", atom(arithmeticVars.get(i).name), arithmeticVars.get(i));
			}
			
			Term goal = compound("ocl_test", atom(search), list(bindings));
			System.out.println(goal);
			
			Query query = new Query(goal);
			if (query.hasMoreSolutions()) {
				Hashtable solution = query.nextSolution();
				for (Object key : solution.keySet()) {
					System.out.println(String.format("%s = %s", key, solution.get(key)));
				}
			}
		}
	}
	
	public static void main(String[] args) {
		Query query = new Query("consult", new Term[] { new Atom("utest.pl") });
		System.out.println("consult " + (query.hasSolution() ? "succeeded" : "failed"));
		try {
			testStateAction();
		} catch (PrologException e) {
			e.printStackTrace();
		}
	}
}
