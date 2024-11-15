package project_2;
import java.util.Random;

public class TreasuryAuditor implements Runnable {
    private static final int MAXSLEEPTIME = 45000;
    private static Random sleepTime = new Random();
    private ABankAccount jointAccount1;
    private ABankAccount jointAccount2;
    private String tname;// Thread name

    public TreasuryAuditor(ABankAccount shared1, ABankAccount shared2, String name) {
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