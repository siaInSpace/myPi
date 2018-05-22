package sia20.myPi;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;



public class BMP180my {
    private I2CDevice device;
    int writeAddress = 0xF4;
    Oss oss;

    //Calibration values


    public BMP180my(Oss oss)throws IOException{
        final int bmp180_i2cAddr = 0x77;
        I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_0);
        device = bus.getDevice(bmp180_i2cAddr);
        this.oss = oss;
    }

    public long[] readCalibarationValues() throws IOException{
        //long ref = {"AC1", "AC2", "AC3", "AC4", "AC5", "AC6", "B1", "B2", "MB", "MC", "MD"};
        //170 -> 191;
        //0xaa -> 0xBF;
        //170 171   172 173    174 175     176 177     178 179     180 181     182 183     184 185     186 187     188 189     190 191
        long[] vals = new long[11];
        int start = 0xAA;
        ArrayList<Word> words = new ArrayList<>();
        for (int i = 0; i < vals.length*2 ; i+=2) {
            vals[i] = readWord(start+i);
        }
        return vals;
    }

    private long readWord(int address0)throws IOException{
        int val0 = device.read(address0);
        int val1 = device.read(address0+1);
        val0 = val0 & 0xff;
        val0 = val0 <<8;
        val1 = val1 & 0xff;
        long res = val0 | val1;
        return  res;
    }

    public long readTemp()throws IOException{
        byte signal = 0x2E;
        device.write(writeAddress, signal);
        try {
            TimeUnit.MICROSECONDS.wait(4500);
        }catch (InterruptedException e){
            System.out.println("Could not wait 4.5ms, idk why");
            e.printStackTrace();
        }
        return readWord(0xF6);
    }

    public long readPressure() throws IOException{
        byte signal = (byte)(0x34 + oss.getVal()<<6);
        device.write(writeAddress, signal);
        try {
            TimeUnit.MICROSECONDS.wait(4500);
        }catch (InterruptedException e){
            System.out.println("Could not wait 4.5ms, idk why");
            e.printStackTrace();
        }
        int msb = device.read(0xF6);
        int lsb = device.read(0xF7);
        int xlsb = device.read(0x78);
        return (msb<<16+lsb<<8+xlsb)>>(8-oss.getVal());
    }

    public enum Oss{
        LOW_POWER(0), STANDARD(1), HIGHRES(2), ULTRAHIGHRES(3);

        private int id;

        Oss(int val) {
            this.id = val;
        }
        public int getVal(){
            return id;
        }
    }
}
