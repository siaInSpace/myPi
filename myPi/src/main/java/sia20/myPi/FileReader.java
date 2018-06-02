package sia20.myPi;

// data is saved in a structure as: {bmpTempMSB, bmpTempLSB, bmpPresMSB, bmpPresLSB, bmpPresXLSB...}

import java.io.*;

public class FileReader {

    byte[] readBytes(String pathName){
        File f = new File(pathName);
        BufferedInputStream bis = null;
        byte[] data = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(f));
        }catch (FileNotFoundException e){
            System.out.println("File not found at: " + f.getAbsolutePath());
            System.out.println(e.getLocalizedMessage());
        }
        try {
            data = new byte[bis.available()];
            for (int i = 0; i < data.length; i++) {
                data[i] = (byte)bis.read();
            }
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
