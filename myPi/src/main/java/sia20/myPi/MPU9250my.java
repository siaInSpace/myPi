package sia20.myPi;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

class MPU9250my {
    private I2CDevice device;
    private int sensAddr = 0x68;
    private final int whoAmIAddr = 0x75;
    private final int whoAmIValueDefualt = 0x73;// usually 0x71, i don't know why this is 0x73 instead

    MPU9250my() {
        I2CBus bus = null;
        try {
            bus = I2CFactory.getInstance(I2CBus.BUS_1);
        } catch (I2CFactory.UnsupportedBusNumberException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
        try {
            device = bus.getDevice(sensAddr);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void setByPassMode(boolean mode){
        int regAddr = 55;
        byte signal;
        if (mode){
            signal = 0b10;
        }else{
            signal = 0b00;
        }
        try {
            device.write(regAddr, signal);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }



    void whoAmI() {
        int res = 0;
        try {
            res = device.read(whoAmIAddr);
        } catch (IOException e) {
            System.out.println("Cannot read whoAmI MPU9250");
            System.out.println(e.getLocalizedMessage());
        }
        if (res == whoAmIValueDefualt) {
            System.out.println("I'm mpu9250");
        } else {
            System.out.println("Who am I? i don't know");
            System.out.print("I got this number, but thats not really me: ");
            System.out.println(String.format("0x%02X", res));
        }
    }
}
