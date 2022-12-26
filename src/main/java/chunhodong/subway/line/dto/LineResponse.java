package chunhodong.subway.line.dto;

import chunhodong.subway.line.domain.Line;
import chunhodong.subway.line.domain.LineColor;
import lombok.Getter;

@Getter
public class LineResponse {
    private Long id;
    private String name;
    private LineColor color;

    private LineResponse(Line line) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
    }

    public static LineResponse of(Line line) {
        return new LineResponse(line);
    }
}
