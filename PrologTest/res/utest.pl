:- op(100, yfx, user:(#)).

bind_vars(_, []).
bind_vars(AVs, [A = V|T]) :-
	member(A = V1, AVs) -> V = V1,
	bind_vars(AVs, T).

meta(T, T) :- var(T), !, print('v: '), print(T), nl.
meta(T, T) :- atomic(T), !, print('a: '), print(T), nl.
meta([Term|T], [Term1|T1]) :-
	meta(Term, Term1), !,
	meta(T, T1).
meta(Object#Member, Result) :-
	functor(Member, _, _),
	Member =.. [Name|Args],
	meta(Object, Object1),
	meta(Args, Args1),
	print('javacall: '), print(Object1#[Name|Args1]), print(' -> '),
	jpl_call(Object1, Name, Args1, Result),
	print(Result), nl.
meta(Term, Term1) :-
	Term =.. [Name|Args],
	print('meta: '), print([Name|Args]), nl,
	meta(Name, Name1),
	meta(Args, Args1),
	Term1 =.. [Name1|Args1],
	print('meta -> '), print([Name1|Args1]), nl.
	
java_test(E, S, Y) :-
	read_term_from_atom(E, T, [variable_names(AVs)]),
	bind_vars(['S' = S, 'Y' = Y], AVs),
	meta(T, T1),
	T1.
