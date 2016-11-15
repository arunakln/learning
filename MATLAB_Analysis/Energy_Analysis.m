clc
clear all
close all

%-------------------------------------------------------------------------%
% Author: Aruna Duraisingam
% Date : 01-Aug-2016
% This script reads the subject's EEG data and compute energy across each subject,
% each feature and each channel and each question type 
% and perform kurkal wallis test to compare different task difficulty level
% (easy, medium and difficult)
%-------------------------------------------------------------------------%

%% intialize values

no_of_questions = 9;
no_of_channels = 14;
Fs = 128; % Sampling Rate - Emotive Device

subject_files = {'1.edf';'3.edf'; '4.edf';'5.edf'; 
                '6.edf'; '8.edf';'9.edf'; '10.edf'};

age = {25;23;24;28;26;26;27;25};

now_time = datetime('now');
dtstr = datestr(now_time,'dd_mm_yyyy_HH_MM');

% Looping over subjects
for s= 1: size(subject_files, 1)
    % Read Data
    [hdr, data] = edfread(strcat('edf/', subject_files{s}));

    % Extract the start and end of marker per question 
    %and also the baseline marker.
    
    start_marker_array = find(data(36,:) == 49);
    end_marker_array = find(data(36,:) == 50);
    baseline_marker_array = find(data(36,:) == 51);
    segment_removed_cnt =1;

    %Computer Individual Peak Alpha Frequency - IAF
    % Peak Alpha Fequency  = 11.95-0.053*Age

    iaf = 11.95-0.053*age{s};

    %Compute variable alpha and theta frequency bands based on IAF
    % Lower1-Alpha = (IAF-4Hz) to (IAF-2Hz)
    % Lower2-Alpha = (IAF-2Hz) to (IAF)
    % Upper-Alpha  = IAF to (IAF+2Hz)
    % Theta        = (IAF-6Hz) to (IAF-4Hz)

    %Additional Features (Frtiz et al.)
    % alpha - 8-12 Hz, Beta - 12 - 30Hz
    % Gamma - 30-50Hz (Beacuse of emotive sampling rate is 128, have changed the
    % maximum freq from 80 to 50 Hz
    % Delta - 1-4 Hz (Becasue miniumum cuttoff should be more than 0, the minimum of 
    % delta frequency changed from 0 to 1 Hz
    % Theta - 4-8 Hz
    % alpha+Theta - 4-12 Hz
    % alpha+beta - 8-30Hz
    % All frequecies combined - 1-50Hz

    freq_bands = [iaf-6, iaf-4; iaf-4, iaf-2; iaf-2, iaf; iaf, iaf+2;       
                    1,4; 8, 12 ; 12, 30; 4, 8; 30, 50; 4, 12; 8, 30; 1, 50];
    % sort  the features           
%      freq_bands = sort(freq_bands,1);
    
    
        %% Filter Design
    for n= 1:size(freq_bands, 1)
        % Compute Order Of Filter
        
        % Band Pass Filter Computation
        Wp = [freq_bands(n,1), freq_bands(n,2)]/64; % normalize Fs/2;

        if(freq_bands(n,1) <= 1)
            % Stop Band 1 HZ both sides and adjusting for zero frequency cutoff;
            Ws = [0.5, freq_bands(n,2)+1]/64; 
        else
            % normalize Fs/2;
            Ws = [freq_bands(n,1)-1, freq_bands(n,2)+1]/64; 
        end

        Rp = 1; % ripples
        Rs = 30; % attenuation

        [N, Wn] = ellipord(Wp, Ws, Rp, Rs);
        [b, a] = ellip(N, Rp, Rs, Wp);

         fd_b{n,:} = b;
         fd_a{n,:} = a;     
    end
    
    %% Compute Vector Auto Regressive Ananlysis foreach feature, each question 
    %,each channel and each 1 sec time segment

    for q = 1:no_of_questions        
        % individual question data based on marker
        quest_raw_data= data(3:16, start_marker_array(q): end_marker_array(q));

        for ch=1:no_of_channels
             quest_data{q,ch,:} = quest_raw_data(ch,:) - mean(quest_raw_data);
        end
    end
    
    % loop over each feature, each channels, each question and each time
    % segment of 1 second and compute Vector Autoregression
    
    for f=1:size(freq_bands, 1) 
        for ch=1:no_of_channels
                easy = [];
                medium = [];
                difficult = [];
            for q=1:no_of_questions
            
                % segment data into 1 secs      
                q_cnt = floor(size(quest_data{q,ch,:},2)/Fs);                
                qd_data = reshape(quest_data{q,ch,:}(1,1:q_cnt*Fs),[],Fs);
                tmp_cnt = 1;              
                
                for t = 1:size(qd_data,1)
                    % Apply Filter and calculate energy for each question 
                    % and each channel check for eye blink noise and remove it.
                    eye_blink_filter = abs(filtfilt(fd_b{5,:},fd_a{5,:},qd_data(t,:)'));
                    filter_mean = mean(eye_blink_filter);
                    
                    if( max(eye_blink_filter) < (100 * filter_mean))
                        %XF{s,f,ch,q,tmp_cnt,:} = filtfilt(fd_b{f,:},fd_a{f,:},qd_data(t,:)');
                         EXF(s,f,ch,q,t,:) = var(filtfilt(fd_b{f,:},fd_a{f,:},qd_data(t,:)'));  
                         
                        if(q==1 || q == 4 || q== 7)
                            easy(end+1) = EXF(s,f,ch,q,tmp_cnt,:);
                        end

                        if(q==2 || q == 5 || q== 8)
                            medium(end+1) = EXF(s,f,ch,q,tmp_cnt,:);
                        end

                        if(q==3 || q == 6 || q== 9)
                            difficult(end+1) = EXF(s,f,ch,q,tmp_cnt,:);
                        end

                    else
                        segment_removed_cnt = segment_removed_cnt+1;
                    end                    

                end  
                clearvars tmp_cnt;
            end    
                % Consolidate the question categories
                subject_easy_feature{s,f,ch,:} = easy;
                subject_medium_feature{s,f,ch,:} = medium;
                subject_hard_feature{s,f,ch,:} = difficult;
                
                clearvars easy medium difficult;
        end
    end    
    %% Clear all temporary variables
    segment_removed_cnt
    clearvars baseline_raw_data quest_raw_data qd_data;
    
    disp(strcat('SubjectID: ', num2str(s),' - Sucessfully Completed'));
end

    %% kurkalwallis Test each subject, each feature     

   filename = strcat('Result/Features_', dtstr, '.xls');
   
   % Add the jar files related to XLWrite to java path.
    javaaddpath('jxl.jar');
    javaaddpath('MXL.jar');

    import mymxl.*;
    import jxl.*; 
    index = 1;
    
    d.data{index, 1} = char('SubjectID');
    d.data{index, 2} = char('FeatureID');
    d.data{index, 3} = char('ChannelID');
    d.data{index, 4} = char('MeanEasy');
    d.data{index, 5} = char('MeanMedium');
    d.data{index, 6} = char('MeanDifficult');
    d.data{index, 7} = char('EasyMeanRank');
    d.data{index, 8} = char('MediumMeanRank');
    d.data{index, 9} = char('HardMeanRank');
    d.data{index, 10} = char('PValue');
    d.data{index, 11} = char('PStatus');
    d.data{index, 12} = char('FeatureName');
    d.data{index, 13} = char('ChannelLabel');
    d.data{index, 14} = char('FreqFrom');
    d.data{index, 15} = char('FreqTo');
    
    channelLabel = {'AF3';'F7';'F3';'FC5';'T7';'P7';
                        'O1';'O2';'P8';'T8';'FC6';'F4';'F8';'AF4'};
    featureLabel ={'LowerAlpha1';'LowerAlpha2';'UpperAlpha';'IAFTheta';'Alpha';
                'Beta';'Gamma';'Delta';'Theta';'AlphaTheta';'AlphaBeta';'All'};
    
    index = index+1;
    
    for s= 1: size(subject_files, 1)          
        for f=1:size(freq_bands, 1) 
            for ch=1:no_of_channels
                    allData = [subject_easy_feature{s,f,ch}'; subject_medium_feature{s,f,ch}';  subject_hard_feature{s,f,ch}']; 
                    groups = [ones(size(subject_easy_feature{s,f,ch}')); 2 * ones(size(subject_medium_feature{s,f,ch}')); 3 * ones(size(subject_hard_feature{s,f,ch}'))];

                    [P,ANOVATAB,STATS] = kruskalwallis(allData, groups, 'off');
                    
                    d.data{index, 1} = s;
                    d.data{index, 2} = f;
                    d.data{index, 3} = ch;
                    d.data{index, 4} = mean(subject_easy_feature{s,f,ch});
                    d.data{index, 5} = mean(subject_medium_feature{s,f,ch});
                    d.data{index, 6} = mean(subject_hard_feature{s,f,ch});
                    
                    if(size(STATS.meanranks(1,:), 2) >=1)
                        d.data{index, 7}= STATS.meanranks(1,1);
                    else
                        d.data{index, 7} = 0;
                    end
                    
                    if(size(STATS.meanranks(1,:), 2) >=2)
                        d.data{index, 8}= STATS.meanranks(1,2);
                    else
                        d.data{index, 8} = 0;
                    end
                    
                    if(size(STATS.meanranks(1,:), 2) >=3)
                        d.data{index, 9}= STATS.meanranks(1,3);
                    else
                        d.data{index, 9} = 0;
                    end

                    d.data{index, 10}= P;
                    if(P <=0.05)
                        d.data{index, 11}= char('Yes');
                    else
                        d.data{index, 11}= char('No');
                    end
                    
                    d.data{index, 12} = featureLabel{f};
                    d.data{index, 13} = channelLabel{ch};
                    d.data{index, 14} = freq_bands(f,1);
                    d.data{index, 15} = freq_bands(f,2);
                    
                    index =index+1;
            end
        end
    end  
    
    xlwrite(filename,d.data);
    disp('All subjects processed completely !!!');