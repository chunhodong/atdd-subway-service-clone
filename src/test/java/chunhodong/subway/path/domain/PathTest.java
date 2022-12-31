package chunhodong.subway.path.domain;

import chunhodong.subway.line.domain.Line;
import chunhodong.subway.line.domain.LineColor;
import chunhodong.subway.line.domain.Section;
import chunhodong.subway.path.exception.PathException;
import chunhodong.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Path 도메인테스트")
public class PathTest {

    @Nested
    @DisplayName("생성자는")
    class DescribeConstructPath {

        @Nested
        @DisplayName("노선이 없으면 예외발생")
        class ContextWithNullLine {
            @Test
            void throwsExeption() {
                assertThatThrownBy(() -> new PathFinder(null))
                        .isInstanceOf(PathException.class)
                        .hasMessageContaining("노선이 없습니다");
            }
        }

        @Nested
        @DisplayName("노선이 비었으면 예외발생")
        class ContextWithEmptyLine {
            @Test
            void throwsExeption() {
                assertThatThrownBy(() -> new PathFinder(Collections.EMPTY_LIST))
                        .isInstanceOf(PathException.class)
                        .hasMessageContaining("노선이 없습니다");
            }
        }
    }

    @Nested
    @DisplayName("최단거리 조회는")
    class DescribeFindShortestPath {

        @Nested
        @DisplayName("역이 노선에 없으면 예외발생")
        class ContextWithNoStationAtLine {
            private Line line;

            @BeforeEach
            void before() {
                Section section = Section.builder()
                        .upStation(Station.of("강동역"))
                        .downStation(Station.of("길동역"))
                        .build();
                line = Line.builder()
                        .name("5호선")
                        .color(LineColor.PURPLE)
                        .section(section)
                        .build();
            }

            @Test
            void throwsException() {
                assertThatThrownBy(() -> {
                    new PathFinder(List.of(line))
                            .getShortestPath(Station.of("강남역"),Station.of("삼성역"));
                }).isInstanceOf(PathException.class)
                        .hasMessageContaining("경로에 역이 존재하지 않습니다");

            }
        }

        @Nested
        @DisplayName("역이 같으면 예외발생")
        class ContextWithSameStation {
            private Line line;

            @BeforeEach
            void before() {
                Section section = Section.builder()
                        .upStation(Station.of("강동역"))
                        .downStation(Station.of("길동역"))
                        .build();
                line = Line.builder()
                        .name("5호선")
                        .color(LineColor.PURPLE)
                        .section(section)
                        .build();
            }

            @Test
            void throwsException() {
                assertThatThrownBy(() -> {
                    new PathFinder(List.of(line))
                            .getShortestPath(Station.of("강남역"),Station.of("강남역"));
                }).isInstanceOf(PathException.class)
                        .hasMessageContaining("동일한 역 입니다");

            }
        }
    }

}
