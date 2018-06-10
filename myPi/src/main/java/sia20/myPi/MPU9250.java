package sia20.myPi;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

class MPU9250 extends Sensor{

    MPU9250() {
        super(0x68);
    }

    byte[][] readRawValues(){
        byte[][] data = new byte[3][];
        data[0] = readAccRaw();
        data[1] = readTempRaw();
        data[2] = readGyrRaw();
        return data;
    }

    byte[] readAccRaw(){
        return word.readBytes(0x3b, 6);
    }

    byte[] readTempRaw(){
        return word.readBytes(0x41, 2);
    }

    byte[] readGyrRaw(){
        return word.readBytes(0x43, 6);

    }

    void whoAmI() {
        int res;
        int whoAmIValueDefault = 0x73;// usually 0x71, i don't know why this is 0x73 instead
        int whoAmIAddress = 0x75;
        res = read(whoAmIAddress);
        if (res == whoAmIValueDefault) {
            System.out.println("I'm mpu9250");
        } else {
            System.out.println("Who am I? i don't know");
            System.out.print("I got this number, but that's not really me: ");
            System.out.println(String.format("0x%02X", res));
        }
    }
}
