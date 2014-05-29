/**
 * Copyright (c) 2001 Alexander V. Konstantinou (akonstan@acm.org)
 *
 * Permission to use, copy, modify, distribute and sell this software
 * and its documentation for any purpose is hereby granted without fee,
 * provided that the above copyright notice appear in all copies and
 * that both that copyright notice and this permission notice appear
 * in supporting documentation.  Alexander V. Konstantinou makes no
 * representations about the suitability of this software for any
 * purpose.  It is provided 'as is' without express or implied warranty.
 *
 * ANTLR Object Constraint Language (OCL) grammar.  This grammar complies
 * with the UML 1.4 OCL grammar (version 0.1c)
 *
 * $Id: ocl.g,v 1.1 2001/05/25 21:32:02 akonstan Exp $
 *
 * The latest version can be obtained at:
 *
 *         http://www.cs.columbia.edu/~akonstan/ocl/
 *
 * References:
 *
 *   - http://www.antlr.org/
 *   - http://www.omg.org/uml/
 *   - http://www.klasse.nl/ocl/
 *
 * History:
 *
 * - Version 1.0: original release
 */

grammar Ocl;

@header {
	package parser.ocl;
}

oclFile
  : ( 'package' packageName oclExpressions 'endpackage' )+
  ;

packageName
  : pathName
  ;

oclExpressions
  : (constraint)*
  ;

constraint 
  : contextDeclaration 
    (   ('def' COLON (NAME)? COLON (letExpression)*)
        | (stereotype (NAME)? COLON oclExpression)
    )+
  ;

contextDeclaration 
  : 'context' (operationContext | classifierContext);

classifierContext
  : (NAME COLON NAME)
  | NAME
  ;

operationContext
  : NAME DCOLON operationName LPAREN formalParameterList RPAREN
    ( COLON returnType )?
    ;

stereotype
    : 'pre'
    | 'post'
    | 'inv'
    ;

operationName
  : NAME 
  | EQUAL
  | PLUS
  | MINUS
  | LT
  | LE
  | GE
  | GT
  | DIVIDE
  | MULT
  | NEQUAL
  | 'implies'
  | 'not'
  | 'or'
  | 'xor'
  | 'and'
  ;

formalParameterList
  : ( NAME COLON typeSpecifier 
      (COMMA NAME COLON typeSpecifier)*
    )?
  ;

typeSpecifier
  : simpleTypeSpecifier
  | collectionType
  ;

collectionType
  : collectionKind LPAREN simpleTypeSpecifier RPAREN
  ;

oclExpression
  : ( ( letExpression )* 'in' )? logicalExpression
  ;

returnType
  : typeSpecifier
  ;

letExpression
  : 'let' NAME ( LPAREN formalParameterList RPAREN )?
    ( COLON typeSpecifier )?
    EQUAL logicalExpression
  ;

logicalExpression
  : 'not' logicalExpression  # LogicalNegation
  | left=logicalExpression op=AND right=logicalExpression  # LogicalBinary
  | left=logicalExpression op=OR right=logicalExpression  # LogicalBinary
  | left=logicalExpression op=IMPLIES right=logicalExpression  # LogicalBinary
  | left=arithmeticExpression op=(EQUAL|GT|LT|GE|LE|NEQUAL) right=arithmeticExpression  # Relation
  | LPAREN logicalExpression RPAREN  # InnerExpression
  ;


arithmeticExpression
  : left=arithmeticExpression op=(MULT|DIVIDE) right=arithmeticExpression  # ArithmeticBinary
  | left=arithmeticExpression op=(PLUS|MINUS) right=arithmeticExpression   # ArithmeticBinary
  | MINUS? postfixExpression  # ArithmeticUnary
  ;

postfixExpression
  : primaryExpression ( (DOT | RARROW) propertyCall )*
  ;

primaryExpression
  : collectionKind LCURLY (collectionItem (COMMA collectionItem)* )? RCURLY  # LiteralCollection
  | (STRING | NUMBER | enumLiteral)  # Literal
  | propertyCall  # PrimaryCall
  | 'if' condition=logicalExpression
    'then' thenExpr=logicalExpression
    'else' elseExpr=logicalExpression
    'endif'  # IfExpression
  ;

propertyCallParameters
    : LPAREN declarator (actualParameterList)? RPAREN
    |   LPAREN (actualParameterList)? RPAREN
  ;

enumLiteral
  : NAME DCOLON NAME ( DCOLON NAME )*
  ;

simpleTypeSpecifier
  : pathName
  ;

collectionItem
  : arithmeticExpression (DOTDOT arithmeticExpression)?
  ;

propertyCall
  : pathName
    timeExpression?
    qualifiers?
    propertyCallParameters?
  ;

qualifiers
  : LBRACK actualParameterList RBRACK
  ;

declarator
  : NAME (COMMA NAME)*
    (COLON simpleTypeSpecifier)?
    (SEMICOL NAME COLON typeSpecifier EQUAL arithmeticExpression)?
    BAR
  ;

pathName
  : NAME (DCOLON NAME)*
  ;

timeExpression
  : ATSIGN 'pre'
  ;

actualParameterList
  : arithmeticExpression (COMMA arithmeticExpression)*
  ;

collectionKind
  : 'Set'
  | 'Bag'
  | 'Sequence'
  | 'Collection'
  ;
  
//////////////////////////////////////////////////////////////////////////////


WS
	:	(' '
	|	'\t'
	|	'\n'  /*{ newline(); }*/
	|	'\r') -> skip
	;

AND             : 'and';
OR              : 'or';
IMPLIES         : 'implies';
LPAREN			: '(';
RPAREN			: ')';
LBRACK			: '[';
RBRACK			: ']';
LCURLY			: '{';
RCURLY			: '}';
COLON			: ':';
DCOLON			: '::';
COMMA			: ',';
EQUAL			: '=';
NEQUAL			: '<>';
LT				: '<';
GT				: '>';
LE				: '<=';
GE				: '>=';
RARROW			: '->';
DOTDOT			: '..';
DOT				: '.';
POUND			: '#';
SEMICOL			: ';';
BAR				: '|';
ATSIGN			: '@';
PLUS			: '+';
MINUS			: '-';
MULT			: '*';
DIVIDE			: '/';

NAME 
    : ( ('a'..'z') | ('A'..'Z') | ('_') ) 
        ( ('a'..'z') | ('0'..'9') | ('A'..'Z') | ('_') )* 
  ;

NUMBER 
  : ('0'..'9')+
    ( /* { LA(2) != '.' }? */ '.' ('0'..'9')+ )?
    ( ('e' | 'E') ('+' | '-')? ('0'..'9')+ )?
  ;

STRING : '\'' ( ( ~ ('\'' | '\\' | '\n' | '\r') )
	| ('\\' ( ( 'n' | 't' | 'b' | 'r' | 'f' | '\\' | '\'' | '\'' )
		| ('0'..'3')
		  (
			options {}
		  :	('0'..'7')
			(	
			  options {}
			:	'0'..'7'
			)?
		  )?
		|	('4'..'7')
		  (
			options {}
		  :	('0'..'9')
		  )? ) ) )* '\''
    ;

SL_COMMENT: ('--' (~'\n')* '\n'
	| '--[' (~']')* (~'\n')* '\n' ) -> skip
        ;
