package sia20.myPi;

import com.pi4j.io.i2c.I2CDevice;

import java.io.IOException;

public class Word {
    I2CDevice device;
    public Word(I2CDevice dev){
        device = dev;
    }

    public byte[] readBytes(int regAddr, int nrAddr){
        byte[] bytes = new byte[nrAddr];
        try {
            for (int i = 0; i < nrAddr; i++) {
                bytes[i] = (byte)(device.read(regAddr+i) & 0xff);
            }
        }catch (IOException e){
            System.out.println("Cannot read bytes in regestry!");
            System.out.println(e.getLocalizedMessage());
        }
        return bytes;
    }

    public short combToShort(byte msb, byte lsb){
        msb = (byte)(msb & 0xff);
        lsb = (byte)(lsb & 0xff);
        short res = (short)(msb << 8);
        res = (short)(res | lsb);
        return res;
    }

    public int combToInt(byte msb, byte lsb){
        msb = (byte)(msb & 0xff); 
        lsb = (byte)(lsb & 0xff);
        int res = (int)(msb << 8);
        res = (int)(res | lsb);
        return res;
    }

    public long combToLong(byte msb, byte lsb){
        msb = (byte)(msb & 0xff); 
        lsb = (byte)(lsb & 0xff);
        long res = (long)(msb << 8);
        res = (long)(res | lsb);
        return res; 
    }

    public long combToLong(byte msb, byte lsb, byte xlsb, int oss){
        msb = (byte)(msb & 0xff); 
        lsb = (byte)(lsb & 0xff);
        xlsb = (byte)(xlsb & 0xff);
        long res = (msb << 16);
        res = (res | (lsb << 8));
        res = (res | xlsb);
        res = (res >> (8-oss));
        return res;
    }
}
