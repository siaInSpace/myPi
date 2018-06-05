package sia20.myPi;

public class MPU9250mag {

    private MPU9250 master;
    Word word;
    MPU9250mag(MPU9250 mpu9250){
        master = mpu9250;
        word = new Word(master);
    }

    void enableMaster(){
        int int_config = 55;
        int i2c_mst_ctrl = 36;
        int user_ctrl = 106;

        master.write(int_config, (byte)0x00); //Disables bypass mode
        master.write(i2c_mst_ctrl, (byte)0x5D); //configures the i2c (clock speed etc.)
        master.write(user_ctrl, (byte)0x20); //Enables i2c master
    }

    void configureSlave(boolean read, int regAddress, int length){
        int slv0_addr = 37;
        int slv0_reg = 38;
        int slv0_ctrl = 39;
        int addr;

        if (read){
            addr = 0b10001100; //MSb is 1 thus transfer is a read
        }else {
            addr = 0b00001100; //MSb is 0 thus transfer is a write
        }

        int config = 0b1001; //config = enabled, no byte swap, only read and write, odd grouping.
        config = (config<<4) & (length & 0xF); //configures to read the specified number of bytes

        master.write(slv0_ctrl, (byte)0x00); //disables slave while configuring it
        master.write(slv0_addr, (byte)addr); //sets the address for the slave device
        master.write(slv0_reg, (byte)regAddress); //sets which register to read from
        master.write(slv0_ctrl, (byte)config); //Sets configuration of slave
    }

    byte[] read(int regAddress){
        master.write(37, (byte)0b10001100);
        master.write(38, (byte)regAddress);
        master.write(39, (byte)0b10010110);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        byte[] data = word.readBytes(73, 6);
        return data;
    }

    byte[] readMaster(int length){
        int ext_sens_data_00_address = 73;
        byte[] data = new byte[length];
        for (int i = 0; i < length; i++) {
            data[i] = master.read(ext_sens_data_00_address+i);
        }
        return data;
    }


    void whoAmI(){
        master.write(37, (byte)0b10001100);
        master.write(38, (byte)0x01);
        master.write(39, (byte)0b10000001);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        byte[] data = word.readBytes(73, 1);
        for (byte d : data) {
            System.out.println("I should be 72, I am: " + d);
        }
    }
}
