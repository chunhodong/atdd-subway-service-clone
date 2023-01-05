package chunhodong.subway.station.domain;

import chunhodong.subway.common.BaseEntity;
import chunhodong.subway.station.exception.StationException;
import chunhodong.subway.station.exception.StationExceptionCode;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
public class Station extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;

    public Station() {

    }

    protected Station(String name) {
        validateStation(name);
        this.name = name;
    }

    public static Station of(String name) {
        return new Station(name);
    }

    private void validateStation(String name) {
        if (Strings.isEmpty(name)) {
            throw new StationException(StationExceptionCode.EMPTY_NAME);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(id, station.id) && name.equals(station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
