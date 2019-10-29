%% Es Grad desc
clear all
close all

[x,y] =meshgrid(-10:.1:10,-20:.1:20);

f = 15*x.^2+7*y.^2;
applyF = @(x,y) 15*x.^2+7*y.^2;

figure,subplot(1,2,1),surf(x,y,f),shading interp, axis xy
subplot(1,2,2),contour(x,y,f,20,'LineWidth',3)

%
x0 = -7; y0 = 18;
c = 10;
stepSize = c;
a =.5;

cnt=1;
for it = 1:5
figure(100)
subplot(1,2,1)
contour(x,y,f,20,'k','LineWidth',1),hold on
plot(x0,y0,'ro','MarkerSize',14,'LineWidth',4)

grad = [30*x0; 14*y0];
gradNorm = grad/norm(grad);


x_=-10:1:10;
y_ = y0 + gradNorm(2)/gradNorm(1)*(x_-x0);
plot(x_, y_,'k-.','LineWidth',2);
q=quiver(x0, y0, -gradNorm(1)*4, -gradNorm(2)*4,'b','LineWidth',3);

set(q,'MaxHeadSize',1e2,'AutoScaleFactor',1);


subplot(1,2,2)

x_=-10:a^(it+2):20;
y_ = y0 + gradNorm(2)/gradNorm(1)*(x_-x0);
d = [x_-x0;y_-y0]'*(-gradNorm);

f1 = applyF(x_,y_);
plot(d,f1,'LineWidth',3),grid on, hold on
plot(0,applyF(x0,y0),'ro','MarkerSize',14,'LineWidth',4)
%stepSize = d(find(f1==min(f1))); %exact backtracking
obj = applyF(x0,y0);
ylim([.8*min(f1),2*obj])
for bi = 1:5
    
    x01 = x0 - stepSize*gradNorm(1);
    y01 = y0 - stepSize*gradNorm(2);
    
    d_1 = [x01-x0;y01-y0]'*(-gradNorm);
    
    plot(d_1,applyF(x01,y01),'yo','MarkerSize',14,'LineWidth',4),title(sprintf('StepSize %f', stepSize),'FontSize',20)
    ylim([.8*min(f1), max(2*obj,applyF(x01,y01))]);
    print('-depsc',figure(100), sprintf('gr%i',cnt));
    cnt = cnt+1;
%     pause 
    
    if applyF(x01,y01) < obj
        break
    end
    
    stepSize = stepSize*a;   % ad ogni passaggio viene dimezzato il passo del gradiente
    
    
end

%plot(d(f1==min((f1))),min((f1)),'go','MarkerSize',14,'LineWidth',4)
x0 = x0 - stepSize*gradNorm(1);
y0 = y0 - stepSize*gradNorm(2);

subplot(1,2,1)
plot(x0,y0,'yo','MarkerSize',14,'LineWidth',4)
print('-depsc',figure(100), sprintf('gr%i',cnt));
    cnt = cnt+1;

    % pause

subplot(1,2,1)
hold off
subplot(1,2,2)
hold off


[x,y] =meshgrid(linspace(-3*abs(x0),3*abs(x0),200), linspace(-3*abs(y0),3*abs(y0),200));
f =applyF(x,y);

end



