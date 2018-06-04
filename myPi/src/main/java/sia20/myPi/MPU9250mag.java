package sia20.myPi;

public class MPU9250mag {

    private MPU9250 master;
    Word word;
    MPU9250mag(MPU9250 mpu9250){
        master = mpu9250;
        word = new Word(master);
    }

    byte[] read(int regAddr){
        master.write(37, (byte)0b10001100);
        master.write(38, (byte)regAddr);
        master.write(39, (byte)0b10010110);
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        byte[] data = word.readBytes(73, 6);
        return data;
    }


    public void whoAmI(){
        master.write(37, (byte)0b10001100);
        master.write(38, (byte)0x00);
        master.write(39, (byte)0b10000001);
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        byte data = word.readBytes(73, 1)[0];
        System.out.println("I should be 72, I am: " + data);
    }
}
