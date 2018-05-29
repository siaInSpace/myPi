package sia20.myPi;

import sia20.myPi.BMP180my;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * FileSaver
 */
public class FileSaver implements Runnable{
    private String path;
    private String[] data;
    private PrintWriter pw;
    private BMP180my bmp;

    public FileSaver(String pathName, String command, BMP180my bmp){
        path = pathName;
        this.bmp = bmp;
        if (command.equals("bmp180saveCalVals")){
            this.start();
        }
    }

    @Override
    public void run() {
        try {
            pw = new PrintWriter(new File(path));    
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
        byte[][] dat = bmp.readCalibarationValuesRaw();
        for (byte[] calVal : dat) {
            pw.println(calVal[0]);
            pw.println(calVal[1]);
        }
        pw.close();
        try {
            Thread.currentThread().join(5000);
        } catch (InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void start(){
        Runnable task = () -> run();
        Thread t = new Thread(task, "saveToFile");
        t.start();       
    }
}