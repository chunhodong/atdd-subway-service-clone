package chunhodong.subway.station.dto;

import chunhodong.subway.station.domain.Station;
import lombok.Getter;

@Getter
public class StationResponse {
    private Long id;
    private String name;

    private StationResponse(Station station) {
        this.id = station.getId();
        this.name = station.getName();
    }

    public static StationResponse of(Station station) {
        return new StationResponse(station);
    }
}
