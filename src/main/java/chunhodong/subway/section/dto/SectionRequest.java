package chunhodong.subway.section.dto;

import chunhodong.subway.section.domain.Section;
import lombok.Getter;

@Getter
public class SectionRequest {

    private Long lineId;
    private Long upStationId;
    private Long downStationId;
    private long distance;

    public SectionRequest(Long lineId,Long upStationId,Long downStationId,long distance){
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Section toSection() {
        return new Section(lineId, upStationId, downStationId, distance);
    }
}
