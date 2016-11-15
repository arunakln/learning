
package puzzle_ad543;

/**
 *
 * @author Aruna Duraisingam - ad543
 * Date : 18/Apr/2016
 * 
 * The aim of this assessment is to solve the eleven tile puzzle, by finding the optimal 
 * solution to reach goal configuration from start.
 * 
 * To formulate this search problem, the following components are considered
 * State Space: which is defined as different configuration of tiles
 * Initial state: The first part of the filename. (16 characters - delimiter character 2) sent in mail.
 * Goal state : The second part of the filename (16 characters - delimiter 2) sent in mail.
 * Operator: '_' tile.
 * Actions - Move '_' tile up, down, right, left. 
 *      Condition: - Cannot move '_' tile to the  '*' marked tile (Disabled). It is considered to be illegal.
 * Because of the lesser branching factor which is between 2 and 4 and the space complexity is lesser
 * (O(d)), I have chosen iterative deepening algorithm to find the solution

 * My puzzle is laid out as follows:

        12**
        1234
        1234
        *23*

The files that I need to generate are the following:

        aa**_dcbdbdd*db*2db**a_cbaddd*db*.txt
        dd**dbdbdc_b*aa*2db**ddbdd_ab*ac*.txt
        bd**dcbda_ab*dd*2db**dc_baabd*dd*.txt
        bb**d_cdddab*ad*2bd**bdbcdad_*da*.txt
        ad**dbb_bdcd*da*2dd**abbdbd_c*ad*.txt
        ad**bd_adddb*bc*2ad**_dabdbdd*bc*.txt
        db**dbcad_ab*dd*2_d**ddbaacbb*dd*.txt
        db**add_bdca*db*2ba**ddacbd_d*db*.txt
        dd**_adcdbba*db*2dd**dbbda_ac*db*.txt
        db**_bddddab*ac*2dd**dbb_adbd*ca*.txt
        bd**da_acbdd*bd*2bd**a_dadcbd*db*.txt
        bd**dbda_bdc*da*2dd**bbacd_bd*da*.txt
        dd**abcbdd_b*ad*2ad**ddab_bbc*dd*.txt
        bd**bcadd_db*ad*2bb**dd_cdabd*ad*.txt
        ad**da_cbdbb*dd*2da**db_badcb*dd*.txt
        ba**ddb_dcad*db*2db**acddd_ba*db*.txt
 */
public class Main {
    
    
    /**
     * Main function loops through all the given files and find the 
     * optimal solution using iterative deepening algorithm.
     */
    public static void main(String[] args) {
        
        // Provided problem list
        String[] problemList = {"aa**_dcbdbdd*db*2db**a_cbaddd*db*", 
                                "dd**dbdbdc_b*aa*2db**ddbdd_ab*ac*",
                                "bd**dcbda_ab*dd*2db**dc_baabd*dd*",
                                "bb**d_cdddab*ad*2bd**bdbcdad_*da*",
                                "ad**dbb_bdcd*da*2dd**abbdbd_c*ad*",
                                "ad**bd_adddb*bc*2ad**_dabdbdd*bc*",
                                "db**dbcad_ab*dd*2_d**ddbaacbb*dd*",
                                "db**add_bdca*db*2ba**ddacbd_d*db*",
                                "dd**_adcdbba*db*2dd**dbbda_ac*db*",
                                "db**_bddddab*ac*2dd**dbb_adbd*ca*",
                                "bd**da_acbdd*bd*2bd**a_dadcbd*db*",
                                "bd**dbda_bdc*da*2dd**bbacd_bd*da*",
                                "dd**abcbdd_b*ad*2ad**ddab_bbc*dd*",
                                "bd**bcadd_db*ad*2bb**dd_cdabd*ad*",
                                "ad**da_cbdbb*dd*2da**db_badcb*dd*",
                                "ba**ddb_dcad*db*2db**acddd_ba*db*"};
        
        // loops through all the problem one by one and find the optimal soln using iterative 
        // deepening algorithm.
        for(int i=0; i< problemList.length; i++){
            String str = problemList[i];
            // Split the filename to inti state and goal state
            String[] ProblemString = str.split("2", 2);
            String initStateString = ProblemString[0];
            String goalStateString = ProblemString[1];    
            // max depth is set to 100.
            int maxDepth = 100;
            
            // call iterative deeping algorithm.
            new IterativeDeepening(initStateString, goalStateString, maxDepth, str);
        }    
        
    }
    
}
