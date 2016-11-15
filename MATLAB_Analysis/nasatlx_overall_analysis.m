clc
clear all
close all

%-------------------------------------------------------------------------%
% Author: Aruna Duraisingam
% Date : 01-Aug-2016
% This script analyses NASA TLX data (subjective measure) which is the
% feedback provided by the participants. It analyses and perform kurkalwallis 
% test for each question type (easy, % medium and hard) and all features like 
% mental demand, physical demand, temporal demand, effort, frustration and 
% performance
%-------------------------------------------------------------------------%

%% Initializing

no_of_questions = 9;
no_of_subjects = 10;

% Excel file.
now_time = datetime('now');
dtstr = datestr(now_time,'dd_mm_yyyy_HH_MM');

filename = 'NasaTLX/Nasatlxdata.xlsx';
sheet = 1;
xlRange = 'B2:D31';

overall_data = xlsread(filename,sheet,xlRange);

   %% kurkalwallis Test each subject, each feature     

   filename = strcat('Result/NasaTLX_overall_', dtstr, '.xls');
   
    % Add the jar files related to XLWrite to java path.
    javaaddpath('jxl.jar');
    javaaddpath('MXL.jar');

    import mymxl.*;
    import jxl.*; 
    index =1;
    
    d.data{index, 1} = char('EasyMean');
    d.data{index, 2} = char('MediumMean');
    d.data{index, 3} = char('HardMean');
    d.data{index, 4} = char('EasyMeanRank');
    d.data{index, 5} = char('MediumMeanRank');
    d.data{index, 6} = char('HardMeanRank');
    d.data{index, 7} = char('PValue');
    
    index = index+1;

    %% Perform Kurskalwallis test for all feature combined and for each question type
    allData = [overall_data(:,1); overall_data(:,2);  overall_data(:,3)]; 
    groups = [ones(size(overall_data(:,1))); 2 * ones(size(overall_data(:,2))); 3 * ones(size(overall_data(:,3)))];

    [P,ANOVATAB,STATS] = kruskalwallis(allData, groups, 'off');

    if(size(STATS.meanranks(1,:), 2) >=1)
        d.data{index, 1}= STATS.meanranks(1,1);
    else
        d.data{index, 1}= 0;
    end

    if(size(STATS.meanranks(1,:), 2) >=2)
        d.data{index, 2}= STATS.meanranks(1,2);
    else
        d.data{index, 2} = 0;
    end

    if(size(STATS.meanranks(1,:), 2) >=3)
        d.data{index, 3}= STATS.meanranks(1,3);
    else
        d.data{index, 3} = 0;
    end
    
    d.data{index, 4} = mean(overall_data(:,1));
    d.data{index, 5} = mean(overall_data(:,2));
    d.data{index, 6} = mean(overall_data(:,3));
    
    d.data{index, 7} = P;

    xlwrite(filename,d.data); 
    
    disp('All subjects processed completely !!!');
    
        %% Box Plot
figure;
boxplot([overall_data(:,1), overall_data(:,2), overall_data(:,3)],'Notch','on','Labels',{'Easy','Medium', 'Difficult'});
title('NASA TLX Overall Analysis - Compare different Task Difficulty Level');
xlabel('Task Difficulty Level');
ylabel('Rating * Weight (Mean)');