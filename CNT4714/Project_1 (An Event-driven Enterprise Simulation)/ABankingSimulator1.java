/* Name: Fredrick Bouloute 
 * Course: CMT 4714 Fall 2024
 * Assignment title: Project 2 - Synchronized/Cooperating Threads – A Banking Simulation
 * Due Date: September 22, 2024
 */
//File is here in the case that the full package does not work

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Random;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.time.*;
import java.time.format.DateTimeFormatter;;

public class ABankingSimulator1 {
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
                application.execute(new internalAuditor(jointAccount1, jointAccount2, "IA-1"));
                application.execute(new treasuryAuditor(jointAccount1, jointAccount2, "TA-1"));

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

class ABankAccount implements TheBank {
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
                        SimpleDateFormat dateID = new SimpleDateFormat("ddMMYhhmmss, ");
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
            SimpleDateFormat dateID = new SimpleDateFormat("ddMMYhhmmss, ");
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

// DEPOSITOR
// CLASS************************************************************************************
class Depositor implements Runnable {
    private static final int MAX_DEPOSIT = 600;
    private static final int MAXSLEEPTIME = 3500;
    private static Random depositAmount = new Random();
    private int depositNum = 0;
    private static Random sleepTime = new Random();
    private ABankAccount jointAccount1;
    private ABankAccount jointAccount2;
    private int accountNum;
    private String tname;

    public Depositor(ABankAccount shared1, ABankAccount shared2, String name)// Constructor might need more. Depends
    {
        jointAccount1 = shared1;
        jointAccount2 = shared2;
        tname = name;
    }

