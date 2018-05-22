package sia20.myPi;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

public class MPU9250my {
    private I2CDevice device;
    private int sensAddr = 0x68;
    private final int startAddr = 0x3B;
    private final int startAddrMag = 0x03;
    //register addresses for 9250 first address is 0x3B all others is pos(in data) + this address(0x3B)//
    public final String[] data = {"accXH", "accXL", "accYH", "accYL", "accZH", "accZL", "tempH", "tempL", "gyrXH", "gyrXL", "gyrYH", "gyrYL", "gyrZH", "gyrZL"};
    //register addresses for the magnetometer first address is 0x03 all others is pos(in data) + this address(0x0B)//
    public final String[] dataMag = {"magXL", "magXH", "magYL", "magYH", "magZL", "magZH"};

    public MPU9250my()throws IOException {
        final int bmp180_i2cAddr = 0x77;
        I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_0);
        device = bus.getDevice(sensAddr);
    }

    private long[] readData()throws IOException{
        long[] res = new long[data.length];
        for (int i = 0; i < data.length; i++) {
            res[i] = device.read(startAddr+i);
        }
        return res;
    }

    private long[] readMag() throws IOException{
        long[] res = new long[dataMag.length];
        for (int i = 0; i < dataMag.length; i++) {
            res[i] = device.read(startAddrMag + i);
        }
        return res;
    }

    public long[][] readAll() throws IOException{
        long[][] res = new long[2][];

        res[0] = readData();
        res[1] = readMag();

        return res;
    }
}
