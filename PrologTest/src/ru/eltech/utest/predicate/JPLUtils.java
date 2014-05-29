package ru.eltech.utest.predicate;

import jpl.Atom;
import jpl.Compound;
import jpl.Term;

public final class JPLUtils {
	private JPLUtils() {}
	
	public static Term[] terms(Term... terms) {
		return terms;
	}
	
	public static Atom atom(String atom) {
		return new Atom(atom);
	}
	
	public static jpl.Integer atom(int value) {
		return new jpl.Integer(value);
	}
	
	public static Atom atom(boolean value) {
		return new Atom(String.valueOf(value));
	}
	
	public static Compound compound(String head, Term... args) {
		return new Compound(head, args);
	}
	
	public static Term list(Term... elements) {
		Term result = new Atom("[]");
		for (int i = elements.length - 1; i >= 0; i--) {
			result = compound(".", elements[i], result);
		}
		return result;
	}
	
	public static Term foldl(String head, Term... elements) {
		if (elements.length < 2) 
			throw new IllegalArgumentException();
		Term result = elements[0];
		for (int i = 1; i < elements.length; i++) {
			result = compound(head, result, elements[i]);
		}
		return result;
	}
}
