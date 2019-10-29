%ICA on two unif. ind. sources

clear all, clc, close all
s=[rand(1,1000)
rand(1,1000)];  %sources

s=s-(ones(1000,1)*mean(s'))';
original_mixing_matrix=rand(2,2);%random choice
%original_mixing_matrix=[1 1.7; 1.5 2];%manual choice
x=original_mixing_matrix*s;%mixing the components
cr=cov(x');
figure;
plot(s(1,:),s(2,:),'*'), title('Data');  axis equal



close all

%%PCA
[E,D]=eig(cr);
y_pca=E'*x;     
%%%ICA
X=x; 
[icasig,A,W]=fastica(X,'approach','symm','g','tanh');



f1=figure; 

plot(x(1,:),x(2,:),'*');
t1=title('Observation Space')
hold on;
mod1=3*sqrt(E(1,1)^2+E(2,1)^2);
mod2=3*sqrt(E(1,2)^2+E(2,2)^2);
l1=line([0 E(1,1)]/mod1, [0 E(2,1)]/mod1);
hold on;
l2=line([0 E(1,2)]/mod2, [0 E(2,2)]/mod2)
axis equal
axis([-0.8 0.8 -0.8 0.8])

set(l1,'LineStyle','-.','LineWidth',2,'Color',[1 0 0])
set(l2,'LineStyle','-.','LineWidth',2,'Color',[1 0 0]);
lab1=xlabel('x1');
lab2=ylabel('x2');

axis equal


%%%

mod1=3*sqrt(A(1,1)^2+A(2,1)^2);
mod2=3*sqrt(A(1,2)^2+A(2,2)^2);

l3=line([0 A(1,1)]/mod1, [0 A(2,1)]/mod1,'Color',[0 1 1]);
l4=line([0 A(1,2)]/mod2, [0 A(2,2)]/mod2,'Color',[0 1 1]);

set(l3,'LineStyle','-','Color',[0 1 0],'LineWidth',2)
set(l4,'LineStyle','-','Color',[0 1 0],'LineWidth',2);


lab1=xlabel('x1');
lab2=ylabel('x2');



legend([l1, l3],'PCA','ICA')

V=D^(-0.5)*E';


y_wht=V*x;

figure;
plot(y_wht(1,:),y_wht(2,:),'*');

axis equal

t2=title('After Whitening')

zA=V*A;
zE=V*E;
hold on;
mod1=0.5*sqrt(zE(1,1)^2+zE(2,1)^2);
mod2=0.5*sqrt(zE(1,2)^2+zE(2,2)^2);

l1=line([0 zE(1,1)]/mod1, [0 zE(2,1)]/mod1);
hold on;
l2=line([0 zE(1,2)]/mod2, [0 zE(2,2)]/mod2)
axis equal
axis([-3 3 -3 3])
set(l1,'LineStyle','-.','LineWidth',2,'Color',[1 0 0])
set(l2,'LineStyle','-.','LineWidth',2,'Color',[1 0 0]);


mod1=0.5*sqrt(zA(1,1)^2+zA(2,1)^2);
mod2=0.5*sqrt(zA(1,2)^2+zA(2,2)^2);

l3=line([0 zA(1,1)]/mod1, [0 zA(2,1)]/mod1,'Color',[0 1 1]);
l4=line([0 zA(1,2)]/mod2, [0 zA(2,2)]/mod2,'Color',[0 1 1]);

set(l3,'LineStyle','-','Color',[0 1 0],'LineWidth',2)
set(l4,'LineStyle','-','Color',[0 1 0],'LineWidth',2);


lab1=xlabel('z1');
lab2=ylabel('z2');


t2=title('After whitening')

zA=V*A;
zE=V*E;
hold on;
mod1=0.5*sqrt(zE(1,1)^2+zE(2,1)^2);
mod2=0.5*sqrt(zE(1,2)^2+zE(2,2)^2);

l1=line([0 zE(1,1)]/mod1, [0 zE(2,1)]/mod1);
hold on;
l2=line([0 zE(1,2)]/mod2, [0 zE(2,2)]/mod2)
axis equal
axis([-3 3 -3 3])
set(l1,'LineStyle','-.','LineWidth',2,'Color',[1 0 0])
set(l2,'LineStyle','-.','LineWidth',2,'Color',[1 0 0]);


mod1=0.5*sqrt(zA(1,1)^2+zA(2,1)^2);
mod2=0.5*sqrt(zA(1,2)^2+zA(2,2)^2);

l3=line([0 zA(1,1)]/mod1, [0 zA(2,1)]/mod1,'Color',[0 1 1]);
l4=line([0 zA(1,2)]/mod2, [0 zA(2,2)]/mod2,'Color',[0 1 1]);

set(l3,'LineStyle','-','Color',[0 1 0],'LineWidth',2)
set(l4,'LineStyle','-','Color',[0 1 0],'LineWidth',2);


lab1=xlabel('z1');
lab2=ylabel('z2');


legend([l1, l3],'PCA','ICA')

%%%ICA
X=x; 
[icasig,A,W]=fastica(X,'approach','symm','g','tanh');

figure;
plot(icasig(1,:),icasig(2,:),'*')
axis equal



t2=title('After ICA')

zA=W*A;
zE=W*E;
hold on;
mod1=0.5*sqrt(zE(1,1)^2+zE(2,1)^2);
mod2=0.5*sqrt(zE(1,2)^2+zE(2,2)^2);
% mod1=1;
% mod2=1;
l1=line([0 zE(1,1)]/mod1, [0 zE(2,1)]/mod1);
hold on;
l2=line([0 zE(1,2)]/mod2, [0 zE(2,2)]/mod2)
axis equal
axis([-3 3 -3 3])
set(l1,'LineStyle','-.','LineWidth',2,'Color',[1 0 0])
set(l2,'LineStyle','-.','LineWidth',2,'Color',[1 0 0]);


mod1=0.5*sqrt(zA(1,1)^2+zA(2,1)^2);
mod2=0.5*sqrt(zA(1,2)^2+zA(2,2)^2);

l3=line([0 zA(1,1)]/mod1, [0 zA(2,1)]/mod1,'Color',[0 1 1]);
l4=line([0 zA(1,2)]/mod2, [0 zA(2,2)]/mod2,'Color',[0 1 1]);

set(l3,'LineStyle','-','Color',[0 1 0],'LineWidth',2)
set(l4,'LineStyle','-','Color',[0 1 0],'LineWidth',2);


lab1=xlabel('s1');
lab2=ylabel('s2');


legend([l1, l3],'PCA','ICA')
