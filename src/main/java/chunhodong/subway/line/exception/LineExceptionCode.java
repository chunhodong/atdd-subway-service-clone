package chunhodong.subway.line.exception;

public enum LineExceptionCode {

    NONE_EXISTS_LINE("없는 노선입니다"),
    EMPTY_NAME("노선이름을 입력해야 합니다"),
    EMPTY_COLOR("노선색상을 입력해야 합니다");

    private String message;

    LineExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
