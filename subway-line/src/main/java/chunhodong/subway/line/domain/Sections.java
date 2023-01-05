package chunhodong.subway.line.domain;


import chunhodong.subway.line.exception.LineException;
import chunhodong.subway.station.domain.Station;
import io.jsonwebtoken.lang.Collections;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static chunhodong.subway.line.exception.LineExceptionCode.NONE_REGISTED_SECTION;
import static chunhodong.subway.line.exception.LineExceptionCode.SINGLE_SECTION;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public Sections(Section section) {
        if (section == null) {
            return;
        }
        sections.add(section);
    }

    public void addSection(Section section) {
        if (section == null) return;
        List<Station> stations = getStations();
        boolean isUpStationExisted = isExistsStation(stations, section.getUpStation());
        boolean isDownStationExisted = isExistsStation(stations, section.getDownStation());
        validateSection(isUpStationExisted, isDownStationExisted, stations);
        rebuildStation(isUpStationExisted, isDownStationExisted, section);
        sections.add(section);
    }

    private void rebuildStation(boolean isUpStationExisted, boolean isDownStationExisted, Section section) {
        rebuildUpStation(isUpStationExisted, section);
        rebuildDownStation(isDownStationExisted, section);
    }

    private boolean isExistsStation(List<Station> stations, Station station) {
        return stations.stream().anyMatch(s -> s.equals(station));
    }

    private void rebuildUpStation(boolean isUpStationExisted, Section newSection) {
        if (isUpStationExisted) {
            sections.stream()
                    .filter(section -> section.getUpStation().equals(newSection.getUpStation()))
                    .findFirst()
                    .ifPresent(section -> section.splitUpStation(newSection.getDownStation(), newSection.getDistance()));
        }
    }

    private void rebuildDownStation(boolean isDownStationExisted, Section section) {
        if (isDownStationExisted) {
            sections.stream()
                    .filter(it -> it.getDownStation().equals(section.getDownStation()))
                    .findFirst()
                    .ifPresent(it -> it.splitDownStation(section.getUpStation(), section.getDistance()));
        }
    }

    private void validateSection(boolean isUpStationExisted, boolean isDownStationExisted, List<Station> stations) {
        if (Collections.isEmpty(stations)) {
            throw new RuntimeException("등록된 역이 없습니다");
        }
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
        if (!isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    public Station findUpStation() {
        return getUpStations().stream()
                .filter(station -> !getDownStations().contains(station))
                .findFirst()
                .orElse(null);
    }

    private List<Station> getUpStations() {
        return sections.stream().map(section -> section.getUpStation())
                .collect(Collectors.toList());
    }

    private List<Station> getDownStations() {
        return sections.stream().map(section -> section.getDownStation())
                .collect(Collectors.toList());
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }
        Station upStation = findUpStation();
        List<Station> stations = new ArrayList<>();
        stations.add(upStation);
        addNextStation(stations, upStation);
        return stations;
    }

    private void addNextStation(List<Station> stations, Station station) {
        while (station != null) {
            Station nextStation = getNextStation(station);
            addStation(stations, nextStation);
            station = nextStation;
        }
    }

    private Station getNextStation(Station finalDownStation) {
        return sections.stream()
                .filter(it -> it.getUpStation().equals(finalDownStation))
                .findFirst()
                .map(section -> section.getDownStation())
                .orElse(null);
    }

    private void addStation(List<Station> stations, Station station) {
        if (station == null) {
            return;
        }
        stations.add(station);
    }

    public void removeStation(Station station) {
        if (sections.size() <= 1) {
            throw new LineException(SINGLE_SECTION);
        }
        Section upLineStation = getUpLineStation(station);
        Section downLineStation = getDownLineStation(station);
        validateSection(upLineStation, downLineStation);
        rebuildSection(upLineStation, downLineStation);
    }

    private void validateSection(Section upLineStation, Section downLineStation) {
        if (Objects.isNull(upLineStation) && Objects.isNull(downLineStation)) {
            throw new LineException(NONE_REGISTED_SECTION);
        }
    }

    private void removeSection(Section section) {
        if (Objects.isNull(section)) {
            return;
        }
        sections.remove(section);
    }

    private void rebuildSection(Section upLineSection, Section downLineSection) {
        if (Objects.isNull(upLineSection)) {
            removeSection(downLineSection);
            return;
        }
        if (Objects.isNull(downLineSection)) {
            removeSection(upLineSection);
            return;
        }
        removeSection(upLineSection);
        downLineSection.modifyDownStation(upLineSection);
    }

    private Section getUpLineStation(Station findStation) {
        return sections.stream()
                .filter(station -> station.getUpStation().equals(findStation))
                .findFirst().orElse(null);
    }

    private Section getDownLineStation(Station findStation) {
        return sections.stream()
                .filter(station -> station.getDownStation().equals(findStation))
                .findFirst().orElse(null);
    }

    public List<Section> getSections() {
        return sections;
    }
}
