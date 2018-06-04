package sia20.myPi;

public class MPU9250mag extends Sensor {

    MPU9250mag(){
        super(0x0C);
    }


    public void whoAmI(){
        byte data = read(0x00);
        System.out.println("I should be 72, I am: " + data);
    }
}
