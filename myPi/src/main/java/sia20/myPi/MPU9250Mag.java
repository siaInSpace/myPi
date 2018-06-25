package sia20.myPi;

public class MPU9250Mag {

    MPU9250 master;

    MPU9250Mag(MPU9250 mast){
        master = mast;
    }
    void whoAmI(){
        int slave0StartAddr = 0x25;
        master.write(slave0StartAddr, (byte)0b10001100);
        master.write(slave0StartAddr+1, (byte)0x00);
        master.write(slave0StartAddr+2, (byte)0b10000001);
        byte res = master.read(0x49);
        System.out.println("I should be 0x48, I am: " + String.format("0x%02X", res));
    }
}
