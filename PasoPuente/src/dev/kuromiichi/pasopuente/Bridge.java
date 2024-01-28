package dev.kuromiichi.pasopuente;

public class Bridge {
    public enum Direction { LR, RL }

    int maxWeight;
    int maxPeople;
    int maxPeoplePerDirection;
    int peopleCrossingLR = 0;
    int peopleCrossingRL = 0;
    int weight = 0;

    public Bridge(int maxWeight, int maxPeople, int maxPeoplePerDirection) {
        this.maxWeight = maxWeight;
        this.maxPeople = maxPeople;
        this.maxPeoplePerDirection = maxPeoplePerDirection;
    }

    public int getPeopleCrossing(Direction direction) {
        return (direction == Direction.LR) ? peopleCrossingLR : peopleCrossingRL;
    }

    public boolean isFull() {
        return peopleCrossingLR + peopleCrossingRL >= maxPeople;
    }

    public void addPerson(Direction direction, int weight) {
        if (direction == Direction.LR) {
            peopleCrossingLR++;
        } else {
            peopleCrossingRL++;
        }
        this.weight += weight;
    }

    public void removePerson(Direction direction, int weight) {
        if (direction == Direction.LR) {
            peopleCrossingLR--;
        } else {
            peopleCrossingRL--;
        }
        this.weight -= weight;
    }
}
