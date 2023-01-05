package chunhodong.subway.line.application;

import chunhodong.subway.line.domain.Line;
import chunhodong.subway.line.domain.Section;
import chunhodong.subway.line.dto.LineRequest;
import chunhodong.subway.line.dto.LineResponse;
import chunhodong.subway.line.dto.SectionRequest;
import chunhodong.subway.line.exception.LineException;
import chunhodong.subway.line.exception.LineExceptionCode;
import chunhodong.subway.line.persistence.LineRepository;
import chunhodong.subway.station.application.StationService;
import chunhodong.subway.station.domain.Station;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    @Transactional
    public LineResponse createLine(LineRequest lineRequest) {
        Station upStation = stationService.findStationById(lineRequest.getUpStationId());
        Station downStation = stationService.findStationById(lineRequest.getDownStationId());
        Section section = Section.builder()
                .upStation(upStation)
                .downStation(downStation)
                .distance(lineRequest.getDistance())
                .build();
        Line line = Line.builder()
                .name(lineRequest.getName())
                .color(lineRequest.getColor())
                .section(section)
                .build();
        return LineResponse.of(lineRepository.save(line));
    }

    public LineResponse findLine(Long lineId) {
        return LineResponse.of(findLineById(lineId));
    }

    public List<LineResponse> findLines() {
        return lineRepository.findAll()
                .stream()
                .map(LineResponse::of).collect(Collectors.toList());
    }

    private List<Line> findLines(List<Long> lineIds) {
        return lineRepository.findAllById(lineIds)
                .stream()
                .collect(Collectors.toList());
    }

    public int findMaxFare(List<Long> lineIds) {
        return findLines(lineIds)
                .stream()
                .mapToInt(value -> value.getFare())
                .max().orElse(0);
    }

    @Transactional
    public void addSection(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        Section section = Section.builder()
                .line(line)
                .upStation(upStation)
                .downStation(downStation)
                .distance(request.getDistance())
                .build();
        line.addSection(section);
    }

    private Line findLineById(Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new LineException(LineExceptionCode.NONE_EXISTS_LINE));
    }

    @Transactional
    public void removeSection(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findStationById(stationId);
        line.removeSection(station);
    }
}
