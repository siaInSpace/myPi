package sia20.myPi;

public class BMP180temp extends Sensor {

    BMP180temp(){
        super(0x77);
    }

    byte[] getRaw() {
        write(0xF4, (byte)0x2E);
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            System.out.println("Could not wait 5ms, idk why");
            e.printStackTrace();
        }
        return word.readBytes(0xF6, 2);
    }
}
