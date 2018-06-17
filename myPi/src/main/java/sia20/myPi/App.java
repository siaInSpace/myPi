package sia20.myPi;

import java.io.File;
import java.util.Scanner;

public class App {
    private BMP180 bmp;
    private MPU9250 mpu;
    private MPU9250magMaster mag;
    private BMP180Slave bmp180Slave;

    private App() {
        //bmp = new BMP180(Oss.STANDARD, "data/CalibrationValues/bmp180calValuesRaw"); bmp180 is disconnected
        mpu = new MPU9250();
        mag = new MPU9250magMaster(mpu);
        bmp180Slave = new BMP180Slave(mpu);
    }

    private void menu() {
        System.out.println("What would you like to do?");
        //System.out.println("1: Save raw temp and raw pressure from bmp180");
        //System.out.println("2: Read raw data files");
        System.out.println("1: whoAmI");
        System.out.println("2: save mpu data");
        System.out.println("3: read saved mpu data");
        System.out.println("4: mpuMag whoAmI");
        System.out.println("5: read extData from mpu");
        System.out.println("q: quit");
    }

    /*
    private void saveBmpValuesRaw(){
        FileSaver fs = new FileSaver();
        fs.start("data/measurements/" + System.nanoTime(), bmp.readRawValues());
    }
    */


    private void readSavedDataRaw(){
        FileReader fr = new FileReader();
        File[] files = (new File("data/measurements").listFiles());
        byte[][] data = new byte[files.length][];
        for (int i = 0; i < files.length; i++) {
            data[i] = fr.readBytes(files[i]);
        }
        for (byte[] bytes : data) {
            for (byte byt : bytes) {
                System.out.print(byt + " ");
            }
            System.out.println();
        }
        //Data is structured as {AccXH, AccXL, AccYH, AccYL, AccZH, AccZL, TempH, TempL, GyrXH, GyrXL,
        //                              GyrYH, GyrZH, GyrZL, MagXL, MagXH, MagYL, MagYH, MagZL, MagZH}
    }

    void whoAmI(){
        System.out.println("MPU9250");
        mpu.whoAmI();
        System.out.println("BMP180");
        bmp180Slave.whoAmI();
    }

    private void saveMpuData(){
        FileSaver fs = new FileSaver();
        fs.start("data/measurements/" + System.nanoTime(), mpu.readRawValues());
    }
    private void run() {
        Scanner in = new Scanner(System.in);
        String choice;
        do {
            menu();
            choice = in.nextLine().toUpperCase();
            switch (choice) {
            /*case "1":
                saveBmpValuesRaw();
                break;
            case "2":
                readSavedBmpValuesRaw();
                break;
            */
            case "1":
                whoAmI();
                break;
            case "2":
                saveMpuData();
                break;
            case "3":
                readSavedDataRaw();
                break;
            case "4":
                mag.whoAmI();
                break;
            case "5":
                mpu.readExtData(8);
                break;
            case "6":
                mpu.enableBypass();
            case "Q":
                in.close();
                System.out.println("Quitting!");
                break;
            default:
                System.out.println("Please select a valid input!");
                break;
            }
        } while (!choice.equals("Q"));
    }

    public static void main(String[] args) {
        App app = new App();
        app.run();

    }
}
