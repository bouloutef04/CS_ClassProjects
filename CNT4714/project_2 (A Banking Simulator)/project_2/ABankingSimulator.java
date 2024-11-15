/* Name: Fredrick Bouloute 
 * Course: CMT 4714 Fall 2024
 * Assignment title: Project 2 - Synchronized/Cooperating Threads – A Banking Simulation
 * Due Date: September 22, 2024
 */
//File is here in the case that the full package does not work

package project_2;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ABankingSimulator {
    public static final int MAX_AGENTS = 20;// Max banking agents
    public static final int MAX_ACCOUNTS = 2;// Max accounts in sim

    public static void main(String[] args) {
        // thread pool - size 20
        ExecutorService application = Executors.newFixedThreadPool(MAX_AGENTS);
        // Joint accounts
        ABankAccount jointAccount1 = new ABankAccount("JA-1");
        ABankAccount jointAccount2 = new ABankAccount("JA-2");

        try {
            // Headers
            System.out.println("* * *  SIMULATION BEGINS...\n");
            System.out
                    .println(
                            "Deposit Agents\t\t\t  Withdrawal Agents\t\t\t       Balances     \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t Transaction Number");
            System.out
                    .println(
                            "--------------\t\t\t  -----------------\t\t\t--------------------\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t-------------------");
            // MaxSleep times Withdrawal: 200 Depositors: 3000 Transfers: 25000 Internal
            // Audit: 45000 Treasury Audit: 55000
            Depositor DT1 = new Depositor(jointAccount1, jointAccount2, "DT1");
            Depositor DT2 = new Depositor(jointAccount1, jointAccount2, "DT2");
            Depositor DT3 = new Depositor(jointAccount1, jointAccount2, "DT3");
            Depositor DT4 = new Depositor(jointAccount1, jointAccount2, "DT4");
            Depositor DT5 = new Depositor(jointAccount1, jointAccount2, "DT5");
            // Add threads to Executor and start

            try {

                application.execute(new WithdrawAgent(jointAccount1, jointAccount2, "WT1"));
                application.execute(new WithdrawAgent(jointAccount1, jointAccount2, "WT2"));
                application.execute(new WithdrawAgent(jointAccount1, jointAccount2, "WT3"));
                application.execute(new WithdrawAgent(jointAccount1, jointAccount2, "WT4"));
                application.execute(new WithdrawAgent(jointAccount1, jointAccount2, "WT5"));
                application.execute(new WithdrawAgent(jointAccount1, jointAccount2, "WT6"));
                application.execute(new WithdrawAgent(jointAccount1, jointAccount2, "WT7"));
                application.execute(new WithdrawAgent(jointAccount1, jointAccount2, "WT8"));
                application.execute(new WithdrawAgent(jointAccount1, jointAccount2, "WT9"));
                application.execute(new WithdrawAgent(jointAccount1, jointAccount2, "WT10"));

                application.execute(DT1);
                application.execute(DT2);
                application.execute(DT3);
                application.execute(DT4);
                application.execute(DT5);
                application.execute(new TransferAgent(jointAccount1, jointAccount2, "TR1"));
                application.execute(new TransferAgent(jointAccount1, jointAccount2, "TR2"));
                application.execute(new InternalAuditor(jointAccount1, jointAccount2, "IA-1"));
                application.execute(new TreasuryAuditor(jointAccount1, jointAccount2, "TA-1"));
            

                
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            application.shutdown();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        application.shutdown();

    }

}