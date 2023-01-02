package chunhodong.subway.path.domain;

import java.util.Arrays;

public enum AgePolicy {
    KIDS(0, 6, 350, 1),
    CHILD(6, 13, 350, 0.5),
    TEEN(13, 19, 350, 0.2),
    ADULT(19, Integer.MAX_VALUE, 0, 0);

    private int minAge;
    private int maxAge;
    private int deductionFare;
    private double discountRate;

    AgePolicy(int minAge, int maxAge, int deductionFare, double discountRate) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.deductionFare = deductionFare;
        this.discountRate = discountRate;
    }

    public static AgePolicy valueOfAge(Integer age) {
        if (age == null) {
            return ADULT;
        }
        return Arrays.stream(AgePolicy.values())
                .filter(it -> it.isRange(age))
                .findFirst()
                .orElse(ADULT);
    }

    private boolean isRange(int age) {
        return this.minAge <= age && age < this.maxAge;
    }

    public int getFare(int fare) {
        return (int) ((fare - this.deductionFare) * (1 - this.discountRate));
    }
}
