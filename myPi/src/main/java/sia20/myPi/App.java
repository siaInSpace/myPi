package sia20.myPi;

import java.io.File;
import java.util.Scanner;

import sia20.myPi.BMP180pressure.Oss;

public class App {
    private BMP180 bmp;
    private MPU9250 mpu;
    private MPU9250mag mag;

    private App() {
        bmp = new BMP180(Oss.STANDARD, "data/CalibrationValues/bmp180calValuesRaw");
        mpu = new MPU9250();
        mag = new MPU9250mag(mpu);
    }

    private void menu() {
        System.out.println("What would you like to do?");
        System.out.println("1: Save raw temp and raw pressure from bmp180");
        System.out.println("2: Read raw data files");
        System.out.println("3: mpuMag whoAmI");
        System.out.println("4: disable master test");
        System.out.println("5: read Acc");
        System.out.println("q: quit");
    }

    private void saveBmpValuesRaw(){
        FileSaver fs = new FileSaver();
        fs.start("data/measurements/" + System.nanoTime(), bmp.readRawValues());
    }

    private void readSavedBmpValuesRaw(){
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
    }

    private void run() {
        Scanner in = new Scanner(System.in);
        String choice;
        do {
            menu();
            choice = in.nextLine().toUpperCase();
            switch (choice) {
            case "1":
                saveBmpValuesRaw();
                break;
            case "2":
                readSavedBmpValuesRaw();
                break;
            case "3":
                mpu.magWhoAmI();
                break;
            case "4":
                mpu.magDisableMaster();
                break;
            case "5":
                mpu.readAcc();
                break;
            case "6":
                mag.newDeviceTest();
                break;
            case "7":
                mag.halvorCommandStuff();
                break;
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
