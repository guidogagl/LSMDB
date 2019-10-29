% ------------------------------------------------------------------------
% Function: POISSON_COUNT

% ------------------------------------------------------------------------

function [y img]= poisson_count( x, mn, mx )

if nargin == 3
    mn=min(x(:));
  x = x - mn;
  x = x ./ max(x(:));
  img = mn + x * (mx-mn);
else
  img = x;
end
img = img;
y = poissrnd( img );



