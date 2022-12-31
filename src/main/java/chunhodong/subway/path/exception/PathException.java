package chunhodong.subway.path.exception;

public class PathException extends RuntimeException{

    public PathException(PathExceptionCode code){
        super(code.getMessage());
    }
}
