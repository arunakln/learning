clc
clear all
close all

%-------------------------------------------------------------------------%
% Author: Aruna Duraisingam
% Date : 01-Aug-2016
% This script analyses NASA TLX data (subjective measure) which is the
% feedback given by the participants. It analyses and perform kurkalwallis 
% test for each question type (easy, % medium and hard) and each features like 
% mental demand, physical demand, temporal demand, effort, frustration and 
% performance
%-------------------------------------------------------------------------%

%% Intializing the values
no_of_questions = 9;
no_of_subjects = 10;

now_time = datetime('now');
dtstr = datestr(now_time,'dd_mm_yyyy_HH_MM');

filename = 'NasaTLX/Nasatlxdata.xlsx';
sheet = 2;
xlRange = 'A2:R31';

feature_data = xlsread(filename,sheet,xlRange);

   %% Compute kurkalwallis Test all subject, each quesiton type (easy, medium, hard)
   % and each feature

    filename = strcat('Result/NasaTLX_feature_', dtstr, '.xls');
    field_name = {'MentalDemand';'Physical Demand'; 'Temporal Demand'; 'Performance';
                    'Effort'; 'Frustration'};
                
      % Add the jar files related to XLWrite to java path.
    javaaddpath('jxl.jar');
    javaaddpath('MXL.jar');

    import mymxl.*;
    import jxl.*; 
    tmp_index = 1;
    index =1;
    
    d.data{index, 1} = char('Feature');
    d.data{index, 2} = char('EasyMean');
    d.data{index, 3} = char('MediumMean');
    d.data{index, 4} = char('HardMean');
    d.data{index, 5} = char('EasyMeanRank');
    d.data{index, 6} = char('MediumMeanRank');
    d.data{index, 7} = char('HardMeanRank');
    d.data{index, 8} = char('PValue');
    d.data{index, 9} = char('df');
    d.data{index, 10} = char('chi_sq');
    d.data{index, 11} = char('N1');
    d.data{index, 12} = char('N2');
    d.data{index, 13} = char('N3');
    
    index = index+1;
    
    final_easy =[];
    final_medium = [];
    final_hard= [];
    
for c=1:3:size(feature_data,2)
    allData = [feature_data(:,c); feature_data(:,c+1);  feature_data(:,c+2)]; 
    groups = [ones(size(feature_data(:,1))); 2 * ones(size(feature_data(:,c+1))); 3 * ones(size(feature_data(:,c+2)))];

    [P,ANOVATAB,STATS] = kruskalwallis(allData, groups , 'off');
    
    d.data{index, 2}= mean(feature_data(:,c));
    d.data{index, 3}= mean(feature_data(:,c+1));
    d.data{index, 4}= mean(feature_data(:,c+2));
    

    if(size(STATS.meanranks(1,:), 2) >=1)
        d.data{index, 5}= STATS.meanranks(1,1);
    else
        d.data{index, 5}= 0;
    end

    if(size(STATS.meanranks(1,:), 2) >=2)
        d.data{index, 6}= STATS.meanranks(1,2);
    else
        d.data{index, 6} = 0;
    end

    if(size(STATS.meanranks(1,:), 2) >=3)
        d.data{index, 7}= STATS.meanranks(1,3);
    else
        d.data{index, 7} = 0;
    end
    
    tmp_index = floor((c+2)/3);
    
    d.data{index, 1} = field_name{tmp_index};
    d.data{index, 8}= P;
    
    d.data{index, 9} = ANOVATAB{2,3};
    d.data{index, 10} = ANOVATAB{2,5};
    d.data{index, 11} = STATS.n(1,1);
    d.data{index, 12} = STATS.n(1,2);
    d.data{index, 13} = STATS.n(1,3); 
    
    index = index+1;
    
%     figure;
%     boxplot([feature_data(:,c), feature_data(:,c+1) , feature_data(:,c+2)],'Notch','on','Labels',{'Easy','Medium', 'Difficult'});
%     title(strcat(field_name{tmp_index}, ' - Compare different Task Difficulty Level'));
%     xlabel('Task Difficulty Level');
%     ylabel('Rating * Weight');
    
end
   
    xlwrite(filename,d.data); 
disp('All subjects processed completely !!!');

d.data
%% Box Plot
% figure;
% boxplot([final_easy', final_medium' , final_hard'],'Notch','on','Labels',{'Easy','Medium', 'Difficult'});
% title('NASA TLX Overall Analysis - Compare different Task Difficulty Level');
% xlabel('Task Difficulty Level');
% ylabel('Mean Value');