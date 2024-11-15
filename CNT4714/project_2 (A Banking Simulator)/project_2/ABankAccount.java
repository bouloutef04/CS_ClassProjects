/* Name: Fredrick Bouloute 
 * Course: CMT 4714 Fall 2024
 * Assignment title: Project 2 - Synchronized/Cooperating Threads – A Banking Simulation
 * Due Date: September 22, 2024
 */
package project_2;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;



public class ABankAccount implements TheBank {
    // Varibales for the bank account
    int balance;
    String accountName;
    private static int tranactionNumber; // Counts total transactions
    private static int internAuditTransactions;// Counts transactions since last internal audit
    private static int treasuryAuditTransactions;// Counts transactions since last treasury audit

    // Lock to control mutually exclusive acceess to the bank account
    private Lock accessLock = new ReentrantLock();
    // Condition variable as needed
    private Condition canWithdraw1 = accessLock.newCondition();
    private Condition canWithdraw2 = accessLock.newCondition();
    private Condition canTransfer = accessLock.newCondition();

    public ABankAccount(String newAccountName) {
        balance = 0;
        accountName = newAccountName;
        tranactionNumber = 0;
    }

    private static final int DEPOSIT_ALERT_LEVEL = 450;
    private static final int WITHDRAWAL_ALERT_LEVEL = 90;
    private static final int TRANSFER_ALERT_LEVEL = 500;

