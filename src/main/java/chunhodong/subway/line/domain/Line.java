package chunhodong.subway.line.domain;

import chunhodong.subway.line.exception.LineException;
import chunhodong.subway.line.exception.LineExceptionCode;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    private LineColor color;

    public Line(String name, LineColor color) {
        validateLine(name,color);
        this.name = name;
        this.color = color;
    }

    public Line() {

    }

    private void validateLine(String name,LineColor color) {
        if (Strings.isEmpty(name)) {
            throw new LineException(LineExceptionCode.EMPTY_NAME);
        }
        if(Objects.isNull(color)){
            throw new LineException(LineExceptionCode.EMPTY_COLOR);
        }
    }
}
