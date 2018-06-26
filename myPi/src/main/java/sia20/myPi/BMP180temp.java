package sia20.myPi;

class BMP180temp extends Slave {

    BMP180temp(MPU9250 master){
        super(master, 0x77);
    }

    byte[] getRaw() {
        write(0xF4, (byte)0x2E);
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            System.out.println("Could not wait 5ms, idk why");
            e.printStackTrace();
        }
        return read(0xF6, 2);
    }
}
