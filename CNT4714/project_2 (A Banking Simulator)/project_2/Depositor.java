/* Name: Fredrick Bouloute 
 * Course: CMT 4714 Fall 2024
 * Assignment title: Project 2 - Synchronized/Cooperating Threads – A Banking Simulation
 * Due Date: September 22, 2024
 */
package project_2;
import java.util.Random;


// DEPOSITOR
// CLASS************************************************************************************
public class Depositor implements Runnable {
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