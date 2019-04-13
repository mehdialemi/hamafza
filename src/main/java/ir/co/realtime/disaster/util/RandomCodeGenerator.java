package ir.co.realtime.disaster.util;

import java.util.Random;

public class RandomCodeGenerator {

    private static Random random = new Random();

    public static int createNewCode() {
        return random.nextInt(9900) + 1000;
    }
}