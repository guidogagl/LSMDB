clear all
u=randn(1,10000);
M=10;
t=0:0.001:10-0.001;
s=sin(2*pi*5*t);
filtro=[1 2 3 4 5 4 3 2 1]/25;
v1=filter(filtro,1,u);
d=s+v1(1:length(s));
w(M,:)=randn(1,M);
for k=M:10000
    y(k)=w(k,:)*u(k:-1:k-M+1)';
    e(k)=d(k)-y(k);
    w(k+1,:)=w(k,:)+0.015*u(k:-1:k-M+1)*e(k);
end
close all

figure;subplot(2,1,1);plot(d);hold on;plot(e,'r-')
subplot(2,1,2);plot(d)
figure;stem(w(end,:));hold on;stem(filtro,'r-')