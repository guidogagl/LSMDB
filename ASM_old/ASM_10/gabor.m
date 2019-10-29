
%Trasformata di Gabor: implementa la trasformata di Gabor e la sua trasformata di Fourier 

clear,clc,
close all

RGB = imread('Normoesposta.jpg');
Imm = rgb2gray(RGB);      %converte l'immagine da RGB a scale di grigio

figure
imagesc(Imm),(colormap(gray))
% load Liver
% I=abs(LiverImage);
I=Imm(1:30:2837,1:30:2837);

dx=1;              %campionamento spaziale (fittizio)
delta_y=dx;

%----------immagine di partenza---------------
% figure('Name','Immagine di partenza')
% colormap(gray(256));
% image(I),title('immagine di partenza');

FI=abs(fftshift(fft2(I,256,256)));
figure
imagesc(FI)

[ri,co]=size(I);

%---------------realizzazione dei filtri-----------------

L=length(FI)-1;           %dimensione della maschera
L1=length(FI);
x=[0:dx:L];
Nx=length(x);
y=[0:dx:L];
Ny=length(y);
sigma_val=[7];          %si possono provare diversi valori della sigma
dettaglio=dx;

fx=-1/(2*dx):1/(Nx*dx):1/(2*dx)-1/(Nx*dx);
fy=-1/(2*delta_y):1/(Ny*delta_y):1/(2*delta_y)-1/(Ny*delta_y);

M=8;          %numero di campionamenti della fase di K
nimax=6;       %numero di campionamenti del modulo di K
f=sqrt(2);

k_max=pi/2/dettaglio;

%%%%%quantizzazione del modulo
k_max_val = zeros(1,nimax);
for my_index = 0:(nimax-1)
    k_max_val(my_index+1) = k_max/(f^my_index);
end
k_max_val

%%%quantizzazione della fase
angolo = zeros(1,M);
for my_index = 0:(M-1)
    angolo(my_index+1) = 2*pi*my_index/M;
end
angolo
Ix=length(angolo);
Iy=length(k_max_val);
I1=zeros(1:Ix,1:Iy);

% tutte_G=zeros(length(x),length(y),length(k_max_val)*length(angolo)*length(sigma_val));
tutte_G=zeros(L1,L1,length(k_max_val)*length(angolo)*length(sigma_val));

count=0;
% g=zeros(length(x),length(y),length(k_max_val),length(angolo)); %allocazione memoria
g=zeros(L1,L1,length(k_max_val),length(angolo)); %allocazione memoria

rapp=[];
tot_g=[];           %rapp e tot_g servono pre rappresentare tutte le risposte impulsive insieme
for ni=1:length(k_max_val)
    rapp=[];
    for mu=1:length(angolo)
            k_modulo=k_max_val(ni);
            phi=angolo(mu);
            k_fase=exp(i*phi);
            k=k_modulo*k_fase;
          
            for u=1:length(sigma_val)
                sigma=sigma_val(u);

                for ind1_g = 1:length(x)
                    parte_reale=x(ind1_g) - (L/2);
                    
                    for ind2_g = 1:length(y)
                        parte_immaginaria=y(ind2_g) - (L/2);
                        g(ind1_g, ind2_g,ni,mu)=abs(k)^2/sigma^2*exp(-abs(k)^2*abs(parte_reale+i*parte_immaginaria)^2/(2*sigma^2))*(exp(i*(real(k)*parte_reale + imag(k)*parte_immaginaria))-exp(-sigma^2/2));
                    end
                   
                end
                
%                figure, mesh(x,y,real(g(:,:,ni,mu))),title(['Gabor (sigma=',num2str(sigma),', phi=', num2str(phi*180/pi),', ni=', num2str(ni),', mu=', num2str(mu),') '])
%                view(-36,30)
                              
                G=fftshift(fft2(g(:,:,ni,mu),L1,L1));     %Trasformata di Fourier della g
                
%                 figure, mesh(fx,fy,abs(G)),title(['Trasformata della Gabor (sigma=',num2str(sigma),', phi=', num2str(phi*180/pi),', ni=', num2str(ni),', mu=', num2str(mu),')'])
%                 view(-36,30)
                
                count=count+1;
                
                tutte_G(:,:,count)=G;
                Int=sum(sum(abs(G.*FI)));
                I1(mu,ni)=Int;
                
            end
            rapp=[rapp,g(:,:,ni,mu)];
    end
    tot_g=[tot_g;rapp];

end

figure
imagesc(I1)

% % figure('Name','tutti i filtri nello spazio'),
% % imagesc(real(tot_g)), colormap('gray');
% % % 
figure('Name','tutti i filtri in frequenza'),
imagesc(fx,fy,sum(abs(tutte_G),3)),view(0,90);
% % % 
% % % 
% % % %---------------------------calcolo dell'uscita convolutiva con una delle maschere di Gabor     --------------------------------- 
% % ni0=2; mu0=6;
% % out=conv2(double(I),g(:,:,ni0,mu0),'same');
% % % 
% % figure('Name',['uscita con ni=',num2str(ni0),' e mu=',num2str(mu0)]),
% % imagesc(abs(out)),title(['immagine di uscita ',', ni=', num2str(ni0),', mu=', num2str(mu0),') ']);
% % 
% % %visualizzazione della maschera usata
% % figure('Name',['g(x,y) con ni=',num2str(ni0),' e mu=',num2str(mu0)]),
% % imagesc(x,y,real(g(:,:,ni0,mu0))),title(['Gabor (sigma=',num2str(sigma),', phi=', num2str(phi*180/pi),', ni=', num2str(ni0),', mu=', num2str(mu0),') '])
% % % view(0,90)
% % 
% % figure('Name',['G(u,v) con ni=',num2str(ni0),' e mu=',num2str(mu0)]),  
% % % mesh(fx,fy,abs(fftshift(fft2(g(:,:,ni0,mu0))))),title(['Trasformata della Gabor (sigma=',num2str(sigma),', phi=', num2str(phi*180/pi),', ni=', num2str(ni0),', mu=', num2str(mu0),')'])
% % imagesc(x,y,abs(fftshift(fft2(g(:,:,ni0,mu0))))),title(['Trasformata della Gabor (sigma=',num2str(sigma),', phi=', num2str(phi*180/pi),', ni=', num2str(ni0),', mu=', num2str(mu0),')'])
% % % view(-36,30)