    // method used to log flagged tranactions made against the ban account
    public void flagged_transaction(int value, String transaction_thread, String transaction_type) {
        if (transaction_type.equals("depositAlert")) {
            System.out.println("* * * Flagged Transaction * * * Agent " + transaction_thread
                    + " Made a Deposit In Excess Of $450.00 USD - See Flagged Transaction Log.\n");
                    try {
                        //Make format for date
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM dd, Y, hh:mm:ss a z");
                        //Open writer for transaction file
                        Date thisDate = new Date();
                        BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true));// write to file
                        writer.append("Agent " + transaction_thread + " issued deposit of $" + value + " at: " + dateFormat.format(thisDate) + "   Transaction Number: " + tranactionNumber);
                        writer.append("\n");
                        writer.flush();
                        writer.close();
                    } catch (IOException f) {
                        System.out.println("Exception occured");
                    }
        }

        if (transaction_type.equals("withdrawalAlert")) {
            System.out.println("* * * Flagged Transaction * * * Agent " + transaction_thread
                    + " Made a Withdrawal In Excess Of $90.00 USD - See Flagged Transaction Log.\n");
            try{
            //Make format for date
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM dd, Y, hh:mm:ss a z");
            //Open writer for transaction file
            Date thisDate = new Date();
            BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true));// write to file
            writer.append("Agent " + transaction_thread + " issued withdrawal of $" + value + " at: " + dateFormat.format(thisDate) + "   Transaction Number: " + tranactionNumber);
            writer.append("\n");
            writer.flush();
            writer.close();
            }
            catch(IOException f){
                System.out.println("Exception occured");
            }
        }

        if (transaction_type.equals("transferAlert")) {
            System.out.println("* * * Flagged Transaction * * * Agent " + transaction_thread
                    + " Made a Transfer In Excess Of $500.00 USD - See Flagged Transaction Log.\n");
        }
    }

    // Method used to make a deposit into the bank account
    public void deposit(int depositAmount, String sharedAcct, String threadName, int actNum) {
        // get the lock on the acoount
        accessLock.lock();
        try {

            // make deposit into account
            balance += depositAmount;
            tranactionNumber++;
            internAuditTransactions++;
            treasuryAuditTransactions++;
            System.out.println("Agent " + threadName + " deposits $" + depositAmount + " into: " + sharedAcct
                    + "\t\t\t\t\t(+) " + sharedAcct + "balance is $" + balance + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t "
                    + tranactionNumber);

            // handle transaction logging for flagegd tranaction
            if (depositAmount > DEPOSIT_ALERT_LEVEL) {
                flagged_transaction(depositAmount, threadName, "depositAlert");
            }
            // signal all waiting threads that deposit has been made
            if (actNum == 1)
                canWithdraw1.signal();
            else
                canWithdraw2.signal();
            canTransfer.signal();

        } catch (Exception e) {
            System.out.println("Exception thrown making a deposit of funds.");
        } finally {
            // unlock the bank account
            accessLock.unlock();
        }
    }

    // Mehthod used for withdrawal
    public void withdrawal(int withdrawalValue, String sharedAcct, String threadName, int actNum) {
        // get the lock on the acoount
        accessLock.lock();
        try {
            // attempt withdrawal
            if (withdrawalValue > balance) {// if not block
                System.out.println(
                        "\t\t\t\t\t\tAgent " + threadName + " attempts to withdraw $" + withdrawalValue + " fron "
                                + sharedAcct
                                + " (*****) WITHDRAWAL BLOCKED- INSUFFICIENT FUNDS!!! Balance is only $" + balance
                                + "\n");
                if (actNum == 1)
                    canWithdraw1.await();
                else
                    canWithdraw2.await();
            } else {// else make withdrawal
                balance -= withdrawalValue;
                tranactionNumber++;
                internAuditTransactions++;
                treasuryAuditTransactions++;
                System.out
                        .println("\t\t\t\t\t\tAgent " + threadName + " withdraws $" + withdrawalValue + "from "
                                + sharedAcct
                                + "\t\t(-) " + sharedAcct + "balance is $" + balance
                                + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t " + tranactionNumber);
                // check for flagged tranaction
                if (withdrawalValue > WITHDRAWAL_ALERT_LEVEL) {
                    flagged_transaction(withdrawalValue, threadName, "transferAler");
                }

            }
            // check for flagged tranaction

        } catch (Exception e) {
            System.out.println("Exception thrown making a withdrawal of funds.");
        } finally {
            // unlock the bank account
            accessLock.unlock();
        }
    }

    // method used to make a transfer
    public void transfer(int transferValue, String fromAccoutn, String toAccount, String threadName, int startActNum,
            int actNum, ABankAccount transferAccount) {
        try {
            // check for sufficient funds
            if (transferValue > balance) {// if not, end tranaction
                canTransfer.await();
            } else {// else complete transfer
                balance -= transferValue;
                transferAccount.balance += transferValue;
                System.out.println("\t\tTRANSFER --> Agent " + threadName + " transferring $" + transferValue
                        + " from " + fromAccoutn + " to " + toAccount + " - - " + toAccount + "balance is now $"
                        + balance);

                System.out.println("\t\tTRANSFER COMPLETE --> Account " + toAccount + " balance is now $"
                        + transferAccount.balance);
                System.out.println("");
                tranactionNumber++;
                internAuditTransactions++;
                treasuryAuditTransactions++;

            }

            // check for flagged transfer
            if (transferValue > TRANSFER_ALERT_LEVEL) {
                flagged_transaction(transferValue, threadName, "transferAlert");
            }
        } catch (Exception e) {
            System.out.println("Exception thrown attempting to transfer funds.");
        }
    }

    // method used to make an audit of the bank account
    public void internalAudit(String account, String auditorThread) {
        try {
            if (account == "JA-1") {// Print out for the first account first
                // print headings
                System.out.println("");
                System.out.println(
                        "********************************************************************************************\n\n");
                System.out.println("Internal Bank Audit Beginning...\n");
                // print # transactions since last audit line
                System.out.println("\t\tThe total number of transactions since the last Internal audit is: "
                        + internAuditTransactions);
                System.out.println("");
                // run audit numbers and print results
                System.out
                        .println("\t\tINTERNAL BANK AUDITOR FINDS CURRETN ACCOUNT BALANCE FOR JA-1 TO BE: $" + balance);

            } else {// Second account
                System.out
                        .println("\t\tINTERNAL BANK AUDITOR FINDS CURRETN ACCOUNT BALANCE FOR JA-1 TO BE: $" + balance);
                System.out.println("\nInternal Bank Audit Complete.\n\n");
                System.out.println(
                        "********************************************************************************************\n\n");
            }
            internAuditTransactions = 0;// Reset internal Transactions
        } catch (Exception e) {
            System.out.println("Exception thrown attempting to get the balance by an Internal Auditor.");
        }
        accessLock.unlock();
    }

    public void treasuryAudit(String account, String auditorThread) {
        try {
            // print headings
            if (account == "JA-1") {
                System.out.println("");
                System.out.println(
                        "********************************************************************************************\n");
                System.out.println("UNITED STATES DEPARTMENT OF THE TREASURY - Bank Audit Beginning...\n");
                // print # transactions since last audit line
                System.out.println("\tThe total number of transction since last Treasury Department audit is: "
                        + treasuryAuditTransactions);// Add the transaction amount

                // run audit numbers and print results
                System.out.println("\tTREASURY DEPT AUDITOR FINDS CURRENT ACCOUNT BALANCE FOR JA-1 TO BE: $" + balance);// Add
                // reset # tranaction counters
                treasuryAuditTransactions = 0;
            }
            if (account == "JA-2") {
                System.out.println("\tTREASURY DEPT AUDITOR FINDS CURRENT ACCOUNT BALANCE FOR JA-2 TO BE: $" + balance);
                System.out.println("");
                System.out.println("UNITED STATES DEPARTMENT OF THE TREASURY - Bank Audit Terminated...\n\n");
                System.out.println(
                        "********************************************************************************************\n\n");
            }

        } catch (Exception e) {
            System.out.println("Exception thrown attempting to get the balance by an Treasury Department Auditor.");
        }
    }

    public boolean lock() {// Used to lock both accounts for Transfer/Audits
        boolean locked = false;
        locked = accessLock.tryLock();
        return locked;
    }

    public void unlock() {// Look at previous method for explanation
        try {
            accessLock.unlock();
        } catch (Exception e) {
            // Just ensures that nothing happens if it can't unlock for one reason or
            // another
        }

    }
}