package ru.eltech.utest.predicate;

import static ru.eltech.utest.predicate.JPLUtils.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ExecutionException;

import jpl.Atom;
import jpl.Compound;
import jpl.JPL;
import jpl.PrologException;
import jpl.Query;
import jpl.Term;
import jpl.Variable;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.TokenStream;

import parser.ocl.OclLexer;
import parser.ocl.OclParser;
import parser.ocl.OclParser.LogicalExpressionContext;
import parser.ocl.OclParser.OclExpressionContext;
import parser.ocl.OclParser.OclFileContext;
import ru.eltech.utest.diagram.state.Action;
import ru.eltech.utest.diagram.state.State;
import ru.eltech.utest.diagram.state.StateDiagram;
import ru.eltech.utest.diagram.state.TestWriter;
import ru.eltech.utest.predicate.OclToPrologTranslator.TranslationResult;

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
	
	private static void testOclExpression() {
		try {
			StringReader reader = new StringReader(
				"(str = 'foo' or str = 'bar') and (top < max and top + 1 > 4) and (str = 'foo' implies top > 10)");
			ANTLRInputStream is = new ANTLRInputStream(reader);
			OclLexer lexer = new OclLexer(is);
			TokenStream ts = new CommonTokenStream(lexer);
			OclParser parser = new OclParser(ts);
			
			LogicalExpressionContext context = parser.logicalExpression();
			//context.inspect(parser);
			
			OclToPrologTranslator translator = new OclToPrologTranslator();
			TranslationResult result = translator.translate(context);
			System.out.println("PrologTerm: " + result.prologTerm.toString());
			
			Query query = new Query(result.prologTerm);
			if (query.hasMoreSolutions()) {
				Hashtable solution = query.nextSolution();
				for (Object key : solution.keySet()) {
					System.out.println(String.format("%s = %s", key, solution.get(key)));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RecognitionException e) {
			e.printStackTrace();
		} catch (PrologException e) {
			e.printStackTrace();
		}
	}
	
	private static void testStateAction() {
		StateDiagram diagram = TestWriter.createStackDiagram();
		Tester tester = new Tester(diagram, diagram.getRoot().getChildren().get(0),
				diagram.getRoot().getChildren().get(0).getActions().get(1));
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
			testOclExpression();
		} catch (PrologException e) {
			e.printStackTrace();
		}
	}
}
