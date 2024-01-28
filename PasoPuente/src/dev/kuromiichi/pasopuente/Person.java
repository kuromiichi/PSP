package dev.kuromiichi.pasopuente;

import java.util.Random;

public class Person implements Runnable {
    private static final Random random = new Random();

    int id;
    int weight;
    Bridge.Direction direction;
    final Bridge bridge;

    public Person(int id, int weight, Bridge.Direction direction, Bridge bridge) {
        this.id = id;
        this.weight = weight;
        this.direction = direction;
        this.bridge = bridge;
    }

    @Override
    public void run() {
        long delay = random.nextLong(5000);
        try {
            Thread.sleep(delay);
            System.out.println("Person " + id + " arrived at bridge after " + delay + "ms");
            synchronized (bridge) {
                while (bridge.isFull()
                    || bridge.weight + weight > bridge.maxWeight
                    || bridge.getPeopleCrossing(direction) >= bridge.maxPeoplePerDirection
                ) {
                    bridge.wait();
                }
                bridge.addPerson(direction, weight);
                System.out.println("Person " + id + " who weighs " + weight
                    + "kg is crossing the bridge in direction " + direction.name()
                );
                System.out.println("People crossing LR: " + bridge.peopleCrossingLR);
                System.out.println("People crossing RL: " + bridge.peopleCrossingRL);
                System.out.println("Weight: " + bridge.weight);
            }
            long crossingTime = random.nextLong(1000);
            Thread.sleep(crossingTime);
            synchronized (bridge) {
                bridge.removePerson(direction, weight);
                System.out.println("Person " + id + " who weighs " + weight
                    + "kg left the bridge in direction " + direction.name()
                    + " after crossing for " + crossingTime + "ms"
                );
                bridge.notifyAll();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
