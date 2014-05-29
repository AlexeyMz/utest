package ru.eltech.utest.predicate;

import static ru.eltech.utest.predicate.JPLUtils.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import jpl.Compound;
import jpl.Term;
import jpl.Variable;

import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.sun.corba.se.impl.orb.ParserTable;
import com.sun.org.apache.bcel.internal.generic.FNEG;

import parser.ocl.OclBaseListener;
import parser.ocl.OclBaseVisitor;
import parser.ocl.OclParser;
import parser.ocl.OclParser.ArithmeticBinaryContext;
import parser.ocl.OclParser.ArithmeticExpressionContext;
import parser.ocl.OclParser.ArithmeticUnaryContext;
import parser.ocl.OclParser.InnerExpressionContext;
import parser.ocl.OclParser.LiteralContext;
import parser.ocl.OclParser.LogicalBinaryContext;
import parser.ocl.OclParser.LogicalExpressionContext;
import parser.ocl.OclParser.LogicalNegationContext;
import parser.ocl.OclParser.OclExpressionContext;
import parser.ocl.OclParser.PostfixExpressionContext;
import parser.ocl.OclParser.PrimaryCallContext;
import parser.ocl.OclParser.RelationContext;

public class OclToPrologTranslator {
	private class AttrInfo {
		public final String name;
		public final boolean isNumber;
		
		private Variable var;
		
		public AttrInfo(String name, boolean isNumber) {
			this.name = name;
			this.isNumber = isNumber;
		}
		
		public Variable getVar() {
			if (var == null) {
				String prologName = name.substring(0, 1).toUpperCase() + name.substring(1);
				var = new Variable(prologName);
			}
			return var;
		}
	}
	
	private Map<String, AttrInfo> attributes = new HashMap<>();
	
	public OclToPrologTranslator() {
		attributes.put("top", new AttrInfo("top",  true));
		attributes.put("max", new AttrInfo("max", true));
		attributes.put("str", new AttrInfo("str", false));
	}
	
	public TranslationResult translate(LogicalExpressionContext expression) {
		ParseTreeWalker walker = new ParseTreeWalker();
		//walker.walk(new TranslationListener(), expression);
		TranslatorVisitor visitor = new TranslatorVisitor();
		Term term = visitor.visit(expression);
		return new TranslationResult(term);
	}
	
	public static class TranslationResult {
		public final Term prologTerm;
		
		public TranslationResult(Term term) {
			this.prologTerm = term;
		}
	}
	
	private static Term ifNotNull(Term t, Function<Term, Term> f) {
		return t == null ? null : f.apply(t);
	}
	
	private static Term ifNotNull(Term left, Term right, BiFunction<Term, Term, Term> f) {
		return left == null || right == null ? null : f.apply(left, right);
	}
	
	private static <U, V> V apply(U u, Function<U, V> f) {
		return f.apply(u);
	}
	
	private class TranslatorVisitor extends OclBaseVisitor<Term> {
		@Override
		public Term visitLogicalBinary(LogicalBinaryContext ctx) {
			ConstrainBuilder builder = new ConstrainBuilder();
			Term term = builder.visitLogicalBinary(ctx);
			if (term != null)
				return builder.wrap(term);
			
			Term left = ctx.left.accept(this);
			Term right = ctx.right.accept(this);
			if (ctx.op.getType() == OclParser.AND) {
				return compound(",", left, right);
			} else if (ctx.op.getType() == OclParser.OR) {
				return compound(";", left, right);
			} else if (ctx.op.getType() == OclParser.IMPLIES) {
				return compound(";", compound("->", left, right), atom(true));
			} else {
				throw new IllegalStateException();
			}
		}

		@Override
		public Term visitLogicalNegation(LogicalNegationContext ctx) {
			ConstrainBuilder builder = new ConstrainBuilder();
			Term term = builder.visitLogicalNegation(ctx);
			if (term != null)
				return builder.wrap(term);
			
			return compound("not", ctx.logicalExpression().accept(this));
		}
		
		@Override
		public Term visitRelation(RelationContext ctx) {
			String op = null;
			switch (ctx.op.getType()) {
			case OclParser.EQUAL: op = "="; break;
			case OclParser.LE: op = "=<"; break;
			case OclParser.GE: op = ">="; break;
			case OclParser.LT: op = "<"; break;
			case OclParser.GT: op = ">"; break;
			case OclParser.NEQUAL: op = "\\="; break;
			default: throw new IllegalStateException("Unknown relation op.");
			}
			
			final String relationOp = op;
			return compound(relationOp, ctx.left.accept(this), ctx.right.accept(this));
		}

		@Override
		public Term visitArithmeticUnary(ArithmeticUnaryContext ctx) {
			Term t = ctx.postfixExpression().accept(this);
			return ctx.MINUS() == null ? t : compound("-", t);
		}

		@Override
		public Term visitArithmeticBinary(ArithmeticBinaryContext ctx) {
			String op = null;
			switch (ctx.op.getType()) {
			case OclParser.PLUS: op = "+"; break;
			case OclParser.MINUS: op = "-"; break;
			case OclParser.MULT: op = "*"; break;
			case OclParser.DIVIDE: op = "/"; break;
			default: throw new IllegalStateException("Unknown arithmetic op.");
			}
			
			final String arithmeticOp = op;
			return compound(arithmeticOp, ctx.left.accept(this), ctx.right.accept(this));
		}
		
