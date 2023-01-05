package chunhodong.subway.path.domain;

import chunhodong.subway.line.domain.Line;
import chunhodong.subway.path.exception.PathException;
import chunhodong.subway.station.domain.Station;
import io.jsonwebtoken.lang.Collections;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.List;
import java.util.Objects;

import static chunhodong.subway.path.exception.PathExceptionCode.*;

public class PathFinder {
    private List<Line> lines;

    public PathFinder(List<Line> lines) {
        validateLines(lines);
        this.lines = lines;
    }

    private void validateLines(List<Line> lines) {
        if (Collections.isEmpty(lines)) {
            throw new PathException(EMPTY_LINES);
        }
    }

    public GraphPath<Station, SectionEdge> getShortestPath(Station source, Station target) {
        PathGraph pathGraph = new PathGraph(lines, SectionEdge.class);
        validateStations(source, target, pathGraph);
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(pathGraph);
        return dijkstraShortestPath.getPath(source, target);
    }

    private void validateStations(Station source, Station target, PathGraph pathGraph) {
        validateEqualStation(source, target);
        validateExistsStation(source, target, pathGraph);
    }

    private void validateEqualStation(Station sourceStation, Station targetStation) {
        if (Objects.equals(sourceStation, targetStation)) {
            throw new PathException(EQUALS_STATION);
        }
    }

    private void validateExistsStation(Station sourceStation, Station targetStation, PathGraph pathGraph) {
        if (!pathGraph.containsVertex(sourceStation) || !pathGraph.containsVertex(targetStation)) {
            throw new PathException(NOT_CONTAIN_STATION);
        }
    }
}
