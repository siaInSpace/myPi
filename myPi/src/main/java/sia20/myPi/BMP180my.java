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

    public BMP180my(Oss oss, String calValsPath){
        this(oss);
        new FileSaver(calValsPath, "bmp180saveCalVals", this);
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
        AC2 = word.combToShort(vals[0][0], vals[0][1]);
        AC1 = word.combToShort(vals[1][0], vals[1][1]);
        AC3 = word.combToShort(vals[2][0], vals[2][1]);
        AC4 = word.combToInt(vals[3][0], vals[3][1]);
        AC5 = word.combToInt(vals[4][0], vals[4][1]);
        AC6 = word.combToInt(vals[5][0], vals[5][1]);
        B1 = word.combToShort(vals[6][0], vals[6][1]);
        B2 = word.combToShort(vals[7][0], vals[7][1]);
        MB = word.combToShort(vals[8][0], vals[8][1]);
        MC = word.combToShort(vals[9][0], vals[9][1]);
        MD = word.combToShort(vals[10][0], vals[10][1]);
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

    public void printCalVals() {
        System.out.print("AC1:\t");
        System.out.println(AC1);
        System.out.print("AC2:\t");
        System.out.println(AC2);
        System.out.print("AC3:\t");
        System.out.println(AC3);
        System.out.print("AC4:\t");
        System.out.println(AC4);
        System.out.print("AC5:\t");
        System.out.println(AC5);
        System.out.print("AC6:\t");
        System.out.println(AC6);
        System.out.print("B1:\t");
        System.out.println(B1);
        System.out.print("B2:\t");
        System.out.println(B2);
        System.out.print("MB:\t");
        System.out.println(MB);
        System.out.print("MC:\t");
        System.out.println(MC);
        System.out.print("MD:\t");
        System.out.println(MD);
    }
    public void printCalValsBinary() {
        System.out.print("AC1:\t");
        System.out.println(Integer.toBinaryString(AC1 & 0xFF));
        System.out.print("AC2:\t");
        System.out.println(Integer.toBinaryString(AC2 & 0xFF));
        System.out.print("AC3:\t");
        System.out.println(Integer.toBinaryString(AC3 & 0xFF));
        System.out.print("AC4:\t");
        System.out.println(Integer.toBinaryString(AC4 & 0xFF));
        System.out.print("AC5:\t");
        System.out.println(Integer.toBinaryString(AC5 & 0xFF));
        System.out.print("AC6:\t");
        System.out.println(Integer.toBinaryString(AC6 & 0xFF));
        System.out.print("B1:\t");
        System.out.println(Integer.toBinaryString(B1 & 0xFF));
        System.out.print("B2:\t");
        System.out.println(Integer.toBinaryString(B2 & 0xFF));
        System.out.print("MB:\t");
        System.out.println(Integer.toBinaryString(MB & 0xFF));
        System.out.print("MC:\t");
        System.out.println(Integer.toBinaryString(MC & 0xFF));
        System.out.print("MD:\t");
        System.out.println(Integer.toBinaryString(MD & 0xFF));
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
