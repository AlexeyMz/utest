:- use_module(library(clpfd)).

testlabeling(X) :- X in 1..10, labeling([], [X]), X > 5.

testnot(X) :- not(X in 1..10), labeling([], [X]).
