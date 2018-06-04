package sia20.myPi;

import com.pi4j.io.i2c.I2CDevice;
import java.io.IOException;
import java.util.BitSet;

public class Word {
    private Sensor device;

    public Word(Sensor dev) {
        device = dev;
    }

    public static String byteToBinaryPadded(byte val) {
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
        for (int i = 0; i < nrAddr; i++) {
            bytes[i] = (byte) (device.read(regAddr + i) & 0xff);
        }
        return bytes;
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
