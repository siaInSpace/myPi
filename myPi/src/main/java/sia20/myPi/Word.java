package sia20.myPi;

import com.pi4j.io.i2c.I2CDevice;

import java.io.IOException;

public class Word {
    I2CDevice device;

    public Word(I2CDevice dev) {
        device = dev;
    }

    public static String byteToBinaryPadded(byte val) { 
        String padded = Integer.toBinaryString(val & 0xff);
        String fitSize = "";
        try {
            fitSize = padded.substring(padded.length()-8, padded.length());
        } catch (IndexOutOfBoundsException e) {
        }
        for (int i = fitSize.length(); i < 8; i++) {
            padded = "0" + padded;
        }
        return padded.substring(padded.length()-8, padded.length()) + "\n";
    }

    public byte[] readBytes(int regAddr, int nrAddr) {
        byte[] bytes = new byte[nrAddr];
        try {
            for (int i = 0; i < nrAddr; i++) {
                bytes[i] = (byte) (device.read(regAddr + i) & 0xff);
            }
        } catch (IOException e) {
            System.out.println("Cannot read bytes in regestry!");
            System.out.println(e.getLocalizedMessage());
        }
        return bytes;
    }

    public short combToShort(byte msb, byte lsb) {
        return (short) combToLong(msb, lsb);
    }

    public int combToInt(byte msb, byte lsb) {
        return (int) combToLong(msb, lsb);
    }

    public long combToLong(byte msb, byte lsb) {
        return (((msb & 0xff) << 8) | (lsb & 0xff)) & 0xffff;
    }

    public long combToLong(byte msb, byte lsb, byte xlsb, int oss) {
        msb = (byte) (msb & 0xff);
        lsb = (byte) (lsb & 0xff);
        xlsb = (byte) (xlsb & 0xff);
        long res = (msb << 16);
        res = (res | (lsb << 8));
        res = (res | xlsb);
        res = (res >> (8 - oss));
        return res;
    }
}
