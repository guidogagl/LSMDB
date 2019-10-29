function x= FGP(b,lambda,Nit)
    % Total variation denoising,
    % solves min. || x - b||_2^2 + lambda*TV(x)
    %
    %Nit= numbero of iterations of the algorithm
    %
    % Note: Educational only!!!!
    %
    %
    %
    % Author: Giuseppe Valvano giuseppe.valvano@for.unipi.it
    %
    %Reference: 
    %   Beck, A., & Teboulle, M. (2009). Fast gradient-based algorithms 
    %   for constrained total variation image denoising and deblurring 
    %   problems. IEEE Transactions on Image Processing : A Publication 
    %   of the IEEE Signal Processing Society, 18(11), 2419?34. 
    %   doi:10.1109/TIP.2009.2028250
        t=1;
%         P=P*0;Q=Q*0;
    [Nx,Ny]=size(b);
    P=zeros(Nx-1,Nx); Q=zeros(Ny,Ny-1);
        r=P; s=Q;
        for it=1:Nit
            
            
            p_prev=P; q_prev=Q;
            
            [p1,q1]=Lt(b-lambda*L(r,s));
            
            [P,Q]=ProjectOnP(r+1/8/lambda*p1,s+1/8/lambda*q1);
            
            tprev=t;
            t=(1+sqrt(1+4*t^2))/2;
            
            r=P+(t-1)/tprev*(P-p_prev);
            s=Q+(t-1)/tprev*(Q-q_prev);
        end
        
        x=b- lambda*L(P,Q);
    end
    
    function y=L(p,q)
        y=[p(1,:);diff(p,1,1);-p(end,:)]+[q(:,1),diff(q,1,2),-q(:,end)];
    end
    
    function [p,q]=Lt(x)
        p=x(1:end-1,:)-x(2:end,:);
        q=x(:,1:end-1)-x(:,2:end);
        
    end
    function [r,s]=ProjectOnP(p,q)
        a=sqrt(abs(p(:,1:end-1)).^2+abs(q(1:end-1,:)).^2);
        r=[p(:,1:end-1)./max(1,a),p(:,end)./max(1,abs(p(:,end)))];
        s=[q(1:end-1,:)./max(1,a);q(end,:)./max(1,abs(q(end,:)))];
    end