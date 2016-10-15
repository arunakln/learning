
clc

s = serial('COM11');

set(s,'BaudRate',9600);
fopen(s);
fprintf(s,'%d', 1) %send marker to COM port
out = fscanf(s);
s
out
fclose(s)
delete(s)
clear s

% delete(instrfindall)


% pport= digitalio('parallel','LPT1');
% get(pport,'PortAddress');  % obtain the port address using
% addline(pport, 0:7, 'out'); % define the pins 2-9 as output pins, by using addline function
% putvalue(pport,0);
% % gvalue=getvalue(pport); % to see the values in parallel port
% 
% 
% putvalue(pport,0); % put some value in port
% putvalue(pport,1);
% 
