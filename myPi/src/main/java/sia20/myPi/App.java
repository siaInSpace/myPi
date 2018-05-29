package sia20.myPi;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Scanner;
public class App {
    private void getCalValsBMP180() {

        BMP180my bmp = null;
        try {
            bmp = new BMP180my(BMP180my.Oss.STANDARD);
        } catch (IOException e) {
            System.out.println("Cannot create new object of class BMP180my");
            System.out.println(e.getLocalizedMessage());
        }
        byte[][] v = bmp.readCalibarationValuesRaw();
        prntCalVal(v, new Word(bmp.getDevice()));

    }

    private String padByte(byte byt){

        String res = Integer.toBinaryString(byt & 0xFF);
        for (int i = res.length(); i < 8; i++) {
            res = "0" + res;
        }
        return res;
    }

    private String padByte(int byt){
        String res = Integer.toBinaryString(byt & 0xFFFF);
        for (int i = res.length(); i < 16; i++) {
            res = "0" + res;
        }
        return res;
    } 

    private String padByte(short byt){
        String res = Integer.toBinaryString(byt & 0xFFFF);
        for (int i = res.length(); i < 16; i++) {
            res = "0" + res;
        }
        return res;
    }

    private String padByte(long byt){
        String res = Long.toBinaryString(byt & 0xFFFF);
        for (int i = res.length(); i < 16; i++) {
            res = "0" + res;
        }
        return res;
    }



    private void printCalVals(byte[][] vals, Word word) {
        for (int i = 0; i < 11; i++) {
            System.out.print(padByte(vals[i][0]));
            System.out.print("");
            System.out.println(padByte(vals[i][1]));
            System.out.println(padByte(word.combToLong(vals[i][0], vals[i][1])));
            System.out.println();
        }
    }

    private void prntCalVal(byte[][] v, Word w){
        for (int i = 0; i < 11; i++) {
            if (i!= 3 || i != 4 || i!= 5){
                //signed short
                short val = w.combToShort(v[i][0], v[i][1]);
                System.out.println(padByte(val));
            }else{
                //unsigned short -> int
                int val = w.combToInt(v[i][0], v[i][1]);
                System.out.println(padByte(val));
            }
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
                getCalValsBMP180();
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
