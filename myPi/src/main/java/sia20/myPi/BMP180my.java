package sia20.myPi;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import sia20.myPi.FileSaver;

public class BMP180my {
    // calibration values
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


    // usefull adresses
    private final int I2Caddr = 0x77;
    private final int I2cSignalAddr = 0xF4;

    // commonly used objects
    private I2CDevice device;
    private Oss oss;
    private Word word;

    public BMP180my(Oss oss) {
        I2CBus bus = null;
        try {
            bus = I2CFactory.getInstance(I2CBus.BUS_1);
            device = bus.getDevice(I2Caddr);
        } catch (UnsupportedBusNumberException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
        word = new Word(device);
        this.oss = oss;
    }

    public BMP180my(Oss oss, String calValsPath) {
        this(oss);
        new FileSaver();
    }

    /**
     * @return the device
     */
    public I2CDevice getDevice() {
        return device;
    }

    public byte[][] readCalibarationValuesRaw() {
        int start = 0xAA;
        byte[][] calValues = new byte[11][2];
        for (int i = 0; i < 22; i += 2) {
            calValues[i / 2] = word.readBytes(start + i, 2);
        }
        return calValues;
    }

    public void combineCalibrationValues(byte[][] vals) {
        AC1 = word.toShort(vals[0]);
        AC2 = word.toShort(vals[1]);
        AC3 = word.toShort(vals[2]);
        AC4 = word.toInt(vals[3]);
        AC5 = word.toInt(vals[4]);
        AC6 = word.toInt(vals[5]);
        B1 = word.toShort(vals[6]);
        B2 = word.toShort(vals[7]);
        MB = word.toShort(vals[8]);
        MC = word.toShort(vals[9]);
        MD = word.toShort(vals[10]);
    }

    public byte[] readTempRaw() {
        byte signal = 0x2E;
        try {
            device.write(I2cSignalAddr, signal);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
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

    public void printCalVals() {
        System.out.println("AC1:\t" + AC1);
        System.out.println("AC2:\t" + AC2);
        System.out.println("AC3:\t" + AC3);
        System.out.println("AC4:\t" + AC4);
        System.out.println("AC5:\t" + AC5);
        System.out.println("AC6:\t" +AC6);
        System.out.println("B1:\t" + B1);
        System.out.println("B2:\t" + B2);
        System.out.println("MB:\t" + MB);
        System.out.println("MC:\t" + MC);
        System.out.println("MD:\t" + MD);
    }

    public void printCalValsBinary() {
        byte[][] dat = readCalibarationValuesRaw();
        for (byte[] calVal : dat) {
            System.out.print(Word.byteToBinaryPadded(calVal[0]));
            System.out.print(Word.byteToBinaryPadded(calVal[1]));
        }
    }

    // calibration values getters
    /**
     * @return the aC1
     */
    public short getAC1() {
        return AC1;
    }

    /**
     * @return the aC2
     */
    public short getAC2() {
        return AC2;
    }

    /**
     * @return the aC3
     */
    public short getAC3() {
        return AC3;
    }

    /**
     * @return the aC4
     */
    public int getAC4() {
        return AC4;
    }

    /**
     * @return the aC5
     */
    public int getAC5() {
        return AC5;
    }

    /**
     * @return the aC6
     */
    public int getAC6() {
        return AC6;
    }

    /**
     * @return the b1
     */
    public short getB1() {
        return B1;
    }

    /**
     * @return the b2
     */
    public short getB2() {
        return B2;
    }

    /**
     * @return the mB
     */
    public short getMB() {
        return MB;
    }

    /**
     * @return the mC
     */
    public short getMC() {
        return MC;
    }

    /**
     * @return the mD
     */
    public short getMD() {
        return MD;
    }
}
