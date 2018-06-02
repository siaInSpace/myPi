package sia20.myPi;

import java.io.*;
import java.util.BitSet;

/**
 * FileSaver: Saves data to a file
 * 
 * @author Sindre Aalhus
 * @version 0.5
 */
public class FileSaver implements Runnable {
    private String path;
    private byte[][] saveData;

    FileSaver() {
    }

    private void saveBytes(byte[][] bytes, String pathName) {
        File file = new File(pathName + ".txt");
        if (!file.exists()){
            try {
                file.mkdirs();
                file.createNewFile();
            }catch (IOException e){
                System.out.println("Cannot create file at:" + file.getAbsolutePath());
            }
        }
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            for (byte[] data: bytes) {
                bos.write(data);
            }
            bos.flush();
            bos.close();
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    byte[] readBytes(String pathName){
        byte[] data = null;
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(pathName)));
            data = new byte[bis.available()];
            for (int i = 0; bis.available() > 0; i++) {
                data[i] = (byte)bis.read();
            }
            bis.close();
        }catch (IOException e){
            System.out.println(e.getLocalizedMessage());
        }
        return data;
    }

    private byte[] reverseArray(byte[] arr) {
        byte[] reversed = new byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            reversed[i] = arr[arr.length - (i + 1)];
        }
        return reversed;
    }

    @Override
    public void run() {
        saveBytes(saveData, path);
    }

    void start(String pathName, byte[][] dataToSave) {
        path = pathName;
        saveData = dataToSave;
        Runnable task = () -> run();
        Thread t = new Thread(task, "saveToFile");
        t.start();
    }

    public String bytesToBinaryString(byte[] bytes) {
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
}