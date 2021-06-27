package Utils;
import java.util.Random;
public class Util {

    public static int getRandomNumber(){
        Random random = new Random();
        return random.nextInt(1000);
    }
}