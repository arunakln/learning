%-------------------------------------------------------------------------%
% Author: Aruna Duraisingam
% Date : 20-Aug-2016
% Function to execute each fold train and test data (recursively) and 
% classify task difficulty level and compute classification success rate.
%-------------------------------------------------------------------------%


function testval = networkFunction(XTRAIN, YTRAIN, XTEST, YTEST)

    % creates a two layer network with 10 hidden layers.
    % Default training function trainlm.
    
    net = feedforwardnet(10);
    
    % Initializes the weight and bias
%     net = init(net);
    
    % modified the transfer function of the layers in netwrok
    net.layers{2}.transferFcn = 'logsig';
    net.layers{1}.transferFcn = 'logsig';
    
    net.divideParam.trainRatio = 70/100;
    net.divideParam.valRatio = 15/100;
    net.divideParam.testRatio = 15/100;
    
    net.trainParam.epochs = 100;
    
    % Train the network. Defualt performance function 'mse' (mean Squared
    % Error)
    [net, tr] = train(net, XTRAIN', YTRAIN');

    % Network responses after trained and validated
    % simulate a network over  input range
    output = net(XTEST');

    
    % find which have highest probability value
    [ maxout ,maxIndex] = max(output',[],2);
    


    % convert  into a format that can be compared with maxindex
    [ testout ,testIndex] = find(YTEST);

    % Correctly classified samples divided by the classified samples. 
    % Inconclusive results are not counted
    % Check the success of the classifier
    cp = classperf(testIndex, maxIndex);
    
   
    figure, plotperform(tr)
    figure, plottrainstate(tr)

    
    % get the correctrate
    testval = (cp.CorrectRate) * 100; 
end