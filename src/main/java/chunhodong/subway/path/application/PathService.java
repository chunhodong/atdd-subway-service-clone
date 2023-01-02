package chunhodong.subway.path.application;

import chunhodong.subway.line.application.LineService;
import chunhodong.subway.path.domain.PathFare;
import chunhodong.subway.path.domain.PathFinder;
import chunhodong.subway.path.domain.SectionEdge;
import chunhodong.subway.path.dto.PathResponse;
import chunhodong.subway.station.application.StationService;
import chunhodong.subway.station.domain.Station;
import lombok.RequiredArgsConstructor;
import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PathService {
    private StationService stationService;
    private LineService lineService;

    public PathResponse findShortestPath(long sourceId, long targetId, Integer age) {
        Station source = stationService.findStationById(sourceId);
        Station target = stationService.findStationById(targetId);
        GraphPath<Station, SectionEdge> path = new PathFinder(null).getShortestPath(source, target);
        List<Long> lineIds = path.getEdgeList().stream().map(SectionEdge::getLineId).collect(Collectors.toList());
        int lineFare = lineService.findMaxFare(lineIds);
        int distance = (int) path.getWeight();
        int pathFare = new PathFare(lineFare, distance, age).getFare();
        return PathResponse.of(path.getVertexList(), distance, pathFare);
    }
}

