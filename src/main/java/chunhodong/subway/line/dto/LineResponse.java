package chunhodong.subway.line.dto;

import chunhodong.subway.line.domain.Line;
import chunhodong.subway.line.domain.LineColor;
import chunhodong.subway.station.dto.StationResponse;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class LineResponse {
    private Long id;
    private String name;
    private LineColor color;
    private List<StationResponse> stations;

    private LineResponse(Line line) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
        this.stations = line.getStations().stream().map(StationResponse::of).collect(Collectors.toList());
    }

    public static LineResponse of(Line line) {
        return new LineResponse(line);
    }

}
