clc
clear all
close all

%-------------------------------------------------------------------------%
% Author: Aruna Duraisingam
% Date : 23-Aug-2016
% This script reads the EEG data and perform Frequency ratio between
% different frequency bands
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
for s= 8:8% size(subject_files, 1)
    % Read Data
    [hdr, data] = edfread(strcat('edf/', subject_files{s}));

    % Extract the start and end of marker per question 
    %and also the baseline marker.
    
    start_marker_array = find(data(36,:) == 49);
    end_marker_array = find(data(36,:) == 50);
    baseline_marker_array = find(data(36,:) == 51);
    segment_removed_cnt =1;

    %Additional Features (Frtiz et al.)
    % alpha - 8-12 Hz, Beta - 12 - 30Hz
    % Gamma - 30-50Hz (Beacuse of emotive sampling rate is 128, have changed the
    % maximum freq from 80 to 50 Hz
    % Delta - 1-4 Hz (Becasue miniumum cuttoff should be more than 0, the minimum of 
    % delta frequency changed from 0 to 1 Hz
    % Theta - 4-8 Hz
    % alpha+Theta - 4-12 Hz
    % alpha+beta - 8-30Hz


    freq_bands = [1,4; 8, 12 ; 12, 30; 4, 8; 30, 50; 4, 12; 8, 30];
%     freq_comb = [2,3];
    freq_comb = [2,3; 2,5; 2, 1; 2,4; 3,2;3,5;3,4;3,1;5,1;5,2;5,3;5,4;
    1,2;1,3;1,4;1,5;4,1; 4,2 ;4,3;4,5; 4, 6; 3, 7];
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
    
   
    baseline_raw_data= data([3,16], baseline_marker_array(1): start_marker_array(1));
    
    avg_baseline_raw_data{s} = mean(baseline_raw_data,1);

    baseline_data{s} = avg_baseline_raw_data{s} - mean(avg_baseline_raw_data{s},2);  
    
    for f=1:size(freq_bands, 1)  
            % segment data into 1 secs
            totallen = size(baseline_data{s},2);        
            cnt = floor(totallen/Fs);
            bl_data = reshape( baseline_data{s}(1,1:cnt*Fs),[],Fs);
            tmp_cnt =1;
            rmv_cnt =1;
  
            % calculate energy for each time segment
            for t = 1:size(bl_data,1)
                % remove eye blink noise
                bl_eye_blink_filter = abs(filtfilt(fd_b{1,:},fd_a{1,:},bl_data(t,:)'));
                bl_filter_mean = mean(bl_eye_blink_filter);
                    
                if( max(bl_eye_blink_filter) < (50 * bl_filter_mean))
                    b_EXF(s,f,tmp_cnt) = var(filtfilt(fd_b{f,:},fd_a{f,:},bl_data(t,:)'));
                    tmp_cnt = tmp_cnt+1;
                else
                    bl_eyeblink{s, rmv_cnt} = bl_data(t,:);
                    rmv_cnt = rmv_cnt+1;
                end
            end
            clearvars tmp_cnt rmv_cnt;
            baseline_mean_EXF(s,f,:) = mean(b_EXF(s,f,:));
    end
    
    for fc = 1: size(freq_comb,1)
        baseline_fc_EXF(s,fc,:) = (baseline_mean_EXF(s,freq_comb(fc,1),:)/baseline_mean_EXF(s,freq_comb(fc,2),:));
    end

    %% Energy calculation for each feature, each question , each channel and each 1 sec time segment

    for q = 1:no_of_questions        
        % individual question data based on marker
        quest_raw_data= data([3,16], start_marker_array(q): end_marker_array(q));
        
        avg_quest_raw_data{q} = mean(quest_raw_data,1);
        quest_data{q,:} = avg_quest_raw_data{q} - mean(avg_quest_raw_data{q} , 2);        
    end
    

    
    e_alphabeta= [];    e_alphagamma = [];    e_alphadelta = [];    e_alphatheta = [];
    e_betaalpha = [];    e_betagamma = [];    e_betatheta = [];    e_betadelta = [];
    e_gammadelta = [];    e_gammaalpha = [];    e_gammabeta =[];    e_gammatheta =[];
    e_deltaalpha =[];    e_deltabeta =[];    e_deltatheta =[];    e_deltagamma =[];
    e_thetadelta =[];    e_thetaalpha =[];    e_thetabeta =[];    e_thetagamma =[];
    e_theta_alphatheta =[];    e_beta_alphabeta=[];
    
    m_alphabeta= [];    m_alphagamma = [];    m_alphadelta = [];    m_alphatheta = [];
    m_betaalpha = [];    m_betagamma = [];    m_betatheta = [];    m_betadelta = [];
    m_gammadelta = [];    m_gammaalpha = [];    m_gammabeta =[];    m_gammatheta =[];
    m_deltaalpha =[];    m_deltabeta =[];    m_deltatheta =[];    m_deltagamma =[];
    m_thetadelta =[];    m_thetaalpha =[];    m_thetabeta =[];    m_thetagamma =[];
    m_theta_alphatheta =[];    m_beta_alphabeta=[];
    
    d_alphabeta= [];    d_alphagamma = [];    d_alphadelta = [];   d_alphatheta = [];
    d_betaalpha = [];    d_betagamma = [];    d_betatheta = [];    d_betadelta = [];
    d_gammadelta = [];    d_gammaalpha = [];    d_gammabeta =[];    d_gammatheta =[];
   d_deltaalpha =[];    d_deltabeta =[];    d_deltatheta =[];    d_deltagamma =[];
    d_thetadelta =[];    d_thetaalpha =[];    d_thetabeta =[];    d_thetagamma =[];
    d_theta_alphatheta =[];    d_beta_alphabeta=[];
    
   for q=1:no_of_questions      

            % segment data into 1 secs      
            q_cnt = floor(size(quest_data{q,:},2)/Fs);                
            qd_data = reshape(quest_data{q,:}(1,1:q_cnt*Fs),[],Fs);
            % calculate energy for each time segment

            tmp_cnt = 1;
            rmv_cnt =1;

            e_cnt =1;
            m_cnt = 1;
            d_cnt =1;
            

            for t = 1:size(qd_data,1)



                % Apply Filter and calculate energy for each question 
                % and each channel check for eye blink noise and remove it.
                eye_blink_filter = abs(filtfilt(fd_b{1,:},fd_a{1,:},qd_data(t,:)'));
                filter_mean = mean(eye_blink_filter);

                if( max(eye_blink_filter) < (50 * filter_mean))
                
                    for f=1:size(freq_bands, 1)                    
                        EXF(s,q,f,tmp_cnt,:) = var(filtfilt(fd_b{f,:},fd_a{f,:},qd_data(t,:)'));                    
                    end  
                    
                    alphabeta = baseline_fc_EXF(s,1,:) - (EXF(s,q,freq_comb(1,1),tmp_cnt)/EXF(s,q,freq_comb(1,2),tmp_cnt));
                    alphagamma = baseline_fc_EXF(s,2,:) - (EXF(s,q,freq_comb(2,1),tmp_cnt)/EXF(s,q,freq_comb(2,2),tmp_cnt));
                    alphadelta = baseline_fc_EXF(s,3,:) - (EXF(s,q,freq_comb(3,1),tmp_cnt)/EXF(s,q,freq_comb(3,2),tmp_cnt));
                    alphatheta = baseline_fc_EXF(s,4,:) - (EXF(s,q,freq_comb(4,1),tmp_cnt)/EXF(s,q,freq_comb(4,2),tmp_cnt));

                    betaalpha = baseline_fc_EXF(s,5,:) - (EXF(s,q,freq_comb(5,1),tmp_cnt)/EXF(s,q,freq_comb(5,2),tmp_cnt));
                    betagamma = baseline_fc_EXF(s,6,:) - (EXF(s,q,freq_comb(6,1),tmp_cnt)/EXF(s,q,freq_comb(6,2),tmp_cnt));
                    betatheta = baseline_fc_EXF(s,7,:) - (EXF(s,q,freq_comb(7,1),tmp_cnt)/EXF(s,q,freq_comb(7,2),tmp_cnt));
                    betadelta = baseline_fc_EXF(s,8,:) - (EXF(s,q,freq_comb(8,1),tmp_cnt)/EXF(s,q,freq_comb(8,2),tmp_cnt));

                    gammadelta = baseline_fc_EXF(s,9,:) - (EXF(s,q,freq_comb(9,1),tmp_cnt)/EXF(s,q,freq_comb(9,2),tmp_cnt));
                    gammaalpha = baseline_fc_EXF(s,10,:) - (EXF(s,q,freq_comb(10,1),tmp_cnt)/EXF(s,q,freq_comb(10,2),tmp_cnt));
                    gammabeta = baseline_fc_EXF(s,11,:) - (EXF(s,q,freq_comb(11,1),tmp_cnt)/EXF(s,q,freq_comb(11,2),tmp_cnt));
                    gammatheta = baseline_fc_EXF(s,12,:) - (EXF(s,q,freq_comb(12,1),tmp_cnt)/EXF(s,q,freq_comb(12,2),tmp_cnt));

                    deltaalpha = baseline_fc_EXF(s,13,:) - (EXF(s,q,freq_comb(13,1),tmp_cnt)/EXF(s,q,freq_comb(13,2),tmp_cnt));
                    deltabeta = baseline_fc_EXF(s,14,:) - (EXF(s,q,freq_comb(14,1),tmp_cnt)/EXF(s,q,freq_comb(14,2),tmp_cnt));
                    deltatheta = baseline_fc_EXF(s,15,:) - (EXF(s,q,freq_comb(15,1),tmp_cnt)/EXF(s,q,freq_comb(15,2),tmp_cnt));
                    deltagamma= baseline_fc_EXF(s,16,:) - (EXF(s,q,freq_comb(16,1),tmp_cnt)/EXF(s,q,freq_comb(16,2),tmp_cnt));

                    thetadelta = baseline_fc_EXF(s,17,:) - (EXF(s,q,freq_comb(17,1),tmp_cnt)/EXF(s,q,freq_comb(17,2),tmp_cnt));
                    thetaalpha = baseline_fc_EXF(s,18,:) - (EXF(s,q,freq_comb(18,1),tmp_cnt)/EXF(s,q,freq_comb(18,2),tmp_cnt));
                    thetabeta= baseline_fc_EXF(s,19,:) - (EXF(s,q,freq_comb(19,1),tmp_cnt)/EXF(s,q,freq_comb(19,2),tmp_cnt));
                    thetagamma = baseline_fc_EXF(s,20,:) - (EXF(s,q,freq_comb(20,1),tmp_cnt)/EXF(s,q,freq_comb(20,2),tmp_cnt));

                    theta_alphatheta = baseline_fc_EXF(s,21,:) - (EXF(s,q,freq_comb(21,1),tmp_cnt)/EXF(s,q,freq_comb(21,2),tmp_cnt));
                    beta_alphabeta = baseline_fc_EXF(s,22,:) - (EXF(s,q,freq_comb(22,1),tmp_cnt)/EXF(s,q,freq_comb(22,2),tmp_cnt));
                    
                    if(q==1 || q == 4 || q== 7)
                        e_alphabeta(end+1) = alphabeta;
                        e_alphagamma(end+1) = alphagamma;
                        e_alphadelta(end+1) = alphadelta;
                        e_alphatheta(end+1) =alphatheta;

                        e_betaalpha(end+1) = betaalpha;
                        e_betagamma(end+1) =betagamma;
                        e_betatheta(end+1) = betatheta;
                        e_betadelta(end+1) = betadelta;

                        e_gammadelta(end+1) =gammadelta;
                        e_gammaalpha(end+1) =gammaalpha;
                        e_gammabeta(end+1) = gammabeta;
                        e_gammatheta(end+1) =gammatheta;

                        e_deltaalpha(end+1) = deltaalpha;
                        e_deltabeta(end+1) =deltabeta;
                        e_deltatheta(end+1) = deltatheta;
                        e_deltagamma(end+1) = deltagamma;

                        e_thetadelta(end+1) =thetadelta;
                        e_thetaalpha(end+1) = thetaalpha;
                        e_thetabeta(end+1) = thetabeta;
                        e_thetagamma(end+1) = thetagamma;

                        e_theta_alphatheta(end+1) =theta_alphatheta;
                        e_beta_alphabeta(end+1) = beta_alphabeta;
                    end
                    
                   if(q==2 || q == 5 || q== 8)
                        m_alphabeta(end+1) = alphabeta;
                        m_alphagamma(end+1) = alphagamma;
                        m_alphadelta(end+1) = alphadelta;
                        m_alphatheta(end+1) =alphatheta;

                        m_betaalpha(end+1) = betaalpha;
                        m_betagamma(end+1) =betagamma;
                        m_betatheta(end+1) = betatheta;
                        m_betadelta(end+1) = betadelta;

                        m_gammadelta(end+1) =gammadelta;
                        m_gammaalpha(end+1) =gammaalpha;
                        m_gammabeta(end+1) = gammabeta;
                        m_gammatheta(end+1) =gammatheta;

                        m_deltaalpha(end+1) = deltaalpha;
                        m_deltabeta(end+1) =deltabeta;
                        m_deltatheta(end+1) = deltatheta;
                        m_deltagamma(end+1) = deltagamma;

                        m_thetadelta(end+1) =thetadelta;
                        m_thetaalpha(end+1) = thetaalpha;
                        m_thetabeta(end+1) = thetabeta;
                        m_thetagamma(end+1) = thetagamma;

                        m_theta_alphatheta(end+1) =theta_alphatheta;
                        m_beta_alphabeta(end+1) = beta_alphabeta;
                    end
                    
                    if(q==3 || q == 6 || q== 9)
                        d_alphabeta(end+1) = alphabeta;
                        d_alphagamma(end+1) = alphagamma;
                        d_alphadelta(end+1) = alphadelta;
                        d_alphatheta(end+1) =alphatheta;

                        d_betaalpha(end+1) = betaalpha;
                        d_betagamma(end+1) =betagamma;
                        d_betatheta(end+1) = betatheta;
                        d_betadelta(end+1) = betadelta;

                        d_gammadelta(end+1) =gammadelta;
                        d_gammaalpha(end+1) =gammaalpha;
                        d_gammabeta(end+1) = gammabeta;
                        d_gammatheta(end+1) =gammatheta;

                        d_deltaalpha(end+1) = deltaalpha;
                        d_deltabeta(end+1) =deltabeta;
                        d_deltatheta(end+1) = deltatheta;
                        d_deltagamma(end+1) = deltagamma;

                        d_thetadelta(end+1) =thetadelta;
                        d_thetaalpha(end+1) = thetaalpha;
                        d_thetabeta(end+1) = thetabeta;
                        d_thetagamma(end+1) = thetagamma;

                        d_theta_alphatheta(end+1) =theta_alphatheta;
                        d_beta_alphabeta(end+1) = beta_alphabeta;
                    end
             
                     tmp_cnt= tmp_cnt+1;  

                else
                    eyeblink{s, rmv_cnt} = qd_data(t,:);
                    rmv_cnt =rmv_cnt+1;
                end
            end   

            clearvars tmp_cnt e_cnt m_cnt d_cnt;
        end
     
    %% Clear all temporary variables   


    clearvars baseline_raw_data avg_baseline_raw_data quest_raw_data qd_data rmv_cnt e_cnt m_cnt d_cnt;  
    
    disp(strcat('SubjectID: ', num2str(s),' - Sucessfully Completed'));
end
easy = [];
medium=[];
difficult = [];

filename = strcat('Result/freqratio_',num2str(s),'.xlsx');
easy = horzcat(e_alphabeta',e_alphagamma',e_alphadelta',e_alphatheta' ...
                ,e_betaalpha', e_betagamma', e_betatheta',e_betadelta' ...
                , e_gammadelta',e_gammaalpha', e_gammabeta', e_gammatheta' ...
                , e_deltaalpha', e_deltabeta', e_deltatheta', e_deltagamma' ... 
                , e_thetadelta',e_thetaalpha', e_thetabeta', e_thetagamma' ...
                , e_theta_alphatheta', e_beta_alphabeta');


xlswrite(filename,easy,1);

medium = horzcat(m_alphabeta',m_alphagamma',m_alphadelta',m_alphatheta' ...
                ,m_betaalpha', m_betagamma', m_betatheta',m_betadelta' ...
                , m_gammadelta',m_gammaalpha', m_gammabeta', m_gammatheta' ...
                , m_deltaalpha', m_deltabeta', m_deltatheta', m_deltagamma' ... 
                , m_thetadelta',m_thetaalpha', m_thetabeta', m_thetagamma' ...
                , m_theta_alphatheta', m_beta_alphabeta');


xlswrite(filename,medium,2);
difficult = horzcat(d_alphabeta',d_alphagamma',d_alphadelta',d_alphatheta' ...
                ,d_betaalpha', d_betagamma', d_betatheta',d_betadelta' ...
                , d_gammadelta',d_gammaalpha', d_gammabeta', d_gammatheta' ...
                , d_deltaalpha', d_deltabeta', d_deltatheta', d_deltagamma' ... 
                , d_thetadelta',d_thetaalpha', d_thetabeta', d_thetagamma' ...
                , d_theta_alphatheta', d_beta_alphabeta');


xlswrite(filename,difficult,3);



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
%     d.data{index, 1} = char('SubjectID');
%     d.data{index, 2} = char('FeatureID');
%     d.data{index, 3} = char('ChannelID');
%     d.data{index, 4} = char('MeanERDEasy');
%     d.data{index, 5} = char('MeanERDMedium');
%     d.data{index, 6} = char('MeanERDDifficult');
%     d.data{index, 7} = char('EasyMeanRank');
%     d.data{index, 8} = char('MediumMeanRank');
%     d.data{index, 9} = char('HardMeanRank');
%     d.data{index, 10} = char('PValue');
%     d.data{index, 11} = char('PStatus');
%     d.data{index, 12} = char('FeatureName');
%     d.data{index, 13} = char('ChannelLabel');
%     d.data{index, 14} = char('FreqFrom');
%     d.data{index, 15} = char('FreqTo');
%     
%     index = index+1;
%     channelLabel = {'AF3';'F7';'F3';'FC5';'T7';'P7';
%                         'O1';'O2';'P8';'T8';'FC6';'F4';'F8';'AF4'};
%     featureLabel ={'LowerAlpha1';'LowerAlpha2';'UpperAlpha';'IAFTheta';'Alpha';
%                 'Beta';'Gamma';'Delta';'Theta';'AlphaTheta';'AlphaBeta';'All'};
% 
%     for ch=1:no_of_channels            
%         for f=1:size(freq_bands, 1) 
%             for s= 1: size(subject_files, 1)
%                     erd_allData = [erd_easy_feature{s,f,ch}'; erd_medium_feature{s,f,ch}';  erd_hard_feature{s,f,ch}']; 
%                     erd_groups = [ones(size(erd_easy_feature{s,f,ch}')); 2 * ones(size(erd_medium_feature{s,f,ch}')); 3 * ones(size(erd_hard_feature{s,f,ch}'))];
% 
%                     [P,ANOVATAB,STATS] = kruskalwallis(erd_allData, erd_groups, 'off');
%                     
%                     d.data{index, 1} = s;
%                     d.data{index, 2} = f;
%                     d.data{index, 3} = ch;
%                     d.data{index, 4} = mean(erd_easy_feature{s,f,ch});
%                     d.data{index, 5} = mean(erd_medium_feature{s,f,ch});
%                     d.data{index, 6} = mean(erd_hard_feature{s,f,ch});
%                     
%                     if(size(STATS.meanranks(1,:), 2) >=1)
%                         d.data{index, 7}= STATS.meanranks(1,1);
%                     else
%                         d.data{index, 7} = 0;
%                     end
%                     
%                     if(size(STATS.meanranks(1,:), 2) >=2)
%                         d.data{index, 8}= STATS.meanranks(1,2);
%                     else
%                         d.data{index, 8} = 0;
%                     end
%                     
%                     if(size(STATS.meanranks(1,:), 2) >=3)
%                         d.data{index, 9}= STATS.meanranks(1,3);
%                     else
%                         d.data{index, 9} = 0;
%                     end
% 
%                     d.data{index, 10}= P;
%                     
%                     if(P <=0.05)
%                         d.data{index, 11}= char('Yes');
%                     else
%                         d.data{index, 11}= char('No');
%                     end
%                     
%                     d.data{index, 12} = featureLabel{f};
%                     d.data{index, 13} = channelLabel{ch};
%                     d.data{index, 14} = freq_bands(f,1);
%                     d.data{index, 15} = freq_bands(f,2);
%                     index =index+1;
%             end
%         end
%     end 
%     
%     %save data to excel
%     xlwrite(filename,d.data);
%     disp('All subjects processed completely !!!');