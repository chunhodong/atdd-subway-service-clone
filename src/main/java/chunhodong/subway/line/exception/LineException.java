package chunhodong.subway.line.exception;

public class LineException extends RuntimeException{
    public LineException(LineExceptionCode code){
        super(code.getMessage());
    }
}
