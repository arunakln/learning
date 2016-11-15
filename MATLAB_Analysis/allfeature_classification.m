clc
clear all
close all

%-------------------------------------------------------------------------%
% Author: Aruna Duraisingam
% Date : 23-Aug-2016
% This script exptracts all the energy and ERD features from different
% frequency band.
%-------------------------------------------------------------------------%

%% Initialize

no_of_questions = 9;
no_of_channels = 14;
Fs = 128; % Sampling Rate - Emotive Device

subject_files = {'1.edf';'3.edf'; '4.edf';'5.edf'; 
                '6.edf'; '8.edf';'9.edf'; '10.edf'};

age = {25;23;24;28;26;26;27;25};

% Excel file.
now_time = datetime('now');
dtstr = datestr(now_time,'dd_mm_yyyy_HH_MM');

% Looping subjects
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

        % Stop Band 1 HZ both sides and adjusting for zero frequency cutoff;
        if(freq_bands(n,1) <= 1)
            Ws = [0.5, freq_bands(n,2)+1]/64; 
        else
            Ws = [freq_bands(n,1)-1, freq_bands(n,2)+1]/64; % normalize Fs/2;
        end

        Rp = 1; % ripples
        Rs = 30; % attenuation

        [N, Wn] = ellipord(Wp, Ws, Rp, Rs);
        [b, a] = ellip(N, Rp, Rs, Wp);

         fd_b{n,:} = b;
         fd_a{n,:} = a;     
    end
    
    %% Baseline calcuation
    
   
    baseline_raw_data= data(3:16, baseline_marker_array(1): start_marker_array(1));

    for ch=1:no_of_channels
        baseline_data{s,ch,:} = baseline_raw_data(ch,:) - mean(baseline_raw_data);                  
    end
    
    for f=1:size(freq_bands, 1)  
        for ch=1:no_of_channels
            % segment data into 1 secs
            totallen = size(baseline_data{s,ch,:},2);        
            cnt = floor(totallen/Fs);
            bl_data = reshape( baseline_data{s,ch,:}(1,1:cnt*Fs),[],Fs);
            tmp_cnt =1;

            % calculate energy for each time segment
            for t = 1:size(bl_data,1)
                % remove eye blink noise
                bl_eye_blink_filter = abs(filtfilt(fd_b{5,:},fd_a{5,:},bl_data(t,:)'));
                bl_filter_mean = mean(bl_eye_blink_filter);
                    
                if( max(bl_eye_blink_filter) < (100 * bl_filter_mean))
                    b_EXF(s,f,ch,tmp_cnt,:) = var(filtfilt(fd_b{f,:},fd_a{f,:},bl_data(t,:)'));
                    tmp_cnt = tmp_cnt+1;
                end
            end
            clearvars tmp_cnt;
            baseline_mean_EXF(s,f,ch,:) = mean(b_EXF(s,f,ch,:));
        end 
    end

    %% Energy calculation for each feature, each question , each channel and each 1 sec time segment

    for q = 1:no_of_questions        
        % individual question data based on marker
        quest_raw_data= data(3:16, start_marker_array(q): end_marker_array(q));

        for ch=1:no_of_channels
             quest_data{q,ch,:} = quest_raw_data(ch,:) - mean(quest_raw_data);
        end
    end

    
    for f=1:size(freq_bands, 1) 
        for ch=1:no_of_channels               
                easy_energy = [];
                easy_erd = [];
                medium_erd=[];
                medium_energy=[];
                difficult_erd=[];
                difficult_energy = [];
                e_cnt =1;
                m_cnt=1;
                d_cnt=1;
                
            for q=1:no_of_questions
            
                % segment data into 1 secs      
                q_cnt = floor(size(quest_data{q,ch,:},2)/Fs);                
                qd_data = reshape(quest_data{q,ch,:}(1,1:q_cnt*Fs),[],Fs);
                % calculate energy for each time segment

                
                tmp_cnt = 1;
                
                for t = 1:size(qd_data,1)
                    % Apply Filter and calculate energy for each question 
                    % and each channel check for eye blink noise and remove it.
                    eye_blink_filter = abs(filtfilt(fd_b{5,:},fd_a{5,:},qd_data(t,:)'));
                    filter_mean = mean(eye_blink_filter);
                    
                    if( max(eye_blink_filter) < (100 * filter_mean))
                        EXF(s,f,ch,q,tmp_cnt) = var(filtfilt(fd_b{f,:},fd_a{f,:},qd_data(t,:)'));
                        
                        % ERD Calculation
                        ERD_EXF(s,f,ch,q,tmp_cnt) = ((baseline_mean_EXF(s,f,ch,:)-EXF(s,f,ch,q,tmp_cnt))*100)/baseline_mean_EXF(s,f,ch,:);

                        if(q==1 || q == 4 || q== 7)
                            easy_erd(end+1) = ERD_EXF(s,f,ch,q,tmp_cnt);
                            easy_energy(end+1) = EXF(s,f,ch,q,tmp_cnt);
                            e_cnt=e_cnt+1;
                        end

                        if(q==2 || q == 5 || q== 8)
                            medium_erd(end+1) = ERD_EXF(s,f,ch,q,tmp_cnt);
                            medium_energy(end+1) = EXF(s,f,ch,q,tmp_cnt);
                            m_cnt = m_cnt+1;
                        end

                        if(q==3 || q == 6 || q== 9)
                            difficult_erd(end+1) = ERD_EXF(s,f,ch,q,tmp_cnt);
                            difficult_energy(end+1) = EXF(s,f,ch,q,tmp_cnt);
                            d_cnt = d_cnt+1;
                        end 
                    else
                        segment_removed_cnt = segment_removed_cnt+1;
                    end       
                    tmpcnt =tmp_cnt + 1;
                end
   

                clearvars tmp_cnt ;
            end
                q_easy_ene{s,f,ch} = easy_energy;
                q_easy_erd{s,f,ch} = easy_erd;
                q_medium_ene{s,f,ch} = medium_energy;
                q_medium_erd{s,f,ch} = medium_erd;
                q_diff_ene{s,f,ch} = difficult_energy;
                q_diff_erd{s,f,ch} = difficult_erd;

            clearvars q_cnt easy_energy easy_erd medium_energy medium_erd difficult_energy difficult_erd e_cnt m_cnt d_cnt;
        end
%         easyenergy{s,f} = easy_energy;
%         easyerd{s,f} = easy_erd;
%         mediumenergy{s,f} = medium_energy;
%         mediumerd{s,f} = medium_erd;
%         diffenergy{s,f} = difficult_energy;
%         differd{s,f} = difficult_erd;
        
        clearvars e_cnt m_cnt d_cnt;
    end    
    %% Clear all temporary variables
    
    segment_removed_cnt
    clearvars baseline_raw_data quest_raw_data qd_data segment_removed_cnt ;
    
    
    disp(strcat('SubjectID: ', num2str(s),' - Sucessfully Completed'));
end


    %% kurkalwallis Test each subject, each feature     

    
%    filename = strcat('Result/ERD_Features_', dtstr, '.xls');
%       % Add the jar files related to XLWrite to java path.
%     javaaddpath('jxl.jar');
%     javaaddpath('MXL.jar');
% 
%     import mymxl.*;
%     import jxl.*; 
%     index = 1;
%     
%         %Compute variable alpha and theta frequency bands based on IAF
%     % Lower1-Alpha = (IAF-4Hz) to (IAF-2Hz)
%     % Lower2-Alpha = (IAF-2Hz) to (IAF)
%     % Upper-Alpha  = IAF to (IAF+2Hz)
%     % Theta        = (IAF-6Hz) to (IAF-4Hz)
% 
%     %Additional Features (Frtiz et al.)
%     % alpha - 8-12 Hz, Beta - 12 - 30Hz
%     % Gamma - 30-50Hz (Beacuse of emotive sampling rate is 128, have changed the
%     % maximum freq from 80 to 50 Hz
%     % Delta - 1-4 Hz (Becasue miniumum cuttoff should be more than 0, the minimum of 
%     % delta frequency changed from 0 to 1 Hz
%     % Theta - 4-8 Hz
%     % alpha+Theta - 4-12 Hz
%     % alpha+beta - 8-30Hz
%     % All frequecies combined - 1-50Hz
%     
%     d.data{index, 1} = char('SubjectID');
%     d.data{index, 2} = char('time');
%     d.data{index, 3} = char('Energy_lower1Alpha');
%     d.data{index, 4} = char('Energy_lower2Alpha');
%     d.data{index, 5} = char('Energy_upperAlpha');
%     d.data{index, 6} = char('Energy_theta');
%     d.data{index, 7} = char('Energy_delta');
%     d.data{index, 8} = char('Energy_alpha');
%     d.data{index, 9} = char('Energy_beta');
%     d.data{index, 10} = char('Energy_theta');
%     d.data{index, 11} = char('Energy_gamma');
%     d.data{index, 12} = char('Energy_alphatheta');
%     d.data{index, 13} = char('Energy_alphabeta');
%     d.data{index, 14} = char('Energy_total');   
%     
%     d.data{index, 15} = char('ERD_lower1Alpha');
%     d.data{index, 16} = char('ERD_lower2Alpha');
%     d.data{index, 17} = char('ERD_upperAlpha');
%     d.data{index, 18} = char('ERD_theta');
%     d.data{index, 19} = char('ERD_delta');
%     d.data{index, 20} = char('ERD_alpha');
%     d.data{index, 21} = char('ERD_beta');
%     d.data{index, 22} = char('ERD_theta');
%     d.data{index, 23} = char('ERD_gamma');
%     d.data{index, 24} = char('ERD_alphatheta');
%     d.data{index, 25} = char('ERD_alphabeta');
%     d.data{index, 26} = char('ERD_total');
%     
%     index = index+1;
%     channelLabel = {'AF3';'F7';'F3';'FC5';'T7';'P7';
%                         'O1';'O2';'P8';'T8';'FC6';'F4';'F8';'AF4'};
%     featureLabel ={'LowerAlpha1';'LowerAlpha2';'UpperAlpha';'IAFTheta';'Alpha';
%                 'Beta';'Gamma';'Delta';'Theta';'AlphaTheta';'AlphaBeta';'All'};
% 
%     for s= 1: size(subject_files, 1)         
%         for f=1:size(freq_bands, 1) 
%             for ch=1:no_of_channels 
%                 for ch=1:no_of_questions
%                     for t = 1:
%                     d.data{index, 1} = s;
%                     d.data{index, 2} = ;
%                     d.data{index, 3} = ch;
%                     d.data{index, 4} = ;
%                     d.data{index, 5} = ;
%                     d.data{index, 6} = ;
%                     d.data{index, 7} = ;
%                     d.data{index, 8} = ;
%                     d.data{index, 9} = ;
%                     d.data{index, 10} = ;
%                     d.data{index, 11} = ;
%                     d.data{index, 12} = ;
%                     d.data{index, 13} = ;
%                     d.data{index, 14} = ;
%                 end
%             end
%         end
%     end 
%     
%     %save data to excel
%     xlwrite(filename,d.data);
%     disp('All subjects processed completely !!!');