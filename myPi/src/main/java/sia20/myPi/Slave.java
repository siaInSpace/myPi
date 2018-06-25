package sia20.myPi;

public class Slave {
    MPU9250 master;
    int address;

    Slave(MPU9250 mast, int addr){
        master = mast;
        address = addr;
    }

    byte read(int register){
        int slave0Address = 0x25;
        int slave0Register = 0x26;
        int slave0Ctrl = 0x27;
        master.write(slave0Address, (byte)(0xFF & address));
        master.write(slave0Register, (byte)register);
        master.write(slave0Ctrl, (byte)0b10000001);
        return master.read(0x49);
    }

    void write(int register, byte data){
        int slave0Address = 0x25;
        int slave0Register = 0x26;
        int slave0Ctrl = 0x27;
        int slave0DataOut = 0x63;
        master.write(slave0Ctrl, (byte)0x00); //disable slave while configure
        master.write(slave0Address, (byte)(0x7F & address));
        master.write(slave0Register, (byte)register);
        master.write(slave0DataOut, data);
        master.write(slave0Ctrl, (byte)0x01);
    }

}
