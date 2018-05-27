package sia20.myPi;

import java.io.IOException;
import java.util.Scanner;

import com.pi4j.io.i2c.*;
//import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

/**
 * Hello world!
 *
 */
public class App {
    private void busses() {
        // find available busses
        for (int number = I2CBus.BUS_0; number <= I2CBus.BUS_17; ++number) {
            try {
                @SuppressWarnings("unused")
                I2CBus bus = I2CFactory.getInstance(number);
                System.out.println("Supported I2C bus: " + number);
            } catch (IOException exception) {
                System.out.println("I/O error on I2C bus: " + number);
            } catch (Exception exception) {
                System.out.println("Unsupported I2C bus: " + number);
            }
        }
    }

    private void calValsBMP180() {
        BMP180my bmp = null;
        try {
            bmp = new BMP180my(BMP180my.Oss.STANDARD);
        } catch (IOException e) {
            System.out.println("Cannot create new object of class BMP180my");
            System.out.println(e.getLocalizedMessage());
        }
        Word word = new Word(bmp.getDevice());

        byte[][] v = bmp.readCalibarationValuesRaw();
        // printCalVals(v, word);
    }

    private void printCalVals(byte[][] vals, Word word) {
        for (int i = 0; i < 11; i++) {
            System.out.print(vals[i][0]);
            System.out.println(" | ");
            System.out.println(vals[i][1]);
            System.out.println(word.combToLong(vals[i][0], vals[i][1]));
        }
    }

    private void readFirstCalVal(){
        I2CBus bus = null;
        try {
            bus = I2CFactory.getInstance(I2CBus.BUS_1);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }catch (UnsupportedBusNumberException e){
            System.out.println(e.getLocalizedMessage());
        }
        for (int i = 0; i < 0xFF; i++) {
            I2CDevice dev = null;
            try {
                dev = bus.getDevice(i);
            } catch (IOException e) {
                System.out.println("Could not get device on adress: " + Integer.toString(i));
            }
            for (int j = 0; j < 0xFF; i++) {
                boolean s = true;
                int res = 0;
                try {
                   res = dev.read(j);
                } catch (IOException e) {
                    s = false;
                }
                if (s){
                    System.out.println("Found valid result on");
                    System.out.println("devAddr: " + Integer.toString(i));
                    System.out.println("regAddr: " + Integer.toString(j));
                    System.out.println("Result was: " + Integer.toString(res));
                }
            }
        }

    }

    private void menu() {
        System.out.println("What would you like to do?");
        System.out.println("1: get busses");
        System.out.println("2: get caliration values for BMP180");
        System.out.println("3: get first calibration values from BMP180");
        System.out.println("q: quit");
    }

    private void run() {
        Scanner in = new Scanner(System.in);
        String choice;
        do {
            menu();
            choice = in.nextLine().toUpperCase();
            switch (choice) {
            case "1":
                busses();
                break;
            case "2":
                calValsBMP180();
                break;
            case "3":
                readFirstCalVal();
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
