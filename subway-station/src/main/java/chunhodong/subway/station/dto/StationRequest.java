package chunhodong.subway.station.dto;

import chunhodong.subway.station.domain.Station;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class StationRequest {

    private String name;

    public StationRequest(String name) {
        this.name = name;
    }

    public Station toStation() {
        return Station.of(name);
    }
}
