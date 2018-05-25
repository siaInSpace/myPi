package sia20.myPi;

import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.jni.I2C;

import java.io.IOException;

public class Word {
    I2CDevice device;
    public Word(I2CDevice dev){
        device = dev;
    }

    public int[] readBytes(int regAddr, int nrAddr){
        int[] bytes = new int[nrAddr];
        try {
            for (int i = 0; i < nrAddr; i++) {
                bytes[i] = device.read(regAddr+i);
            }
        }catch (IOException e){
            System.out.println(e.getStackTrace());
        }
        return bytes;
    }

    public int combineBytes(int[] bytes){
        int res;
        if (bytes.length == 2){
            bytes[0] = bytes[0] & 0xff;
            bytes[0] = bytes[0] <<8;
            bytes[1] = bytes[1] & 0xff;
            res = bytes[0] + bytes[1];
        }else{
            System.out.println("Unexpected number of bytes: " + Integer.toString(bytes.length));
            res = -999999999;
        }
        return res;
    }

    public int combineBytes(int[] bytes, int oss) {
        int res;
        if (bytes.length == 0) {
            res = (bytes[0] << 16 + bytes[1] << 8 + bytes[2]) >> (8 - oss);
        }else{
            System.out.println("Unexpected number of bytes: " + Integer.toString(bytes.length));
            res = -999999999;
        }
        return res;
    }
}
