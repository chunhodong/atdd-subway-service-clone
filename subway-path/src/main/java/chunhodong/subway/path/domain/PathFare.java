package chunhodong.subway.path.domain;

public class PathFare {
    private int distance;
    private Integer age;
    private int lineFare;

    public PathFare(int lineFare, int distance, Integer age) {
        this.lineFare = lineFare;
        this.age = age;
        this.distance = distance;
    }

    public int getFare() {
        int distanceFare = getDistanceFare(distance);
        return AgePolicy.valueOfAge(age).getFare(lineFare + distanceFare);
    }

    private int getDistanceFare(int distance) {
        return new DistanceFare(distance).getFare();

    }
}

