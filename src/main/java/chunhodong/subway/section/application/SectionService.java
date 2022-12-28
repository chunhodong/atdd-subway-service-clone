package chunhodong.subway.section.application;

import chunhodong.subway.line.application.LineService;
import chunhodong.subway.section.dto.SectionResponse;
import chunhodong.subway.section.persistence.SectionRepository;
import chunhodong.subway.station.application.StationService;
import chunhodong.subway.section.dto.SectionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SectionService {
    private final LineService lineService;
    private final StationService stationService;
    private final SectionRepository sectionRepository;

    @Transactional
    public SectionResponse addSection(SectionRequest sectionRequest) {
        lineService.validateLine(sectionRequest.getLineId());
        stationService.validateStation(sectionRequest.getUpStationId());
        stationService.validateStation(sectionRequest.getDownStationId());
        return SectionResponse.of(sectionRepository.save(sectionRequest.toSection()));
    }
}
