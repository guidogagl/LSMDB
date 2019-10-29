function [x,obj] = cgRecNE(y,A,At,numIt,x0,lambda)
%
% Conjugate Gradient Algoritm 
%
% Min. ||A*x-y||_2^2 + lambda*||x||_2^2
%
% INPUT:
%   y = data
%   A = function handle : A(x) = A*x
%   At = function handle : At(x) = A'*x
%   numIt = number of Iterations
%   x0 = starting value
%   lambda = tikhonov reg. weight
%
b = At(y);    % immagine misurata
% figure,imagesc(b)

M = @(x) At(A(x)) + lambda*x;  % definisco la funzione M

x = x0;
r = b- M(x);   %    immagine differenza
p = r; 
rr = sum(abs(r(:)).^2);

for it = 1:numIt
    Ap  = M(p);
    a   = rr/(sum(conj(p(:)).*Ap(:)));
    x   = x + a*p;
    rnew = r - a*Ap;    % derivata
    bb  = (sum(abs(rnew(:)).^2))/rr;
    r   = rnew;
    if mod(it,50) == 0
        r = b- M(x);
    end
    rr  = sum(abs(r(:)).^2);
    p   = r + bb*p;
    
    %evaluate objective
    tmp= abs(A(x)-y).^2;
    obj(it) = sum(tmp(:));
    displayFn()
end

function displayFn()
    figure(100),subplot(1,2,1),imagesc(abs(x)),colormap gray,colorbar
    subplot(1,2,2),semilogy(obj),grid on
    drawnow
end

end