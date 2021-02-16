%Question 1
parent(mary,georgeVI).
parent(mary,henry).
parent(mary,george).
parent(georgeV,georgeVI).
parent(georgeV,henry).
parent(georgeV,george).
parent(elizabeth,elizabethII).
parent(georgeVI,elizabethII).
parent(alice,richard).
parent(henry,richard).
parent(elizabethII,charles).
parent(elizabethII,andrew).
parent(elizabethII,anne).
parent(elizabethII,edward).
parent(philip,charles).
parent(philip,andrew).
parent(philip,anne).
parent(philip,edward).
parent(diana,william).
parent(diana,harry).
parent(charles,william).
parent(charles,harry).
parent(sarah,beatrice).
parent(sarah,eugenie).
parent(andrew,beatrice).
parent(andrew,eugenie).
parent(anne,peter).
parent(anne,zara).
parent(mark,peter).
parent(mark,zara).
parent(kate,georgejun).
parent(kate,charlotte).
parent(kate,louis).
parent(william,georgejun).
parent(william,charlotte).
parent(william,louis).
parent(meghan,archie).
parent(harry,archie).

the_royal_females([mary,elizabeth,elizabethII,alice,anne,diana,sarah,
beatrice,zara,eugenie,charlotte,kate,meghan]).
the_royal_males([georgeV,georgeVI,george,philip,charles,andrew,edward,
richard,henry, william,harry,peter,georgejun,mark,louis,archie]).

%1 - finds all the royals
the_royal_family(X) :- the_royal_females(A),the_royal_males(B),append(A,B,X).

%2 - finds all the mothers
mother(X,Y) :- parent(X,Y),the_royal_females(A),member(X,A).

%3 - finds all the grandmas
grandma(X,Y) :- parent(X,Z),parent(Z,Y),the_royal_females(A),member(X,A).

%4 - finds all the parents who have a childs
has_child(X) :- parent(X,_).

%5 - finds all ancestors
ancestor(X,X).
ancestor(X,Y) :- parent(X,Z),ancestor(Z,Y).

%6 - finds who has siblings
sibling(X,Y) :- parent(Z,X),parent(Z,Y).

%7 - finds who has a sisters
sister(X,Y) :- the_royal_females(A),member(X,A),member(Y,A),sibling(X,Y).

%8 - Who is a grandchild of George V?
%parent(georgeV,Y),parent(Y,Z).
%Y = georgeVI,
%Z = elizabethII
%Y = henry,
%Z = richard

%9 - Who has a child (one or more)?
%has_child(X).
%X = mary
%X = georgeV
%X = elizabeth
%X = gerogeVI
%X = alice
%X = henry
%X = elizabethII
%X = philip
%X = diana
%X = charles
%X = sarah
%X = andrew
%X = anne
%X = mark
%X = kate
%X = william
%X = meghan
%X = harry

%10 - Who is an ancestor of Archie?
%ancestor(X,archie).
%X = archie
%X = mary
%X = georgeV
%X = georgeVI
%X = elizabethII
%X = philip
%X = diana
%X = charles
%X = meghan
%X = harry

%11 - Who is a cousin of Eugenie?
%parent(A,eugenie),parent(B,X),sibling(A,B).
%loads of duplicates
%X = william
%X = harry
%X = beatrice
%X = eugenie
%X = peter
%X = zara

has_cousin_who_is_grandma(Ans) :- parent(A,Ans),parent(B,X),sibling(A,B),grandma(X,_).

%12 - Who has a cousin who is grandma?
%parent(A,Ans),parent(B,X),sibling(A,B),grandma(X,Y).
%Ans = richard
%Ans = elizabethII

%13 -  Who has a brother who is a grandfather?
%(sibling(Pers,Brother),the_royal_males(M),member(Brother,M)),(parent(Brother,C),parent(C,D)).
%Pers = henry
%Pers = george
%Pers = georgeVI (himself)
%Pers = charles (himself)
%Pers = andrew
%Pers = anne
%Pers = edward

%--------------------
%Question 2
train(swansea, cardiff, [3,5,8,15,17,18,19,20,23],1,[4,5,6,7,10,14,18,22,23],2).
train(cardiff, manchester, [7,11,16],4,[8,14,19],5).
train(cardiff, bristol, [3,5,7,11,15,18,19,20],2,[5,6,7,10,14,16,18,22],2).
train(manchester, bristol, [5,6,7,8,11,15,18,19,20],4,[5,6,7,10,14,16,18,22],5).
train(manchester, swansea, [7,11,16],5,[8,14,19],6).
train(manchester, london, [6,7,11,16],4,[7,8,14,19],5).
train(cardiff, london, [5,6,7,11,18,19,20],3,[8,9,17,18,19,20,21],3).
train(london, brussels, [6,7,8,11,13,17,18,20],5,[9,11,13,16,17,18,19,23],5).
train(london, paris, [7,11,13,17,18,20],5,[9,11,13,16,18,20],6).
train(paris, brussels, [7,11,17],4,[9,13,19],3).
train(paris, munich, [7,11,13,17,22],8,[5,9,13,19,23],7).
train(munich, vienna, [8,9,11,13,17,19],6,[9,10,12,16,18,23],5).
train(vienna, venice, [5,7,8,10,13,16,12,23],8,[2,4,7,9,12,20,21,23],9).
train(venice, paris, [4,11,20],11,[9,12,21],10).

