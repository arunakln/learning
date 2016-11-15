clc
clear all
close all

%-------------------------------------------------------------------------%
% Author: Aruna Duraisingam
% Date : 01-Aug-2016
% This script performs evaluate time taken for each tasks for each subject 
% and st atistically compared(Kurskalwallis test)
%-------------------------------------------------------------------------%


%% Initialize
no_of_questions = 9;
no_of_channels = 14;
Fs = 128; % Sampling Rate - Emotive Device

subject_files = {'1.edf';'3.edf'; '4.edf';'5.edf'; 
                '6.edf'; '8.edf';'9.edf'; '10.edf'};

now_time = datetime('now');
dtstr = datestr(now_time,'dd_mm_yyyy_HH_MM');

time_easy = [];
time_medium = [];
time_difficult = [];

% Looping subjects
for s= 1: size(subject_files, 1)
    % Read Data
    [hdr, data] = edfread(strcat('edf/', subject_files{s}));
    
    % get the start, end  marker for each task

    start_marker_array = find(data(36,:) == 49);
    end_marker_array = find(data(36,:) == 50);
    
        %% Time Analysis for based on question type (easy, medium and difficult)         
    for q = 1:no_of_questions        
        % individual question data based on marker
        quest_raw_data= data(3:3, start_marker_array(q): end_marker_array(q));

        % Compute time for each qestion
        time_data(s,q,:) =size(quest_raw_data,2)/128;
        
        if(q==1 || q == 4 || q== 7)
            time_easy(end+1) = time_data(s,q,:);
        end

        if(q==2 || q == 5 || q== 8)
            time_medium(end+1) = time_data(s,q,:);
        end

        if(q==3 || q == 6 || q== 9)
            time_difficult(end+1) = time_data(s,q,:);
        end
    end
   
    %% Clear all temporary variables
    
    clearvars quest_raw_data ;
    
    disp(strcat('SubjectID: ', num2str(s),' - Sucessfully Completed'));
end


    %% kurkalwallis Test each subject, each feature     

   filename = strcat('Result/Time_', dtstr, '.xls');
    

    % Add the jar files related to XLWrite to java path.
    javaaddpath('jxl.jar');
    javaaddpath('MXL.jar');

    import mymxl.*;
    import jxl.*; 
    
    index =1;
    
    d.data{index, 1} = char('EasyMeanRank');
    d.data{index, 2} = char('MediumMeanRank');
    d.data{index, 3} = char('HardMeanRank');
    d.data{index, 4} = char('EasyMean');
    d.data{index, 5} = char('MediumMean');
    d.data{index, 6} = char('HardMean');
    d.data{index, 7} = char('PValue');
    
    index = index+1;

    allData = [time_easy'; time_medium';  time_difficult']; 
    groups = [ones(size(time_easy')); 2 * ones(size(time_medium')); 3 * ones(size(time_difficult'))];

    [P,ANOVATAB,STATS] = kruskalwallis(allData, groups);
    

    if(size(STATS.meanranks(1,:), 2) >=1)
        d.data{index, 1}= STATS.meanranks(1,1);
    else
        d.data{index, 1} = 0;
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
    
    d.data{index, 4}= mean(time_easy);
    d.data{index, 5}= mean(time_medium);
    d.data{index, 6}= mean(time_difficult);

    d.data{index, 7}= P;

    % Save the result to a file
    xlwrite(filename,d.data);
    
    xlwrite(strcat('Result/Time_data_', dtstr, '.xls'), horzcat (time_easy', time_medium', time_difficult'));
    
    disp('All subjects processed completely !!!');
 
    %% Box Plot
figure;
boxplot([time_easy',time_medium', time_difficult'],'Notch','on','Labels',{'Easy','Medium', 'Difficult'});
title('Time Analysis - Compare different Task Difficulty Level');
xlabel('Task Difficulty Level');
ylabel('Seconds');


