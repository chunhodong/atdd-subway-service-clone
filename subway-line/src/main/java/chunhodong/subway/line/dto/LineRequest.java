package chunhodong.subway.line.dto;

import chunhodong.subway.line.domain.LineColor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LineRequest {

    private Long id;
    private String name;
    private LineColor color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
}
