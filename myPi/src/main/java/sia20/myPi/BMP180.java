package sia20.myPi;

class BMP180 extends Slave{
    // calibration values
    /*private short AC1;
    private short AC2;
    private short AC3;
    private int AC4;
    private int AC5;
    private int AC6;
    private short B1;
    private short B2;
    private short MB;
    private short MC;
    private short MD;
*/

    // commonly used objects
    private BMP180temp temp;
    private BMP180pressure pressure;
    private BMP180pressure.Oss oss;

    BMP180(BMP180pressure.Oss osss, MPU9250 master) {
        super(master, 0b1110111);
        oss = osss;
    }
/*
    BMP180(BMP180pressure.Oss oss, String pathName) {
        this(oss);
        FileSaver fs = new FileSaver();
        fs.start(pathName, readCalibrationValuesRaw());
    }

    private byte[][] readCalibrationValuesRaw() {
        int start = 0xAA;
        byte[][] calValues = new byte[11][2];
        for (int i = 0; i < 22; i += 2) {
            calValues[i / 2] = word.readBytes(start + i, 2);
        }
        return calValues;
    }

    byte[][] readRawValues() {
        return new byte[][]{temp.getRaw(), pressure.getRaw()};
    }

*/

    void whoAmI(){
        int res = super.read(0xD0);
        System.out.println("I should be 0x55, I am: " + String.format("0x%02X", res));
    }


/*
    void whoAmI(){
        int slave0StartAddr = 0x25;
        master.write(slave0StartAddr, (byte)0b11110111);
        master.write(slave0StartAddr+1, (byte)0xD0);
        master.write(slave0StartAddr+2, (byte)0b10000001);
        byte res = master.read(0x49);
        System.out.println("I should be 0x55, I am: " + String.format("0x%02X", res));
    }
*/
}