%Part a)

%checks if there is a direct train between stations.
direct(X,Y,XDeps,XDur):- train(X,Y,XDeps,XDur,_,_); train(Y,X,_,_,XDeps,XDur).

%?- direct(london,manchester,[7,8,14,19],5).
%true 

%part b)
%checks the connections with no vias.
con(From,FDep,V,TArr,To,_):-
	direct(From,To,[FDep|_],FDur),
	TArr2 is FDep + FDur,
	dayTime(TArr,TArr2),
	TArr < 24,
	V = [],
	From\=To.
	
%checks the connections with 1 via.
con(From,FDep,V,TArr,To,_):-
	direct(From,Through,[FDep|_],FDur),
	ThArr is FDep + FDur,
	direct(Through,To,ThDeps,ThDur),
	filterList(ThArr,ThDeps,[ThDep|_]),
	Diff is ThDep-ThArr,
	betweenLR(0,Diff,4),
	TArr2 is ThDep + ThDur,
	dayTime(TArr,TArr2),
	TArr < 24,
	V = [via(ThArr, Through, ThDep)],
	From\=To,From\=Through,Through\=To.
	
%checks the connections with 2 vias.
con(From,FDep,V,TArr,To,_):-
	direct(From,Through,[FDep|_],FDur),
	ThArr is FDep + FDur,
	direct(Through,PTo,ThDeps,ThDur),
	filterList(ThArr,ThDeps,[ThDep|_]),
	Diff is ThDep-ThArr,
	betweenLR(0,Diff,4),
	PTArr is ThDep + ThDur,
	direct(PTo,To,PTDeps,PTDur),
	filterList(PTArr,PTDeps,[PTDep|_]),
	PDiff is PTDep-PTArr,
	betweenLR(0,PDiff,4),
	TArr2 is PTDep + PTDur,
	dayTime(TArr,TArr2),
	TArr < 24,
	V = [via(ThArr, Through, ThDep), via(PTArr,PTo,PTDep)],
	From\=To,From\=PTo,From\=Through,Through\=PTo,Through\=To,PTo\=To.

%checks the connections with 3 vias.
con(From,FDep,V,TArr,To,_):-
	direct(From,Through,[FDep|_],FDur),
	ThArr is FDep + FDur,
	direct(Through,PTo,ThDeps,ThDur),
	filterList(ThArr,ThDeps,[ThDep|_]),
	Diff is ThDep-ThArr,
	betweenLR(0,Diff,4),
	PTArr is ThDep + ThDur,
	direct(PTo,FTo,PTDeps,PTDur),
	filterList(PTArr,PTDeps,[PTDep|_]),
	PDiff is PTDep-PTArr,
	betweenLR(0,PDiff,4),
	FTArr is PTDep + PTDur,
	direct(FTo,To,FTDeps,FTDur),
	filterList(FTArr,FTDeps,[FTDep|_]),
	FTDiff is FTDep-FTArr,
	betweenLR(0,FTDiff,4),
	TArr2 is FTDep + FTDur,
	dayTime(TArr,TArr2),
	TArr < 24,
	V = [via(ThArr, Through, ThDep), via(PTArr,PTo,PTDep), via(FTArr, FTo, FTDep)],
	From\=To,From\=FTo,From\=PTo,From\=Through,Through\=PTo,Through\=FTo,Through\=To,PTo\=FTo,PTo\=To,FTo\=To.

	
betweenLR(L,X,R):- L < R, L1 is L+1, X = L1.
betweenLR(L,X,R):- L < R, L1 is L+1, betweenLR(L1, X, R).

mem_rem(H,[H|T],R):- R = T.
mem_rem(X,[H|T],[H|R]):- mem_rem(X,T,R).

%filters a list to only have the values above X.
filterList(_,[],[]).
filterList(X,[H|T],R):- X>=H, filterList(X,T,R).
filterList(X,[H|T],[H|T]):- X<H.

%shows the connection stations
via(_,_,_).

%keeps times within 0-23 range.	
dayTime(X,Num) :- (Num > 23, X is Num - 24);X is Num.

%part c)
connection(X,XDep,V,YArr,Y):- con(X,XDep,V,YArr,Y,_).

%?- connection(swansea,D,V,A,munich).
%D = 3,
%V = [via(4, cardiff, 5), via(8, london, 11), via(16, paris, 17)],
%A = 1 
