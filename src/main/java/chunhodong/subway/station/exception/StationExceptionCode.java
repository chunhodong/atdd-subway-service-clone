package chunhodong.subway.station.exception;

public enum StationExceptionCode {

    EMPTY_NAME("역 이름을 입력해야 합니다"),
    NONE_EXISTS_STATION("없는 역입니다");

    private String message;

    StationExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
