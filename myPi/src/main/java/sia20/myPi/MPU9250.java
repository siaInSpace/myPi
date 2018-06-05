package sia20.myPi;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

class MPU9250 extends Sensor{
    private final int whoAmIAddr = 0x75;
    private final int whoAmIValueDefualt = 0x73;// usually 0x71, i don't know why this is 0x73 instead
    private MPU9250mag mag;

    MPU9250() {
        super(0x68);
        mag = new MPU9250mag(this);
    }

    void magWhoAmI(){
        System.out.println("I should be: 72, I am: ");
        mag.enableMaster();
        mag.configureSlave(true, 0x00, 1);
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        byte[] answer = mag.readMaster(1);
        for (byte data : answer) {
            System.out.println(data);
        }
    }



    void whoAmI() {
        int res = 0;
        res = read(whoAmIAddr);
        if (res == whoAmIValueDefualt) {
            System.out.println("I'm mpu9250");
        } else {
            System.out.println("Who am I? i don't know");
            System.out.print("I got this number, but that's not really me: ");
            System.out.println(String.format("0x%02X", res));
        }
    }
}
