package sia20.myPi;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

public class BMP180my {
    //calibration values
    private short AC1;
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

    //usefull adresses
    private final int I2Caddr = 0x77;
    private final int I2cSignalAddr = 0xF4;
    
    //commonly used objects
    private I2CDevice device;
    private Oss oss;
    private Word word;

    public BMP180my(Oss oss) throws IOException { 
        I2CBus bus = null;
        try {
            bus = I2CFactory.getInstance(I2CBus.BUS_1);
        } catch (UnsupportedBusNumberException e) {
            System.out.println(e.getLocalizedMessage());
        }
        
        device = bus.getDevice(I2Caddr);
        word = new Word(device);
        this.oss = oss;
    }

    /**
     * @return the device
     */
    public I2CDevice getDevice() {
        return device;
    }

    public byte[][] readCalibarationValuesRaw(){
        int start = 0xAA;
        byte[][] calValues = new byte[11][2];
        for (int i = 0; i < 22; i += 2) {
            calValues[i / 2] = word.readBytes(start + i, 2);
        }
        return calValues;
    }

    public void readCalibarationValues(){
        byte[][] vals = this.readCalibarationValuesRaw();
        short AC2 = word.combToShort(vals[0][0], vals[0][1]);
        short AC1 = word.combToShort(vals[1][0], vals[1][1]);
        short AC3 = word.combToShort(vals[2][0], vals[2][1]);
        int AC4 = word.combToInt(vals[3][0], vals[3][1]);
        int AC5 = word.combToInt(vals[4][0], vals[4][1]);
        int AC6 = word.combToInt(vals[5][0], vals[5][1]);
        short B1 = word.combToShort(vals[6][0], vals[6][1]);
        short B2 = word.combToShort(vals[7][0], vals[7][1]);
        short MB = word.combToShort(vals[8][0], vals[8][1]);
        short MC = word.combToShort(vals[9][0], vals[9][1]);
        short MD = word.combToShort(vals[10][0], vals[10][1]);
    }

    
    public byte[] readTempRaw() throws IOException {
        byte signal = 0x2E;
        device.write(I2cSignalAddr, signal);
        try {
            TimeUnit.MILLISECONDS.wait(5);
        } catch (InterruptedException e) {
            System.out.println("Could not wait 5ms, idk why");
            e.printStackTrace();
        }
        return word.readBytes(0xF6, 2);
    }

    public byte[] readPressureRaw() throws IOException {
        byte signal = (byte) (0x34 + oss.getVal() << 6);
        device.write(I2cSignalAddr, signal);
        try {
            TimeUnit.MILLISECONDS.wait(oss.getTime());
        } catch (InterruptedException e) {
            System.out.println("Could not wait Xms, idk why");
            e.printStackTrace();
        }
        return word.readBytes(0xF6, 3);
    }

    public enum Oss {
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

    public void saveCalValues(String filePath) {
        PrintWriter pw = null;
        byte[][] calVals = null;
        try {
            pw = new PrintWriter(new File(filePath));
            calVals = this.readCalibarationValuesRaw();
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
        for (byte[] calVal : calVals) {
            pw.print(calVal[0]);
            pw.print("-");
            pw.println(calVal[1]);
        }
    }
}
