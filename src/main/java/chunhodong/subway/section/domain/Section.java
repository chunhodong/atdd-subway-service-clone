package chunhodong.subway.section.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "line_id", nullable = false)
    private Long lineId;
    @Column(name = "up_station_id", nullable = false)
    private Long upStationId;
    @Column(name = "down_station_id", nullable = false)
    private Long downStationId;
    private long distance;

    public Section(Long lineId, Long upStationId, Long downStationId, long distance) {
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }
}
