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
    private final int whoAmIAddr = 0x75;
    private final int whoAmIValueDefualt = 0x71;
    // register addresses for 9250 first address is 0x3B all others is pos(in data)
    // + this address(0x3B)//
    public final String[] data = { "accXH", "accXL", "accYH", "accYL", "accZH", "accZL", "tempH", "tempL", "gyrXH",
            "gyrXL", "gyrYH", "gyrYL", "gyrZH", "gyrZL" };
    // register addresses for the magnetometer first address is 0x03 all others is
    // pos(in data) + this address(0x0B)//
    public final String[] dataMag = { "magXL", "magXH", "magYL", "magYH", "magZL", "magZH" };

    public MPU9250my() {
        I2CBus bus = null;
        try {
            bus = I2CFactory.getInstance(I2CBus.BUS_1);
        } catch (I2CFactory.UnsupportedBusNumberException e) {
            System.out.println(e.getLocalizedMessage());
        }catch(IOException e){
            System.out.println(e.getLocalizedMessage());
        }
        try{
            device = bus.getDevice(sensAddr);
        }catch(IOException e){
            System.out.println(e.getLocalizedMessage());
        }
    }

    private long[] readData() throws IOException {
        long[] res = new long[data.length];
        for (int i = 0; i < data.length; i++) {
            res[i] = device.read(startAddr + i);
        }
        return res;
    }

    private long[] readMag() throws IOException {
        long[] res = new long[dataMag.length];
        for (int i = 0; i < dataMag.length; i++) {
            res[i] = device.read(startAddrMag + i);
        }
        return res;
    }

    public long[][] readAll() throws IOException {
        long[][] res = new long[2][];

        res[0] = readData();
        res[1] = readMag();

        return res;
    }

    public void whoAmI(){
        int res = 0;
        int counter = 0;
        boolean done = false;
        while(!done){
            try {
                res = device.read(whoAmIAddr);
                done = true;
            } catch (IOException e) {
                System.out.println("Cannot read whoAmI MPU9250");
                System.out.println(e.getLocalizedMessage());
                done = false;
            }
            System.out.print("Attemt: ");
            System.out.print(counter);
            if (done) {
                System.out.println(" successful!");
            }else{
                System.out.println(" not successful!");
            }
        }
        if (res==whoAmIValueDefualt){
            System.out.println("I'm mpu9250");
        }else{
            System.out.println("Who am I? i don't know");
            System.out.print("I got this number, but thats not really me: ");
            System.out.println(res);
        }
    }
}
