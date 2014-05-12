:- op(100, yfx, user:(#)).

bind_vars(_, []).
bind_vars(AVs, [A = V|T]) :-
	member(A = V1, AVs) -> V = V1,
	bind_vars(AVs, T).

java_eval(Object#Member, Result) :-
	functor(Member, _, _),
	Member =.. [Name|Args],
	meta(Args, Args1),
	jpl_call(Object, Name, Args1, Result).

meta(T, T) :- var(T), !, print('v: '), print(T), nl.
meta(T, T) :- atomic(T), !, print('a: '), print(T), nl.
meta([Term|T], [Term1|T1]) :-
	(nonvar(Term), java_eval(Term, Term1) ; meta(Term, Term1)), !,
	meta(T, T1).
meta(Term, Term1) :-
	print('meta: '), print(Term), nl,
	Term =.. [Name|Args],
	meta(Args, Args1),
	Term1 =.. [Name|Args1],
	print('meta> '), print(Term1), nl.
	
java_test(E, S, Y) :-
	read_term_from_atom(E, T, [variable_names(AVs)]),
	bind_vars(['S' = S, 'Y' = Y], AVs),
	meta(T, T1),
	T1.