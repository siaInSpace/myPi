package sia20.myPi;

import java.io.IOException;
import java.util.Scanner;

import com.pi4j.io.i2c.*;
//import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

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
        byte[][] calValsBMP = null;
        try {
             calValsBMP = bmp.readCalibarationValuesRaw();
        } catch (IOException e) {
            System.out.println("Cannot get calibration values!");
            System.out.println(e.getLocalizedMessage());    
        }
        for (int i = 0; i < 11; i++) {
            System.out.print(calValsBMP[i][0]);
            System.out.println(" | ");
            System.out.println(calValsBMP[i][1]);
            System.out.println(word.combToLong(calValsBMP[i][0], calValsBMP[i][1]));
        }
    }

    private void menu() {
        System.out.println("What would you like to do?");
        System.out.println("1: get busses");
        System.out.println("2: get caliration values for BMP180");
        System.out.println("3: quit");
    }

    private void run() {
        Scanner in = new Scanner(System.in);
        String choice;
        do {
            menu();
            choice = in.nextLine();
            switch (choice) {
            case "1":
                busses();
                break;
            case "2":
                calValsBMP180();
            case "3":
                in.close();
                System.out.println("Quitting!");
                break;

            default:
                break;
            }

        } while (!choice.equals("3"));
    }

    public static void main(String[] args) {
        App app = new App();
        app.run();

    }
}
