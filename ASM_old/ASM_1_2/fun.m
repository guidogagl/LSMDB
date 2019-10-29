% FUNZIONE che calcola il campo di pressione p(x0=0, z0)

function [y] = fun (x,zo,k,lambda)
r = sqrt(zo(k)^2+x.^2);                          % distanza tra il generico elementino piezoelettrico e
                                                 % il punto di coordinate (x0=0, Z0)
y=(1/(sqrt(zo(k))))*exp(i*((2*pi)/lambda)*r);    %valore della pressione all'interfaccia del trasduttore

%il termine 1/(sqrt(zo(k))) è un termine di correzione che tiene conto del
%fatto che propagandosi l'intensità dell'onda diminuisce come 1/r