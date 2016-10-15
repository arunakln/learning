function varargout = relax(varargin)
% RELAX MATLAB code for relax.fig
%      RELAX, by itself, creates a new RELAX or raises the existing
%      singleton*.
%
%      H = RELAX returns the handle to a new RELAX or the handle to
%      the existing singleton*.
%
%      RELAX('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in RELAX.M with the given input arguments.
%
%      RELAX('Property','Value',...) creates a new RELAX or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before relax_OpeningFcn gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to relax_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help relax

% Last Modified by GUIDE v2.5 13-Jul-2016 02:28:31

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @relax_OpeningFcn, ...
                   'gui_OutputFcn',  @relax_OutputFcn, ...
                   'gui_LayoutFcn',  [] , ...
                   'gui_Callback',   []);
if nargin && ischar(varargin{1})
    gui_State.gui_Callback = str2func(varargin{1});
end

if nargout
    [varargout{1:nargout}] = gui_mainfcn(gui_State, varargin{:});
else
    gui_mainfcn(gui_State, varargin{:});
end
% End initialization code - DO NOT EDIT


% --- Executes just before relax is made visible.
function relax_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to relax (see VARARGIN)

% Choose default command line output for relax
handles.output = hObject;

img = imread('Image/relax.jpg');
imshow(img,'InitialMagnification','fit');

set(handles.text2,'visible','off');
set(handles.edit1,'visible','off');

handles.sPort = '';

if (length(varargin) >0 && isfield(varargin{1}, 'sPort'))
    handles.sPort = varargin{1}.sPort;
end

handles.timeout = 60;

o = findobj(hObject, 'Tag', 'edit1');
o.String = num2str(handles.timeout);

hObject.UserData = handles;

% intialize timer and set its properties
t = timer('period',1.0);
handles.timerobj = t;
set(t,'ExecutionMode','fixedrate','StartDelay',0);
set(t, 'TimerFcn', {@mytimer, handles, str2double(o.String), hObject});
set(t, 'TasksToExecute', str2double(o.String));

start(t);

% Update handles structure
guidata(hObject, handles);

% UIWAIT makes relax wait for user response (see UIRESUME)
% uiwait(handles.figure1);


% --- Outputs from this function are returned to the command line.
function varargout = relax_OutputFcn(hObject, eventdata, handles) 
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;
% set(gcf, 'units','normalized','outerposition',[0 0 1 1]);



function edit1_Callback(hObject, eventdata, handles)
% hObject    handle to edit1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of edit1 as text
%        str2double(get(hObject,'String')) returns contents of edit1 as a double


% --- Executes during object creation, after setting all properties.
function edit1_CreateFcn(hObject, eventdata, handles)
% hObject    handle to edit1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end

% -------- Display the countdown time to the user
function mytimer(hObject, eventdata, handles,...
timeout, obj)

% get the countdown time and display in the textbox
o = findobj(obj, 'Tag', 'edit1');
obj.UserData.timeout =obj.UserData.timeout-1;
o.String = obj.UserData.timeout;

if(obj.UserData.timeout == 5)
    set(handles.text2,'visible','on');
    set(handles.edit1,'visible','on');
end

% if(obj.UserData.timeout == 0)
%     serialConnection(handles, 1);
%     close(relax);
% end

guidata(obj,handles);


% --- Executes when user attempts to close figure1.
function figure1_CloseRequestFcn(hObject, eventdata, handles)
% hObject    handle to figure1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: delete(hObject) closes the figure
delete(hObject);


% --- Executes on button press in pushbutton1.
function pushbutton1_Callback(hObject, eventdata, handles)
% hObject    handle to pushbutton1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

    serialConnection(handles, 1);
    close(relax);
