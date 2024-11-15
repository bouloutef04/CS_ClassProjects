/* Name: Fredrick Bouloute 
 * Course: CMT 4714 Fall 2024
 * Assignment title: Project 2 - Synchronized/Cooperating Threads – A Banking Simulation
 * Due Date: September 22, 2024
 */
package project_2;

// The bank interface
public interface TheBank{

    public abstract void deposit(int depositValue, String accountNum, String name, int actNum);

    public abstract void withdrawal(int withdrawalValue, String accountNum, String name, int actNum);

    public abstract void transfer(int transferValue, String fromAccoutn, String toAccount, String threadName, int startActNum,
    int actNum, ABankAccount transferAccount);
    public abstract void flagged_transaction(int value, String name, String trans_type);

    public abstract void internalAudit(String account, String auditor);// Idk Examines balance only

    public abstract void treasuryAudit(String account, String auditor);// Idk Examines balance only
}
