% RISPOSTA IN FREQUENZA DI UN ARRAY DI TRASDUTTORI ULTRASONICI
%  - caratterizzazione dell'array con parametri standard - 

close all
clear all

%c=velocità degli ultrasuoni nel mezzo in esame (acqua);
%rho=densità;
%a=larghezza del singolo trasduttore;
%b=altezza del singolo trasduttore;
%d=distanza fra due elementi trasduttori;
%n=numero di elementi trasduttori;
%zf=distanza del bersaglio;
%fm=frequenza a centro banda;
%fs=frequenza a -3dB;

xl=258; %258
zl=150; %150
%PARTE 1 : definizione del trasduttore ultrasonico a parametri standard

c=1500;rho=1;a=.002;b=.01;d=.002;n=10;fm=2.0e+06;fs=0.1e+06;     % parametri del trasduttore
% ws=2*pi*fs;  %ws è la varianza della gaussiana e riduce la banda quando aumenta
% wm=2*pi*fm;
ws=fs;  %ws è la varianza della gaussiana e riduce la banda quando aumenta
wm=fm;


trans=(n*d)^2/(4*c/fm)
zf=trans/2;    % fuoco calcolato per essere in campo vicino

wf=linspace((fm-fs),(fm+fs),zl);                                    % linspace genera 130 punti tra +/-0.027 

w=linspace((wm-ws),(wm+ws),zl);                                    % linspace genera 130 punti tra +/-0.027 
z=linspace(-.027,.027,zl);
y=linspace(-.005,.005,zl);
x=linspace(-.01,.01,xl);
[W,X]=meshgrid(w,x);    % meshgrid trasforma i vettori x e w in una matrice per poter fare il plot in 3D

% a) generazione di B(x,w)
%tiene conto della risoluzione assiale e laterale;

for j=1:zl  % 130
     for h=1:xl  %258
    BW(h,j)=(sinc((a*w(j)*x(h))/(zf*c)).^2).*(sin(n*d*w(j)*x(h))/(zf*c))./(sin((d*w(j)*x(h))/(zf*c))).^2;
     end
end

% figure
% plot(abs(BW(50:210,1)))

% b) generazione di b(x,z)
for k=1:xl
     bz(k,1:zl)=fftshift(ifft(BW(k,1:zl)));                %fftshift serve per centrare la antitrasformata sull'asse del tempo, sfruttano la periodicità
%     bz(k,1:zl)=fftshift(ifft(BW(k,h)));                %fftshift serve per centrare la antitrasformata sull'asse del tempo, sfruttano la periodicità

end

RW=(2*pi*ws^2)^(-.5).*exp(-((w-wm).^2)/(2*ws^2));
figure
plot(w,RW)

% d) generazione di r(t)

rt=fftshift(ifft(RW));

% e) generazione di I(w)
%propagazione dell'onda lungo l'asse y;

for j=1:zl
    IW(j)=trapz(y,((cos((w(j)*y.^2)/(zf*c))).*(sinc((w(j)*y*b)/(zf*c))).^2));
end

figure
plot(w,IW)

% f) generazione di H(x,w)
%risposta totale del sistema;

RI=RW.*IW;

figure
plot(RI)

RI1=RI;
for j=2:xl
    RI=[RI;RI1];
end

H=(rho/(i*(4*pi*c*zf)^2))*W.^3.*RI.*BW;  %risposta totale del sistema;

% g) generazione di h(x,z)

for j=1:xl
    h(j,1:zl)=fftshift(ifft(H(j,1:zl)));  %risposta impulsiva del sistema
end
h1(:,:)=h(120:140,50:80);

% figure
% mesh(abs(h1)),title('Risposta impulsiva h')

figure 
%Visualizzazione di B(x,w)
subplot (2,1,1), surf(wf,x(119:139)*1000,abs(BW(119:139,:))), xlabel('wf [MHz]'), ylabel('x [mm]'),title('B(x,w) , trasduttore classico'),axis tight,shading interp,rotate3d on
%  subplot (2,1,1), mesh(wf,x(109:149)*1000,abs(BW(109:149,:))), xlabel('wf [MHz]'), ylabel('x [mm]'),title('B(x,w) , trasduttore classico'),axis tight,shading interp,rotate3d on

%Visualizzazione di b(x,z)
subplot (2,1,2),mesh(abs(bz)),xlabel('z [cm]'),ylabel('x [mm]'),title('b(x,z)')


figure 
%Visualizzazione di H(x,w)
subplot (2,1,1),surf(wf,x(109:149)*1000,abs(H(109:149,:))),xlabel('wf [MHz]'),ylabel('x [mm]'),title('H(x,w) ,trasduttore classico'),axis tight,shading interp,rotate3d on
%Visualizzazione di h(x,z)
subplot (2,1,2),surf (z,x(109:149)*1000,abs(h(109:149,:))),xlabel('z [cm]'),ylabel('x [mm]'),title('h(x,z)'),axis tight,shading interp,rotate3d on


% h) Definizione degli ingressi e visualizzazione 

%Ingresso 1 
in=zeros(xl,zl);                                     % definisco una matrice 258x130 di zeri 
in(128,68)=1;in(128,72)=1;in(133,68)=1;in(133,72)=1;     % nella matrice di prima posiziono quattro 1 (r80,c58 ; r80 ,c78 ; r150, c58 ; r150 ,c78) 
figure 
subplot(2,1,1),mesh(z*100,x*1000,in),xlabel('z [cm]'),ylabel('x [mm]'),title('Ingresso '),axis tight,rotate3d on,shading interp
subplot(2,1,2),surf(z*100,x*1000,in),xlabel('z [cm]'),ylabel('x [mm]'),title('Ingresso (vista dall''alto)'),axis tight,rotate3d on,view(0,90),shading interp   %vista dall'alto dell'ingresso 3

%uscita 1

out=conv2(in,abs(h1),'same');  
% figure
% mesh(out)
%Rappresentazione dell'uscita all'ingresso 1
figure 
subplot(2,1,1),mesh(z*200,x*200,out),xlabel('z [cm]'),ylabel('x [mm]'),title('Uscita all''ingresso 1'),shading interp,axis tight,rotate3d on
subplot(2,1,2),mesh(z*200,x*200,out),xlabel('z [cm]'),ylabel('x [mm]'),title('Uscita (vista dall''alto)'),shading interp,axis tight, rotate3d on, view(0,90)
colormap(gray)

figure
plot(out(117:143,68)),title('sezione trasversale')
figure
plot(out(128,60:120)),title('sezione assiale')


% figure
% imagesc(in(120:140,40:100)),title('sezione trasversale')

% figure
% imagesc(out(100:180,40:140)),title('sezione trasversale')
% colormap(gray)
