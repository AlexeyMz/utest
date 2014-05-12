package ru.eltech.utest.predicate;

import java.util.Hashtable;

import jpl.Atom;
import jpl.Compound;
import jpl.JPL;
import jpl.JRef;
import jpl.Query;
import jpl.Term;
import jpl.Variable;

public final class Program {
	
	public static class Foo {
		public int bar(int j) {
			return 42 * j;
		}
	}
	
	public static void main(String[] args) {
		Query query = new Query("consult", new Term[] { new Atom("utest.pl") });
		System.out.println("consult " + (query.hasSolution() ? "succeeded" : "failed"));
		
		Variable Y = new Variable("Y");
		Term term = new Compound("java_test", new Term[] { JPL.newJRef(new Foo()), Y });
		System.out.println(term.toString());
		Query q = new Query(term);
		while (q.hasMoreSolutions()) {
			Hashtable solution = q.nextSolution();
			System.out.println("Y = " + solution.get(Y.name));
		}
	}
}
