package chunhodong.subway.path.domain;

public class DistanceFare {
    private static final int BASE_FARE = 1250;
    private DistancePolicy policy;
    private int distance;

    public DistanceFare(int distance) {
        this.distance = distance;
        this.policy = DistancePolicy.valueOfRange(distance);
    }

    public int getFare() {
        return policy.getFare(distance) + BASE_FARE;
    }
}
