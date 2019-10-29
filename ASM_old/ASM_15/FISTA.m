function x= FISTA(mask,y,mu,numIter)

t=1;
m=numel(y);
N=numel(mask);
rho=1/2;%1/lpschitz constant
tmp=0*mask;
tmp(mask==1)=y;
Aty=ifft(tmp)*sqrt(m);
x=Aty;
r=Aty;
prevObj=inf;
for it=1:numIter
    Ar=fft(r).*mask/sqrt(m);
    AtAr=ifft(Ar)*sqrt(m);
    grad_r=0.5*(AtAr-Aty);
    
    xprev=x;
    
    x=r-rho*grad_r;
    x=(1-rho*mu./abs(x)).*((1-rho*mu./abs(x))>0).*x;%soft Tresh
    
    tprev=t;
    t=(1+sqrt(1+4*t^2))/2;
    r=x+(tprev-1)/t*(x-xprev);
       
    figure(100),subplot(1,2,1),stem(abs(x)),title(sprintf('iter %i%',it)),drawnow
    lsqrTerm=norm(y-Ar(mask==1))^2;
    l1Term=norm(x,1);
    obj=lsqrTerm+mu*l1Term;
    %if (prevObj<obj && it>20),break,end;
    prevObj=obj;
    fprintf('Iter %i: obj=%5.4f l1=%5.4f lsqrTerm=%5.4f (est. sigma= %5.4f) \n',it,obj,l1Term,lsqrTerm,std(y-Ar(mask==1)));
    subplot(1,2,2),semilogy(it,(obj),'*'),hold on, grid on,drawnow,title('obj')
  
end
