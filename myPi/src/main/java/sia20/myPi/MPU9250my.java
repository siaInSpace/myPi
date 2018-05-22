package sia20.myPi;

public class MPU9250my {
    private int sensAddr = 0x68;
    private final int startAddr = 0x3B;
    //registrer addresses for 9250, first address is 0x3B all others is pos + this address//
    private final String[] data = {"accXH", "accXL", "accYH", "accYL", "accZH", "accZL", "tempH", "tempL", "gyrXH", "gyrXL", "gyrYH", "gyrYL", "gyrZH", "gyrZL"};

}
