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
    private BMP180temp temp;
    private BMP180pressure pressure;
    private BMP180pressure.Oss oss;

    BMP180(BMP180pressure.Oss osss, MPU9250 master) {
        super(master, 0x77);
        oss = osss;
    }

    byte[][] readCalibrationValuesRaw() {
        byte[][] calValues = new byte[11][2];
        for (int i = 0; i < 11; i++) {
            calValues[i] = read(0xAA+(i*2), 2);
        }
        return calValues;
    }

    void whoAmI(){
        int res = super.read(0xD0);
        System.out.println("I should be 0x55, I am: " + String.format("0x%02X", res));
    }
}