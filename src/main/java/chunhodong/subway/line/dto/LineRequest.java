package chunhodong.subway.line.dto;

import chunhodong.subway.line.domain.Line;
import chunhodong.subway.line.domain.LineColor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class LineRequest {

    private Long id;
    private String name;
    private LineColor color;

    public LineRequest(String name, LineColor color) {
        this.name = name;
        this.color = color;
    }

    public LineRequest(Long id, String name, LineColor color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Line toLine() {
        return Line.of(id, name, color);
    }
}
