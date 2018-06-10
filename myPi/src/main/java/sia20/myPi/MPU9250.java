package sia20.myPi;

class MPU9250 extends Sensor{

    private MPU9250mag mag;
    MPU9250() {
        super(0x68);
        mag = new MPU9250mag(this);
    }

    byte[][] readRawValues(){
        byte[][] data = new byte[4][];
        data[0] = readAccRaw();
        data[1] = readTempRaw();
        data[2] = readGyrRaw();
        data[3] = mag.readDataRaw();
        return data;
    }

    private byte[] readAccRaw(){
        return word.readBytes(0x3b, 6);
    }

    private byte[] readTempRaw(){
        return word.readBytes(0x41, 2);
    }

    private byte[] readGyrRaw(){
        return word.readBytes(0x43, 6);

    }

    void whoAmI() {
        int res;
        int whoAmIValueDefault = 0x73;// usually 0x71, i don't know why this is 0x73 instead
        int whoAmIAddress = 0x75;
        res = read(whoAmIAddress);
        if (res == whoAmIValueDefault) {
            System.out.println("I'm mpu9250");
        } else {
            System.out.println("Who am I? i don't know");
            System.out.print("I got this number, but that's not really me: ");
            System.out.println(String.format("0x%02X", res));
        }
    }
}
