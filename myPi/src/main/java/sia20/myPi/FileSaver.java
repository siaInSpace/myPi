package sia20.myPi;

import sia20.myPi.BMP180my;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.BitSet;

/**
 * FileSaver: Saves data to a file
 * 
 * @author Sindre Aalhus
 * @version 0.4
 */
public class FileSaver implements Runnable {
    private String path;
    private FileWriter fw;
    private BMP180my bmp;
    private String command;

    public FileSaver(String pathName, String command, BMP180my bmp) {
        this.path = pathName;
        this.bmp = bmp;
        this.command = command;
        this.start();
    }

    public static void saveBytes(byte[] bytes) {
        try {
            FileOutputStream fo = new FileOutputStream(new File("./byteTest.txt"));
            fo.write(bytes);
            fo.flush();
            fo.close();
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }

    }

    private void saveCalVals() {
        try {
            fw = new FileWriter(new File(path));
            byte[][] dat = bmp.readCalibarationValuesRaw();
            for (byte[] calVal : dat) {
                fw.write(bytesToBinaryString(calVal));
                fw.write("\n");
            }
            fw.close();
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
        try {
            Thread.currentThread().join(5000);
        } catch (InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    private byte[] reverseArray(byte[] arr) {
        byte[] reversed = new byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            reversed[i] = arr[arr.length - (i + 1)];
        }
        return reversed;
    }

    private String bytesToBinaryString(byte[] bytes) {
        byte[] data = reverseArray(bytes);
        BitSet b = BitSet.valueOf(data);
        String res = "";
        for (int i = b.length(); i < 16; i++) {
            res += "0";
        }
        for (int i = b.length() - 1; i >= 0; i--) {
            if (b.get(i)) {
                res += "1";
            } else {
                res += "0";
            }
        }
        return res;
    }

    @Override
    public void run() {
        if (this.command.equals("bmp180saveTemp")) {

        } else if (this.command.equals("bmp180savePress")) {

        } else if (this.command.equals("bmp180saveCalVals")) {
            this.saveCalVals();
        }
    }

    public void start() {
        Runnable task = () -> run();
        Thread t = new Thread(task, "saveToFile");
        t.start();
    }
}