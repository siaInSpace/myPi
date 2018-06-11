package sia20.myPi;

class MPU9250magMaster {

    private MPU9250 master;
    Word word;

    MPU9250magMaster(MPU9250 mpu9250){
        master = mpu9250;
        word = new Word(master);
        enableMaster();
    }

    private void enableMaster(){
        int int_config = 0X37;
        int i2c_mst_ctrl = 0x24;
        int user_ctrl = 0x6A;

        master.write(int_config, (byte)0x00); //Disables bypass mode
        master.write(i2c_mst_ctrl, (byte)0x5D); //configures the i2c (clock speed etc.)
        master.write(user_ctrl, (byte)0x20); //Enables i2c master
        master.write(0x38, (byte)0x00); //disable int?

        byte[] data = {master.read(int_config), master.read(i2c_mst_ctrl), master.read(user_ctrl)};
        for (byte d: data){
            System.out.println(d);
        }
    }

    void halvorCommandStuff(){ //Almost the same as DisableMaster, might be to enable byPassMode
        master.write(0x6A, (byte)0x01);
        master.write(0x37,(byte)0x32);
        master.write(0x38, (byte)0x01);

        Sensor s = new Sensor(0x0C);
        System.out.println(s.read(0x00));
    }

    void configureSlave(boolean read, int regAddress, int length){
        int slv0_address = 37;
        int slv0_reg = 38;
        int slv0_ctrl = 39;
        int address;
        if (read){
            address = 0b10001100; //MSb is 1 thus transfer is a read
        }else {
            address = 0b00001100; //MSb is 0 thus transfer is a write
        }
        int config = 0b1001; //0001; //config = enabled, no byte swap, only read and write, odd grouping.
        config = (config<<4) & (length & 0x0F); //configures to read the specified number of bytes

        master.write(slv0_ctrl, (byte)0x00); //disables slave while configuring it
        master.write(slv0_address, (byte)address); //sets the address for the slave device
        master.write(slv0_reg, (byte)regAddress); //sets which register to read from
        master.write(slv0_ctrl, (byte)config); //Sets configuration of slave
    }

    byte[] readRaw(int regAddress){
        master.write(37, (byte)0b10001100);
        master.write(38, (byte)regAddress);
        master.write(39, (byte)0b10010110);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return word.readBytes(73, 6);
    }

    private byte[] readMaster(int length){
        int extSensData00Address = 0x49;
        return word.readBytes(extSensData00Address, length);
    }

    void whoAmI(){
        enableMaster();
        configureSlave(true, 0x00, 1);
        byte[] data = readMaster(1);
        for (byte d : data) {
            System.out.println("I should be 72, I am: " + d);
        }
    }

    byte[] readDataRaw(){
        enableMaster();
        configureSlave(true, 0x03, 6);
        return readMaster(6);
    }


    private void delay(int ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
