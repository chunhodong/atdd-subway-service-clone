package chunhodong.subway.line.domain;

import chunhodong.subway.line.exception.LineException;
import chunhodong.subway.line.exception.LineExceptionCode;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    private LineColor color;

    protected Line(String name, LineColor color) {
        validateLine(name, color);
        this.name = name;
        this.color = color;
    }

    protected Line() {

    }

    public static Line of(String name, LineColor color) {
        return new Line(name,color);
    }

    private void validateLine(String name, LineColor color) {
        if (Strings.isEmpty(name)) {
            throw new LineException(LineExceptionCode.EMPTY_NAME);
        }
        if (Objects.isNull(color)) {
            throw new LineException(LineExceptionCode.EMPTY_COLOR);
        }
    }

    public void modifyName(String name){
        validateLine(name,color);
        this.name = name;
    }
}
