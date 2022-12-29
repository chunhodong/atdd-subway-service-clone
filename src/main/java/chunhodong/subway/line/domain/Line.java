package chunhodong.subway.line.domain;

import chunhodong.subway.common.BaseEntity;
import chunhodong.subway.line.exception.LineException;
import chunhodong.subway.line.exception.LineExceptionCode;
import chunhodong.subway.station.domain.Station;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LineColor color;
    @Embedded
    private Sections sections = new Sections();

    protected Line() {
    }

    protected Line(LineBuilder builder) {
        validateLine(builder.name, builder.color);
        this.id = builder.id;
        this.name = builder.name;
        this.color = builder.color;
        this.sections = new Sections(Section.builder()
                .line(this)
                .upStation(builder.section.getUpStation())
                .downStation(builder.section.getDownStation())
                .distance(builder.section.getDistance())
                .build());
    }

    private void validateLine(String name, LineColor color) {
        if (Strings.isEmpty(name)) {
            throw new LineException(LineExceptionCode.EMPTY_NAME);
        }
        if (Objects.isNull(color)) {
            throw new LineException(LineExceptionCode.EMPTY_COLOR);
        }
    }

    public void addSection(Section section) {
        sections.addSection(section);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public static LineBuilder builder() {
        return new LineBuilder();
    }

    public static class LineBuilder {
        private Long id;
        private String name;
        private LineColor color;
        private Section section;

        public LineBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public LineBuilder name(String name) {
            this.name = name;
            return this;
        }

        public LineBuilder color(LineColor color) {
            this.color = color;
            return this;
        }

        public LineBuilder section(Section section) {
            this.section = section;
            return this;
        }

        public Line build() {
            return new Line(this);
        }

    }
}
