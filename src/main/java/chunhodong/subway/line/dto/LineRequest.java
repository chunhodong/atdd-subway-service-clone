package chunhodong.subway.line.dto;

import chunhodong.subway.line.domain.Line;
import chunhodong.subway.line.domain.LineColor;
import lombok.Getter;

@Getter
public class LineRequest {
    private String name;
    private LineColor color;

    public LineRequest(String name, LineColor color) {
        this.name = name;
        this.color = color;
    }

    public Line toLine() {
        return Line.of(name, color);
    }
}
