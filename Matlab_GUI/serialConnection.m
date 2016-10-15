
% ------ Send the marker to COM port - serial communication.
function serialConnection(handles, marker)
%clc
%s = serial(handls.sPort);


fprintf(handles.sPort,'%d', marker,'async'); %send marker to COM port
disp('Serial Connection Marker sent:');
disp(marker);

%out = fscanf(handles.sPort);
%fclose(s);
%delete(s);
%clear s;