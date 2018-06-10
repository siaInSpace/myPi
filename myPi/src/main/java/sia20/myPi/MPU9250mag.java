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

    void halvorCommandStuff(){
        master.write(0x6A, (byte)0x01);
        master.write(0x37,(byte)0x32);
        master.write(0x38, (byte)0x01);

        Sensor s = new Sensor(0x0C);
        System.out.println(s.read(0x00));
    }

    void disableMasterTest(){
        int int_config = 0x37;
        int i2c_mst_ctrl = 0x24;
        int user_ctrl = 0x6A;

        master.write(int_config, (byte)0x02); //Enable bypass mode
        //master.write(i2c_mst_ctrl, (byte)0x5D); //configures the i2c (clock speed etc.)
        master.write(user_ctrl, (byte)0x00); //Enables i2c master
        master.write(0x38, (byte)0x00); //disable int?
        byte data[] = {master.read(i2c_mst_ctrl), master.read(0x36), master.read(int_config), master.read(0x38)};
        for (int i = 0; i < data.length; i++) {
            System.out.println(data[i]);
        }
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

        int config = 0b10010001; //config = enabled, no byte swap, only read and write, odd grouping.
        //config = (config<<4) & (length & 0xF); //configures to read the specified number of bytes

        master.write(slv0_ctrl, (byte)0x00); //disables slave while configuring it
        delay(5);
        master.write(slv0_addr, (byte)addr); //sets the address for the slave device
        delay(5);
        master.write(slv0_reg, (byte)regAddress); //sets which register to read from
        delay(5);
        master.write(slv0_ctrl, (byte)config); //Sets configuration of slave
        delay(20);
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

    void newDeviceTest(){
        disableMasterTest();
        Sensor s = new Sensor(0x0C);
        System.out.println(s.read(0x00));
    }

    void whoAmI(){
        enableMaster();
        configureSlave(true, 0x00, 1);
        byte[] data = word.readBytes(73, 1);
        for (byte d : data) {
            System.out.println("I should be 72, I am: " + d);
        }
    }


    private void delay(int ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    void selfTest(){

        master.write(0x0C, (byte)0b01000000);
    }
}
