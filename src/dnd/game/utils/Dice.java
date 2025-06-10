package dnd.game.utils;

import java.util.Random;

public class Dice {
    private static final Random rand = new Random();

    public static int roll(int max) {
        return rand.nextInt(max + 1);
    }

    public static <T> T chooseRandom(java.util.List<T> list) {
        return list.get(rand.nextInt(list.size()));
    }
}
