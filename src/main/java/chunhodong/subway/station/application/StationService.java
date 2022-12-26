package chunhodong.subway.station.application;

import chunhodong.subway.station.dto.StationRequest;
import chunhodong.subway.station.dto.StationResponse;
import chunhodong.subway.station.persistence.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StationService {

    private final StationRepository stationRepository;

    public StationResponse createStation(StationRequest stationRequest) {
        return StationResponse.of(stationRepository.save(stationRequest.toStation()));
    }

    public List<StationResponse> findStations() {
        return stationRepository.findAll()
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }
}
