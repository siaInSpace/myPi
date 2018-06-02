package sia20.myPi;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

import java.io.IOException;

class BMP180my {
    // calibration values
    /*private short AC1;
    private short AC2;
    private short AC3;
    private int AC4;
    private int AC5;
    private int AC6;
    private short B1;
    private short B2;
    private short MB;
    private short MC;
    private short MD;
*/

    // usefull adresses
    private final int I2cSignalAddress = 0xF4;

    // commonly used objects
    private I2CDevice device;
    private Oss oss;
    private Word word;

    BMP180my(Oss oss) {
        I2CBus bus;
        try {
            bus = I2CFactory.getInstance(I2CBus.BUS_1);
            device = bus.getDevice(0x77);
        } catch (UnsupportedBusNumberException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
        word = new Word(device);
        this.oss = oss;
    }

    BMP180my (Oss oss, String pathName){
        this(oss);
        FileSaver fs = new FileSaver();
        fs.start(pathName, readCalibrationValuesRaw());
    }
    /**
     * @return the device
     */
    public I2CDevice getDevice() {
        return device;
    }

    private byte[][] readCalibrationValuesRaw() {
        int start = 0xAA;
        byte[][] calValues = new byte[11][2];
        for (int i = 0; i < 22; i += 2) {
            calValues[i / 2] = word.readBytes(start + i, 2);
        }
        return calValues;
    }

    byte[] readTempRaw() {
        byte signal = 0x2E;
        try {
            device.write(I2cSignalAddress, signal);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            System.out.println("Could not wait 5ms, idk why");
            e.printStackTrace();
        }
        return word.readBytes(0xF6, 2);
    }

    byte[] readPressureRaw(){
        byte signal = (byte) (0x34 + (oss.getVal() << 6));
        try{
            device.write(I2cSignalAddress, signal);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
        try {
            Thread.sleep(oss.getTime());
        } catch (InterruptedException e) {
            System.out.println("Could not wait Xms, idk why");
            e.printStackTrace();
        }
        return word.readBytes(0xF6, 3);
    }

    byte[][] readRawValues(){
        byte[][] b = {readTempRaw(), readPressureRaw()};
        return b;
    }

    enum Oss {
        LOW_POWER(0, 5), STANDARD(1, 8), HIGHRES(2, 14), ULTRAHIGHRES(3, 26);

        private int id;
        private int waitTime;

        Oss(int val, int time) {
            this.id = val;
            this.waitTime = time;
        }

        public int getVal() {
            return id;
        }

        public int getTime() {
            return waitTime;
        }
    }
}
