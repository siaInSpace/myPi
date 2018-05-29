package sia20.myPi;

import java.io.IOException;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App {
    private void calValsBMP180() {
        BMP180my bmp = null;
        try {
            bmp = new BMP180my(BMP180my.Oss.STANDARD);
        } catch (IOException e) {
            System.out.println("Cannot create new object of class BMP180my");
            System.out.println(e.getLocalizedMessage());
        }
        byte[][] v = bmp.readCalibarationValuesRaw();
        printCalVals(v, new Word(bmp.getDevice()));
    }

    private void printCalVals(byte[][] vals, Word word) {
        for (int i = 0; i < 11; i++) {
            System.out.print(String.format("%08b", vals[i][0] & 0xff));
            System.out.print("");
            System.out.println(String.format("%08b", vals[i][1] & 0xff));
            System.out.println(String.format("%016b", word.combToLong(vals[i][0], vals[i][1])));
        }
    }

    private void menu() {
        System.out.println("What would you like to do?");
        System.out.println("1: get caliration values for BMP180");
        System.out.println("2: get who am I value from MPU9250");
        System.out.println("q: quit");
    }

    private void whoAmIMPU9250() {
        MPU9250my mpu = new MPU9250my();
        mpu.whoAmI();
    }

    private void run() {
        Scanner in = new Scanner(System.in);
        String choice;
        do {
            menu();
            choice = in.nextLine().toUpperCase();
            switch (choice) {
            case "1":
                calValsBMP180();
                break;
            case "2":
                whoAmIMPU9250();
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
