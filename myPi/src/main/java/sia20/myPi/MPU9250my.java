package sia20.myPi;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

public class MPU9250my {
    private I2CDevice device;
    private int sensAddr = 0x68;
    private final int startAddr = 0x3B;
    //register addresses for 9250, first address is 0x3B all others is pos + this address//
    public final String[] data = {"accXH", "accXL", "accYH", "accYL", "accZH", "accZL", "tempH", "tempL", "gyrXH", "gyrXL", "gyrYH", "gyrYL", "gyrZH", "gyrZL"};

    public MPU9250my()throws IOException {
        final int bmp180_i2cAddr = 0x77;
        I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_0);
        device = bus.getDevice(sensAddr);
    }

    public long[] readData()throws IOException{
        long[] res = new long[data.length];
        for (int i = 0; i < data.length; i++) {
            res[i] = device.read(sensAddr+i);
        }
        return res;
    }
}