		@Override
		public Term visitInnerExpression(InnerExpressionContext ctx) {
			return ctx.logicalExpression().accept(this);
		}
		
		@Override
		public Term visitPostfixExpression(PostfixExpressionContext ctx) {
			// TODO: add built-in props support
			return ctx.propertyCall().isEmpty() ? ctx.primaryExpression().accept(this) : null;
		}

		@Override
		public Term visitLiteral(LiteralContext ctx) {
			if (ctx.STRING() != null) {
				String str = ctx.STRING().getText();
				return atom(str.substring(1, str.length() - 1));
			} else if (ctx.NUMBER() != null) {
				return atom(Integer.valueOf(ctx.NUMBER().getText()));
			} else {
				throw new IllegalStateException();
			}
		}
		
		@Override
		public Term visitPrimaryCall(PrimaryCallContext ctx) {
			if (ctx.propertyCall().timeExpression() != null
					|| ctx.propertyCall().qualifiers() != null
					|| ctx.propertyCall().propertyCallParameters() != null
					|| ctx.propertyCall().pathName().NAME().size() > 1) {
				return null;
			} else {
				String attrName = ctx.propertyCall().pathName().NAME(0).getText();
				return attributes.get(attrName).getVar();
			}
		}
	}
	
	private class ConstrainBuilder extends OclBaseVisitor<Term> {
		public final Map<String, Variable> variables = new HashMap<>();
		
		public Term wrap(Term term) {
			Term[] prologVars = variables.values().stream().toArray(size -> new Term[size]);
			return foldl(",",
				compound("ins", list(prologVars), compound("..", atom(0), atom(1000))),
				term,
				compound("label", list(prologVars)));
		}
		
		@Override
		public Term visitLogicalNegation(LogicalNegationContext ctx) {
			return ifNotNull(ctx.logicalExpression().accept(this), t -> compound("#\\", t));
		}

		@Override
		public Term visitLogicalBinary(LogicalBinaryContext ctx) {
			String op = apply(ctx.op.getType(), type -> {
				switch (type) {
				case OclParser.AND: return "#/\\";
				case OclParser.OR: return "#\\/";
				case OclParser.IMPLIES: return "#==>";
				default: throw new IllegalStateException();
				}
			});
			return ifNotNull(ctx.left.accept(this), ctx.right.accept(this), (l, r) -> compound(op, l, r));
		}
		
		@Override
		public Term visitRelation(RelationContext ctx) {
			String op = null;
			switch (ctx.op.getType()) {
			case OclParser.EQUAL: op = "#="; break;
			case OclParser.LE: op = "#=<"; break;
			case OclParser.GE: op = "#>="; break;
			case OclParser.LT: op = "#<"; break;
			case OclParser.GT: op = "#>"; break;
			case OclParser.NEQUAL: op = "#\\="; break;
			default: throw new IllegalStateException("Unknown relation op.");
			}
			
			final String relationOp = op;
			return ifNotNull(ctx.left.accept(this), ctx.right.accept(this), (l, r) -> compound(relationOp, l, r));
		}

		@Override
		public Term visitArithmeticUnary(ArithmeticUnaryContext ctx) {
			return ifNotNull(ctx.postfixExpression().accept(this),
					t -> ctx.MINUS() == null ? t : compound("-", t));
		}

		@Override
		public Term visitArithmeticBinary(ArithmeticBinaryContext ctx) {
			String op = null;
			switch (ctx.op.getType()) {
			case OclParser.PLUS: op = "+"; break;
			case OclParser.MINUS: op = "-"; break;
			case OclParser.MULT: op = "*"; break;
			case OclParser.DIVIDE: op = "/"; break;
			default: throw new IllegalStateException("Unknown arithmetic op.");
			}
			
			final String arithmeticOp = op;
			return ifNotNull(ctx.left.accept(this), ctx.right.accept(this), (l, r) -> compound(arithmeticOp, l, r));
		}

		@Override
		public Term visitPostfixExpression(PostfixExpressionContext ctx) {
			// TODO: add built-in props support
			return ctx.propertyCall().isEmpty() ? ctx.primaryExpression().accept(this) : null;
		}

		@Override
		public Term visitInnerExpression(InnerExpressionContext ctx) {
			return ctx.logicalExpression().accept(this);
		}

		@Override
		public Term visitLiteral(LiteralContext ctx) {
			return ctx.NUMBER() == null ? null : atom(Integer.valueOf(ctx.NUMBER().getText()));
		}

		@Override
		public Term visitPrimaryCall(PrimaryCallContext ctx) {
			if (ctx.propertyCall().timeExpression() != null
					|| ctx.propertyCall().qualifiers() != null
					|| ctx.propertyCall().propertyCallParameters() != null
					|| ctx.propertyCall().pathName().NAME().size() > 1) {
				return null;
			} else {
				String attrName = ctx.propertyCall().pathName().NAME(0).getText();
				AttrInfo info = attributes.get(attrName);
				if (info.isNumber) {
					variables.put(attrName, info.getVar());
					return info.getVar();
				} else {
					return null;
				}
			}
		}
	}
}
