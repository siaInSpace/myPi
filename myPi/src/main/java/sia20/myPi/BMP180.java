package sia20.myPi;

class BMP180 extends Sensor {
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

    BMP180(BMP180pressure.Oss oss) {
        super(0x77);
        temp = new BMP180temp();
        pressure = new BMP180pressure(oss);
    }

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
        byte[][] b = {temp.getRaw(), pressure.getRaw()};
        return b;
    }
}