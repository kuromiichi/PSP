package dev.kuromiichi.pasopuente;

public class Main {
    private static final int MAX_WEIGHT = 400;
    private static final int MAX_PEOPLE = 4;
    private static final int MAX_PEOPLE_PER_DIRECTION = 3;
    private static final int TOTAL_PEOPLE = 200;

    public static void main(String[] args) throws InterruptedException {
        Bridge bridge = new Bridge(MAX_WEIGHT, MAX_PEOPLE, MAX_PEOPLE_PER_DIRECTION);

        Thread[] people = new Thread[TOTAL_PEOPLE];
        for (int i = 0; i < TOTAL_PEOPLE; i++) {
            people[i] = new Thread(PersonFactory.createPerson(bridge));
            people[i].start();
        }

        for (int i = 0; i < TOTAL_PEOPLE; i++) {
            people[i].join();
        }

        System.out.println("All people have crossed the bridge");
    }
}
