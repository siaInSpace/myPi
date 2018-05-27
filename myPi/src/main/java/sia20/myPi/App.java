package sia20.myPi;

import java.io.IOException;
/**
 * Hello world!
 *
 */
public class App 
{

    private void run() throws IOException{
        BMP180my bmp = new BMP180my(BMP180my.Oss.STANDARD);
        Word word = new Word(bmp.getDevice());
        byte[][] calValsBMP = bmp.readCalibarationValuesRaw();
        for (int i = 0; i < 11; i++) {
            System.out.print(calValsBMP[i][0]);
            System.out.println(" | ");
            System.out.println(calValsBMP[i][1]);
            System.out.println(word.combToLong(calValsBMP[i][0], calValsBMP[i][1]));
        }
    }
    public static void main( String[] args ) {
        App app = new App();
        try {
            app.run();
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
    }
}
