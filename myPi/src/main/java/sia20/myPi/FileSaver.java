package sia20.myPi;

import sia20.myPi.BMP180my;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * FileSaver
 */
public class FileSaver implements Runnable {
    private String path;
    private FileWriter fw;
    private BMP180my bmp;

    public FileSaver(String pathName, String command, BMP180my bmp) {
        path = pathName;
        this.bmp = bmp;
        if (command.equals("bmp180saveCalVals")) {
            this.start();
        }
    }

    private String pad(byte val) {
        String padded = Integer.toBinaryString(val);
        for (int i = padded.length(); i < 8; i++) {
            padded = "0" + padded;
        }
        return padded + "\n";
    }

    @Override
    public void run() {
        try {
            fw = new FileWriter(new File(path));
            byte[][] dat = bmp.readCalibarationValuesRaw();
            for (byte[] calVal : dat) {
                fw.write(pad(calVal[0]));
                fw.write(pad(calVal[1]));
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

    public void start() {
        Runnable task = () -> run();
        Thread t = new Thread(task, "saveToFile");
        t.start();
    }
}