package sia20.myPi;

import sia20.myPi.BMP180my;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.BitSet;

import sia20.myPi.Word;

/**
 * FileSaver
 */
public class FileSaver implements Runnable {
    private String path;
    private FileWriter fw;
    private BMP180my bmp;

    private String BitSetToBinaryString(byte[] bytes){
        BitSet b = BitSet.valueOf(bytes);
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


    public FileSaver(String pathName, String command, BMP180my bmp) {
        path = pathName;
        this.bmp = bmp;
        if (command.equals("bmp180saveCalVals")) {
            this.start();
        }
    }

    @Override
    public void run() {
        try {
            fw = new FileWriter(new File(path));
            byte[][] dat = bmp.readCalibarationValuesRaw();
            for (byte[] calVal : dat) {
                fw.write(BitSetToBinaryString(calVal));
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

    public void start() {
        Runnable task = () -> run();
        Thread t = new Thread(task, "saveToFile");
        t.start();
    }
}