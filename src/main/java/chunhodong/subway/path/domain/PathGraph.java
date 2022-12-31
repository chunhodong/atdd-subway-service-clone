package chunhodong.subway.path.domain;

import chunhodong.subway.line.domain.Line;
import chunhodong.subway.line.domain.Section;
import chunhodong.subway.station.domain.Station;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;

public class PathGraph extends WeightedMultigraph<Station, SectionEdge> {
    public PathGraph(List<Line> lines, Class<? extends SectionEdge> edgeClass) {
        super(edgeClass);
        addVertexWith(lines);
        addEdge(lines);
    }

    public void addVertexWith(List<Line> lines) {
        lines.stream()
                .flatMap(line -> line.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(this::addVertex);
    }

    public void addEdge(List<Line> lines) {
        for (Line line : lines) {
            line.getSections().stream()
                    .forEach(section -> addEdge(section, line));
        }
    }

    private void addEdge(Section section, Line line) {
        SectionEdge sectionEdge = new SectionEdge(section, line.getId());
        addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
        setEdgeWeight(sectionEdge, section.getDistance());
    }
}
