%sintesi, mescolamento e decomposizione di segnali temporali
dt=0.001;
Tobs=2;
t=0:dt:2;
f1=5;
s1=sawtooth(2*pi*5*t);
f2=25;s2=0.7*sin(2*pi*f2*t);
s3=randn(1,length(t));
figure;subplot(3,1,1);plot(t,s1);subplot(3,1,2);plot(t,s2);subplot(3,1,3);plot(t,s3);
A=randn(3);
x=A*[s1;s2;s3];
figure;subplot(3,1,1);plot(t,x(1,:));subplot(3,1,2);
plot(t,x(2,:));subplot(3,1,3);plot(t,x(3,:));
y=fastica(x,'interactivePCA','on','g','skew');
figure;subplot(3,1,1);plot(t,y(1,:));subplot(3,1,2);
plot(t,y(2,:));subplot(3,1,3);plot(t,y(3,:));
c=corrcoef(s3,y(3,:))

