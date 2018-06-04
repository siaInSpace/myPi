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
        mag = new MPU9250mag();
    }
    public void setByPassMode(boolean mode){
        int bypassAddress = 0x37;
        int masterEnableAddress = 0x6A;
        byte masteEnabelSignal;
        byte bypassSignal;
        if (mode){
            bypassSignal = 0x02;
            masteEnabelSignal = 0b01000000;
        }else{
            bypassSignal = 0x00;
            masteEnabelSignal = 0b01100000;
        }
        write(bypassAddress, bypassSignal);
        write(masterEnableAddress, masteEnabelSignal);
    }

    public void magWhoAmI(){
        setByPassMode(true);
        mag.whoAmI();
        setByPassMode(false);
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
