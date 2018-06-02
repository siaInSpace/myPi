package sia20.myPi;

import java.util.Scanner;

import sia20.myPi.BMP180my.Oss;

public class App {
    private BMP180my bmp;
    private MPU9250my mpu;

    private App() {
        bmp = new BMP180my(Oss.STANDARD, "./data/bmp180calValuesRaw.txt");
        mpu = new MPU9250my();
    }

    private void menu() {
        System.out.println("What would you like to do?");
        System.out.println("1: Read raw calibration values from bmp180 file");
        System.out.println("q: quit");
    }

    private void readSavedRawData() {
        FileSaver fs = new FileSaver();
        byte[] data = fs.readBytes("./data/bmp180calValuesRaw.txt");
        for (byte word : data) {
            System.out.println(word);
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
                readSavedRawData();
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
