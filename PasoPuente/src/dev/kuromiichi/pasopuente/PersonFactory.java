package dev.kuromiichi.pasopuente;

import java.util.Random;

public class PersonFactory {
    private PersonFactory() {
    }

    private static final Random random = new Random();
    private static int id = 1;

    public static Person createPerson(Bridge bridge) {
        return new Person(
            id++,
            random.nextInt(50, 121),
            Bridge.Direction.values()[random.nextInt(2)],
            bridge
        );
    }
}
