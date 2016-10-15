function varargout = thankyou(varargin)
% THANKYOU MATLAB code for thankyou.fig
%      THANKYOU, by itself, creates a new THANKYOU or raises the existing
%      singleton*.
%
%      H = THANKYOU returns the handle to a new THANKYOU or the handle to
%      the existing singleton*.
%
%      THANKYOU('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in THANKYOU.M with the given input arguments.
%
%      THANKYOU('Property','Value',...) creates a new THANKYOU or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before thankyou_OpeningFcn gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to thankyou_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help thankyou

% Last Modified by GUIDE v2.5 24-Jun-2016 02:59:47

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @thankyou_OpeningFcn, ...
                   'gui_OutputFcn',  @thankyou_OutputFcn, ...
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


% --- Executes just before thankyou is made visible.
function thankyou_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to thankyou (see VARARGIN)

% Choose default command line output for thankyou
handles.output = hObject;

% Update handles structure
guidata(hObject, handles);

% UIWAIT makes thankyou wait for user response (see UIRESUME)
% uiwait(handles.figure1);


% --- Outputs from this function are returned to the command line.
function varargout = thankyou_OutputFcn(hObject, eventdata, handles) 
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;
% set(gcf, 'units','normalized','outerposition',[0 0 1 1]);
