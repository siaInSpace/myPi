package sia20.myPi;

public class BMP180Slave {
    MPU9250 master;

    BMP180Slave(MPU9250 mas){
        master = mas;
    }

    private void setupMagReadPressure(){
        master.disableBypass();
    }

    byte[] readTemp(){
        master.slaveDataOut(0, (byte)0x2E);
        master.configureSlave(0, false, 0x77, 0xF4, 1, false);
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            System.out.println("Could not wait 5ms, idk why");
            e.printStackTrace();
        }
        master.configureSlave(0, true, 0x77, 0xF6, 2, false);
        return master.returnExtData(2);
    }

    byte[] readPressure(Oss oss){
        master.slaveDataOut(0, (byte) (0x34 + (oss.getVal() << 6)));
        master.configureSlave(0, false, 0x77, 0xF4, 1, false);
        try {
            Thread.sleep(oss.getTime());
        } catch (InterruptedException e) {
            System.out.println("Could not wait " + oss.getTime() + "ms, idk why");
            e.printStackTrace();
        }
        master.configureSlave(0, true, 0x77, 0xF6, 3, false);
        return master.returnExtData(3);
    }

    void whoAmI(){
        master.configureSlave(0, true, 0x77, 0xD0, 1, false);
        byte res = master.returnExtData(1)[0];
        System.out.println("I should be 0x55, I am: " + String.format("0x%02X", res));

    }


    enum Oss {
        LOW_POWER(0, 5), STANDARD(1, 8), HIGHRES(2, 14), ULTRAHIGHRES(3, 26);

        private int id;
        private int waitTime;

        Oss(int val, int time) {
            this.id = val;
            this.waitTime = time;
        }

        int getVal() {
            return id;
        }

        int getTime() {
            return waitTime;
        }
    }

}
