package chunhodong.subway.path.exception;

public enum PathExceptionCode {
    EMPTY_LINES("노선이 없습니다"),
    EQUALS_STATION("동일한 역 입니다"),
    NOT_CONTAIN_STATION("경로에 역이 존재하지 않습니다");
    private String message;

    PathExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
