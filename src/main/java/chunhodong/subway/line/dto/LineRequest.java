package chunhodong.subway.line.dto;

import chunhodong.subway.line.domain.Line;
import chunhodong.subway.line.domain.LineColor;

public class LineRequest {
    private String name;
    private LineColor color;

    public LineRequest(String name, LineColor color) {
        this.name = name;
        this.color = color;
    }
}
