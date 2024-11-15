/* Name: Fredrick Bouloute 
 * Course: CMT 4714 Fall 2024
 * Assignment title: Project 2 - Synchronized/Cooperating Threads – A Banking Simulation
 * Due Date: September 22, 2024
 */
package project_2;
import java.util.Random;

public class WithdrawAgent implements Runnable {
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