    @Override
    public void run() {
        while (true) {
            try {
                accountNum = (Math.random() <= 0.5) ? 1 : 2;// Did not know this was a thing. Thanks professor
                if (accountNum == 1) {
                    // Make deposit in account 1
                    depositNum = depositAmount.nextInt(MAX_DEPOSIT) + 1;
                    jointAccount1.deposit(depositNum, "JA-1", tname, 1);
                } else {
                    // Make deposit in account 2
                    depositNum = depositAmount.nextInt(MAX_DEPOSIT) + 1;
                    jointAccount2.deposit(depositNum, "JA-2", tname, 2);
                }
                Thread.sleep(sleepTime.nextInt(MAXSLEEPTIME - 1 + 1) + 1);

            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }
}

class WithdrawAgent implements Runnable {
    private static final int MAX_WITHDRAWAL = 99;
    private static final int MAXSLEEPTIME = 100;
    private static Random withdrawalAmount = new Random();
    private int withdrawNum = 0;
    private static Random sleepTime = new Random();
    private ABankAccount jointAccount1;
    private ABankAccount jointAccount2;
    private int accountNum;
    private String tname;

    public WithdrawAgent(ABankAccount shared1, ABankAccount shared2, String name)// Constructor might need more. Depends
    {
        jointAccount1 = shared1;
        jointAccount2 = shared2;
        tname = name;
    }

    @Override
    public void run() {
        while (true) {
            double randomNumber = Math.random();
            if (randomNumber < 0.5) {
                accountNum = 1;
            } else {
                accountNum = 2;
            }
            try {
                if (accountNum == 1) {
                    // Make deposit in account 1
                    withdrawNum = withdrawalAmount.nextInt(MAX_WITHDRAWAL) + 1;
                    jointAccount1.withdrawal(withdrawNum, "JA-1", tname, 1);
                } else {
                    // Make deposit in account 2
                    withdrawNum = withdrawalAmount.nextInt(MAX_WITHDRAWAL) + 1;
                    jointAccount2.withdrawal(withdrawNum, "JA-2", tname, 2);
                }

                int sT = sleepTime.nextInt(MAXSLEEPTIME - 1 + 1) + 1;
                Thread.sleep(sT);

            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }
}

class TransferAgent implements Runnable {
    private static final int MAX_TRANSFER = 600;// Could not find the max transfer so I picked 600
    private static final int MAXSLEEPTIME = 15000;
    private static Random transferAmount = new Random();
    private int transferNum = 0;
    private static Random sleepTime = new Random();
    private ABankAccount jointAccount1;
    private ABankAccount jointAccount2;
    private int accountNum;
    private String tname;

    public TransferAgent(ABankAccount shared1, ABankAccount shared2, String name) {
        jointAccount1 = shared1;
        jointAccount2 = shared2;
        tname = name;
    }

    @Override
    public void run() {
        while (true) {
            double randomNumber = Math.random();
            if (randomNumber < 0.5) {
                accountNum = 1;
            } else {
                accountNum = 2;
            }
            try {
                if (accountNum == 1) {
                    // Make transfer from account 1
                    boolean locked1 = jointAccount1.lock();// Lock both accounts
                    boolean locked2 = jointAccount2.lock();
                    if (locked1 == false || locked2 == false) {// If one account could not be locked, cancel transfer
                        jointAccount1.unlock();
                        jointAccount2.unlock();
                    } else {// Else, make transfer and unlock
                        transferNum = transferAmount.nextInt(MAX_TRANSFER) + 1;
                        jointAccount1.transfer(transferNum, "JA-1", "JA-2", tname, 1, 1, jointAccount2);

                        jointAccount1.unlock();
                        jointAccount2.unlock();
                    }

                } else {
                    // Make transfer from account 2
                    boolean locked1 = jointAccount1.lock();
                    boolean locked2 = jointAccount2.lock();
                    if (locked1 == false || locked2 == false) {
                        jointAccount1.unlock();
                        jointAccount2.unlock();
                    } else {
                        transferNum = transferAmount.nextInt(MAX_TRANSFER) + 1;
                        jointAccount2.transfer(transferNum, "JA-2", "JA-1", tname, 2, 2, jointAccount1);

                        jointAccount1.unlock();
                        jointAccount2.unlock();
                    }
                }
                // Thread sleep
                int sT = sleepTime.nextInt(MAXSLEEPTIME - 1 + 1) + 1;
                Thread.sleep(sT);

            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }
}

class internalAuditor implements Runnable {
    private static final int MAXSLEEPTIME = 35000;
    private static Random sleepTime = new Random();
    private ABankAccount jointAccount1;
    private ABankAccount jointAccount2;
    private String tname;// Thread name

    public internalAuditor(ABankAccount shared1, ABankAccount shared2, String name) {
        jointAccount1 = shared1;
        jointAccount2 = shared2;
        tname = name;
    }

    @Override
    public void run() {
        while (true) {
            try {

                // Make transfer from account 1
                boolean locked1 = jointAccount1.lock();// Lock both accounts
                boolean locked2 = jointAccount2.lock();
                if (locked1 == false || locked2 == false) {// If one account could not be locked, cancel transfer
                    jointAccount1.unlock();
                    jointAccount2.unlock();
                } else {// Else, perform internal audit and then unlock
                    jointAccount1.internalAudit("JA-1", tname);
                    jointAccount2.internalAudit("JA-2", tname);
                    jointAccount1.unlock();
                    jointAccount2.unlock();
                }

                // Thread sleep
                int sT = sleepTime.nextInt(MAXSLEEPTIME - 1 + 1) + 1;
                Thread.sleep(sT);

            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }
}

class treasuryAuditor implements Runnable {
    private static final int MAXSLEEPTIME = 45000;
    private static Random sleepTime = new Random();
    private ABankAccount jointAccount1;
    private ABankAccount jointAccount2;
    private String tname;// Thread name

    public treasuryAuditor(ABankAccount shared1, ABankAccount shared2, String name) {
        jointAccount1 = shared1;
        jointAccount2 = shared2;
        tname = name;
    }

    @Override
    public void run() {
        while (true) {
            try {

                // Make transfer from account 1
                boolean locked1 = jointAccount1.lock();// Lock both accounts
                boolean locked2 = jointAccount2.lock();
                if (locked1 == false || locked2 == false) {// If one account could not be locked, cancel transfer
                    jointAccount1.unlock();
                    jointAccount2.unlock();
                } else {// Else, perform internal audit and then unlock
                    jointAccount1.treasuryAudit("JA-1", tname);
                    jointAccount2.treasuryAudit("JA-2", tname);
                    jointAccount1.unlock();
                    jointAccount2.unlock();
                }

                // Thread sleep
                int sT = sleepTime.nextInt(MAXSLEEPTIME - 1 + 1) + 1;
                Thread.sleep(sT);

            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }
}

// The bank interface
interface TheBank {

    public abstract void deposit(int depositValue, String accountNum, String name, int actNum);

    public abstract void withdrawal(int withdrawalValue, String accountNum, String name, int actNum);

    public abstract void transfer(int transferValue, String fromAccount, String toAccount, String name, int startActNum,
            int actNum, ABankAccount transferAccount);

    public abstract void flagged_transaction(int value, String name, String trans_type);

    public abstract void internalAudit(String account, String auditor);// Idk Examines balance only

    public abstract void treasuryAudit(String account, String auditor);// Idk Examines balance only
}
