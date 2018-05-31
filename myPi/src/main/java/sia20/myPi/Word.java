package sia20.myPi;

import com.pi4j.io.i2c.I2CDevice;
G
import java.io.IOException;
import java.util.BitSet;

public class Word {
    I2CDevice device;

    public Word(I2CDevice dev) {
        device = dev;
    }

    public static String byteToBinaryPadded(byte val) {

        // 1010110011100 11110110
        // 0000000000000 11111111: 21, (21-8 = 13)->
        // 101011: 6, (6-8 = -2) ->
        // 0: 1, (1-8 = -7) ->
        // 10110011: 8 (8-8 = 0) ->
        int vals = val & 0xFF;

        String padded = Integer.toBinaryString(vals);
        String fitSize = padded.substring(padded.length() - 8);
        for (int i = fitSize.length(); i < 8; i++) {
            padded = "0" + padded;
        }
        return padded.substring(padded.length() - 8) + "\n";
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

    private byte[] reverseArray(byte[] arr) {
        byte[] reversed = new byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            reversed[i] = arr[arr.length - (i + 1)];
        }
        return reversed;
    }

    public short toShort(byte[] vals) {
        return (short) (BitSet.valueOf(reverseArray(vals)).toLongArray()[0]);
    }

    public int toInt(byte[] vals) {
        return (int) (BitSet.valueOf(reverseArray(vals)).toLongArray()[0]);
    }

    public long toLong(byte[] vals) {
        return (BitSet.valueOf(reverseArray(vals)).toLongArray()[0]);
    }
}
