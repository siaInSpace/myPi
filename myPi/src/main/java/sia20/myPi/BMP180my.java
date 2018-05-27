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

    // private short[] calibrationSignedValues; //{AC1, AC2, AC3, B1, B2, MB, MC,
    // MD}
    // private int[] calibrationUnsignedValues; //{AC4, AC5, AC6}

    private I2CDevice device;
    int writeAddress = 0xF4;
    Oss oss;

    public BMP180my(Oss oss) throws IOException {
        final int bmp180_i2cAddr = 0x77;
        I2CBus bus = null;
        try {
            bus = I2CFactory.getInstance(I2CBus.BUS_1);
        } catch (UnsupportedBusNumberException e) {
            System.out.println(e.getLocalizedMessage());
        }
        device = bus.getDevice(bmp180_i2cAddr);
        this.oss = oss;
    }

    /**
     * @return the device
     */
    public I2CDevice getDevice() {
        return device;
    }

    public byte[][] readCalibarationValuesRaw(){
        Word word = new Word(this.device);
        int start = 0xAA;
        byte[][] calValues = new byte[11][2];
        for (int i = 0; i < 22; i += 2) {
            calValues[i / 2] = word.readBytes(start + i, 2);
        }
        return calValues;
    }

    public byte[] readTempRaw() throws IOException {
        Word bytesToInts = new Word(this.device);
        byte signal = 0x2E;
        device.write(writeAddress, signal);
        try {
            TimeUnit.MILLISECONDS.wait(5);
        } catch (InterruptedException e) {
            System.out.println("Could not wait 5ms, idk why");
            e.printStackTrace();
        }
        return bytesToInts.readBytes(0xF6, 2);
    }

    public byte[] readPressureRaw() throws IOException {
        Word bytesToInts = new Word(this.device);
        byte signal = (byte) (0x34 + oss.getVal() << 6);
        device.write(writeAddress, signal);
        try {
            TimeUnit.MILLISECONDS.wait(oss.getTime());
        } catch (InterruptedException e) {
            System.out.println("Could not wait Xms, idk why");
            e.printStackTrace();
        }
        return bytesToInts.readBytes(0xF6, 3);
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
