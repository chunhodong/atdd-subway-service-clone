package chunhodong.subway.station.exception;

public class StationException extends RuntimeException{
    public StationException(StationExceptionCode code){
        super(code.getMessage());
    }
}
