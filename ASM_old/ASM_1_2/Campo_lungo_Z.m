%EFFETTI DELL'ALLONTANAMENTO DAL TRASDUTTORE sulla distribuzione di campo
%generato da un trasduttore ultrasonico.

close all
clear all

L=.005;                      %semidimensione del sensore [m]
xo=0;
lambda=0.0003;               %lunghezza d'onda [m]
risz=.0001;
risx=0.0001;
x=[-L:risx:L];             %intervallo del sensore

zo=[0.0001:risz:.50001];    %intervallo di osservazione, si parte da 0.0001 
                             %per evitare di dividere per 0
Zo= length (zo);
X = length(x);
xo=[-5*L:.0001:5*L];         %intervallo di osservazione trasversale
Xo= length (xo);

%Calcolo del campo di pressione lungo l'asse di propagazione del campo

for k= 1:Zo;
    y=fun(x,zo,k,lambda);
    p(k)=trapz(x,y);         %calcolo dell'integrale col metodo trapezoidale
    P=abs(p);
end;

figure(1)
plot (zo,P),title ('VARIAZIONE DI CAMPO LUNGO L''ASSE Z'),  xlabel ('Distanza dal trasduttore [m]'),ylabel('Intensità campo');
zlim_teorica = (2*L)^2/(4*lambda) ;           %distanza teorica di confine zona campo vicino/lontano 
hold on 
index = floor(zlim_teorica/risz);
for h=2:length(P)-1 
    dP(h-1)=P(h+1)/risz-P(h-1)/risz;           %tramite il cilco for definiamo da derivata per poi valutare i punti di  massimo e minimo
end                                            %e trovare quindi l'ultimo massimo prima della zona di campo lontano
dp1=[dP,0]; dp2=[0,dP];
massmin=find(dp1.*dp2<0);
stem (zo(massmin(end)),P(massmin(end)),'g'),stem (zlim_teorica,P(index), 'r'), legend('Andamento del campo', 'Confine Reale'  , 'Confine Teorico'), 
axis tight

figure(2)
plot (zo(4:index),P(4:index)),title ('VARIAZIONE DI CAMPO LUNGO L''ASSE Z NELLA ZONA DI CAMPO VICINO') 
xlabel ('Distanza dal trasduttore [m]'),ylabel('Intensità campo')
axis tight

figure(3)
plot (zo(massmin(end):end),P(massmin(end):end)),title ('VARIAZIONE DI CAMPO LUNGO L''ASSE Z NELLA ZONA DI CAMPO LONTANO') 
xlabel ('Distanza dal trasduttore [m]'),ylabel('Intensità campo')
axis tight
zlim_teorica = (2*L)^2/(lambda);                %distanza teorica

%Calcolo del campo di pressione lungo l'asse trasversale
zvis=[zlim_teorica/2,zlim_teorica/1.5,zlim_teorica,1.5*zlim_teorica,2*zlim_teorica];
for h=1:length(zvis)
for k= 1:Xo;
    for n=1:X
     r(n)=sqrt(zvis(h)^2+(x(n)-xo(k))^2);
     y(n)=(1/zvis(h))*exp(i*((2*pi)/lambda)*r(n));
    end 
    p1(h,k)=trapz(x,y);    %calcolo dell'integrale col metodo trapezoidale
    P1=abs(p1);
end


[massimo, indice] = max(P1(h,:));          % in indice avro' l'indice del massimo
valore_3dB = massimo/(sqrt(2));       %calcolo della larghezza del campo di pressione a -3 dB 
figure
stringa=['VARIAZIONE DI CAMPO LUNGO L''ASSE X con Z_0 = ',num2str(zvis(h)),' m'];
if zvis(h)==zlim_teorica stringa=['VARIAZIONE DI CAMPO LUNGO L''ASSE X con Z_0 = D^2/lamda =z limteorica ',num2str(zvis(h)),' m']; end
plot(xo,P1(h,:), 'LineWidth',1.2),title (stringa), xlabel ('Distanza lungo Xo - Intervallo sensore -0.005 : 0.005'),ylabel ('Intensità campo');
axis tight;
hold on
plot (xo,valore_3dB, '-r', 'LineWidth',2)
legend ('Campo dell''onda US generata dal radiatore','Valore a -3dB')
end

%Allontanadosi dalla superficie del trasduttore si incontrano per prime le
%INTERFERENZE dovute alle sovrapposizioni delle onde sfasate in zona CAMPO
%VICINO. Passando invece in zona di CAMPO LONTANO SPARISCONO LE
%OSCILLAZIONI E LE INTERFERENZE in modo che il campo assomiglia sempre più
%alla TRASFORMATA DELL'APERTURA.
%Allontanadosi dalla superficie del trasduttore il fascio di ultrasuoni
%generato è SEMPRE PIU' LARGO, infatti la r=z*lambda/L.

