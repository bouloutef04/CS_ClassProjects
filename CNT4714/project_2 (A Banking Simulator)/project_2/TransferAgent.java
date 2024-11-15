/* Name: Fredrick Bouloute 
 * Course: CMT 4714 Fall 2024
 * Assignment title: Project 2 - Synchronized/Cooperating Threads – A Banking Simulation
 * Due Date: September 22, 2024
 */
package project_2;
import java.util.Random;

public class TransferAgent implements Runnable {
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