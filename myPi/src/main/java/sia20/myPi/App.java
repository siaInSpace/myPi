package sia20.myPi;

import java.util.Scanner;

import sia20.myPi.BMP180my.Oss;

public class App {
    private BMP180my bmp;
    private MPU9250my mpu;

    public App() {
        bmp = new BMP180my(Oss.STANDARD, "./test.txt");
        mpu = new MPU9250my();
    }

    private void menu() {
        System.out.println("What would you like to do?");
        System.out.println("1: get caliration values for BMP180");
        System.out.println("2: get who am I value from MPU9250");
        System.out.println("3: print calibration values in binary from BMP180");
        System.out.println("q: quit");
    }

    private void bmpCalVals() {
        byte[][] data = bmp.readCalibarationValuesRaw();
        bmp.combineCalibrationValues(data);
        bmp.printCalVals();
    }

    private void bmpCalValsBin() {
        bmp.printCalValsBinary();
    }

    private void whoAmIMPU9250() {
        MPU9250my mpu = new MPU9250my();
        mpu.whoAmI();
    }

    private void saveTempRawAsByteUsingFiles() {
        byte[][] data = bmp.readCalibarationValuesRaw();
        FileSaver.saveBytes(data[0]);

    }

    private void run() {
        Scanner in = new Scanner(System.in);
        String choice;
        do {
            menu();
            choice = in.nextLine().toUpperCase();
            switch (choice) {
            case "1":
                bmpCalVals();
                break;
            case "2":
                whoAmIMPU9250();
                break;
            case "3":
                bmpCalValsBin();
                break;
            case "4":
                saveTempRawAsByteUsingFiles();
                break;
            case "Q":
                in.close();
                System.out.println("Quitting!");
                break;
            default:
                break;
            }
        } while (!choice.equals("Q"));
    }

    public static void main(String[] args) {
        App app = new App();
        app.run();

    }
}
