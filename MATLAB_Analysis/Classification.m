clc 
clear all
close all

%-------------------------------------------------------------------------%
% Author: Aruna Duraisingam
% Date : 20-Aug-2016
% Main script to import the inputand target data from Excel sheet and
% perform 10 fold classification based on task difficulty level (Easy,
% medium and difficult).
%-------------------------------------------------------------------------%

% import input and target data from excel sheet
data = importdata('ClassifyData/AllFeatures.xlsx');

% define the input and target data. The size of the inputdata and the
% target data should be same. The size should be same between class records
% as well.

inp =data.InputData;
target = data.TargetData;


% Cross validation function automatically splits into 10 folds data
% (default) and perfro recursively based on the input and the target data.
% fun is a function where the feedforward neural network is designed and
% evaluated.

vals = crossval(@(XTRAIN, YTRAIN, XTEST, YTEST)networkFunction(XTRAIN, YTRAIN, XTEST, YTEST), inp, target);

