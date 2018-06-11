package sia20.myPi;

public class MPU9250magBypass{

    Sensor sens;

    MPU9250magBypass(MPU9250 mpu){
        mpu.enableBypass();
        sens = new Sensor(0x0C);
    }

    void write(int address, byte data){
        sens.write(address, data);
    }

    byte read(int address){
        return sens.read(address);
    }



}
