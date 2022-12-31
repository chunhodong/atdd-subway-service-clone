package chunhodong.subway.path.application;

import chunhodong.subway.line.application.LineService;
import chunhodong.subway.path.domain.PathFinder;
import chunhodong.subway.path.dto.PathResponse;
import chunhodong.subway.station.application.StationService;
import chunhodong.subway.station.domain.Station;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PathService {
    private StationService stationService;
    private LineService lineService;

    public PathResponse findShortestPath(long sourceId, long targetId, Integer age) {
        Station source = stationService.findStationById(sourceId);
        Station target = stationService.findStationById(targetId);

        new PathFinder(null).getShortestPath(source, target);
        return null;
    }
}

