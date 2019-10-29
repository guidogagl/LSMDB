load filt_ord_300_bp_2_30.mat
clean=zeros(32001,66);
for k=1:66
    clean(:,k)=filter(Num,1,VEP132sec(:,k));
end
dataxICA=clean(:,1:61)';
icasig=fastica(dataxICA,'interactivePCA','on');
[icasig,A,W]=fastica(dataxICA,'interactivePCA','on');
    