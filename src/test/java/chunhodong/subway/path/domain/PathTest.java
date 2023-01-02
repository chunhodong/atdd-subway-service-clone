package chunhodong.subway.path.domain;

import chunhodong.subway.line.domain.Line;
import chunhodong.subway.line.domain.LineColor;
import chunhodong.subway.line.domain.Section;
import chunhodong.subway.path.exception.PathException;
import chunhodong.subway.station.domain.Station;
import net.jqwik.api.Arbitraries;
import org.jgrapht.GraphPath;
import org.junit.jupiter.api.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

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
                            .getShortestPath(Station.of("강남역"), Station.of("삼성역"));
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
                            .getShortestPath(Station.of("강남역"), Station.of("강남역"));
                }).isInstanceOf(PathException.class)
                        .hasMessageContaining("동일한 역 입니다");

            }
        }

        @TestFactory
        @DisplayName("노선중에서 가장 짦은 역들을 반환")
        Stream<DynamicTest> findShortestPath() {
            Section 송파구간 = Section.builder().upStation(Station.of("송파")).downStation(Station.of("가락시장")).distance(10).build();
            Line 칠호선 = Line.builder().name("칠호선").color(LineColor.RED).section(송파구간).build();

            Section 도곡대치구간 = Section.builder().upStation(Station.of("도곡")).downStation(Station.of("대치")).distance(10).build();
            Line 삼호선 = Line.builder().name("삼호선").color(LineColor.PURPLE).section(도곡대치구간).build();

            Section 도곡구룡구간 = Section.builder().upStation(Station.of("도곡")).downStation(Station.of("구룡")).distance(10).build();
            Line 분당선 = Line.builder().name("분당선").color(LineColor.BLUE).section(도곡구룡구간).build();
            HashMap<String, Station> 지하철역 = new HashMap<>();
            return Stream.of(
                    DynamicTest.dynamicTest("역 생성", () -> {
                        지하철역.put("대치", Station.of("대치"));
                        지하철역.put("학여울", Station.of("학여울"));
                        지하철역.put("대청", Station.of("대청"));
                        지하철역.put("일원", Station.of("일원"));
                        지하철역.put("수서", Station.of("수서"));
                        지하철역.put("가락시장", Station.of("가락시장"));
                        지하철역.put("구룡", Station.of("구룡"));
                        지하철역.put("개포동", Station.of("개포동"));
                        지하철역.put("대모산입구", Station.of("대모산입구"));
                    }),
                    DynamicTest.dynamicTest("구간추가", () -> {
                        삼호선.addSection(Section.builder().upStation(지하철역.get("대치")).downStation(지하철역.get("학여울")).distance(3).build());
                        삼호선.addSection(Section.builder().upStation(지하철역.get("학여울")).downStation(지하철역.get("대청")).distance(3).build());
                        삼호선.addSection(Section.builder().upStation(지하철역.get("대청")).downStation(지하철역.get("일원")).distance(3).build());
                        삼호선.addSection(Section.builder().upStation(지하철역.get("일원")).downStation(지하철역.get("수서")).distance(3).build());
                        삼호선.addSection(Section.builder().upStation(지하철역.get("수서")).downStation(지하철역.get("가락시장")).distance(3).build());

                        분당선.addSection(Section.builder().upStation(지하철역.get("구룡")).downStation(지하철역.get("개포동")).distance(1).build());
                        분당선.addSection(Section.builder().upStation(지하철역.get("개포동")).downStation(지하철역.get("대모산입구")).distance(1).build());
                        분당선.addSection(Section.builder().upStation(지하철역.get("대모산입구")).downStation(지하철역.get("수서")).distance(1).build());

                    }),
                    DynamicTest.dynamicTest("최단거리조회", () -> {
                        GraphPath<Station, SectionEdge> path = new PathFinder(List.of(칠호선, 삼호선, 분당선))
                                .getShortestPath(Station.of("도곡"), Station.of("가락시장"));
                        List<Station> stations = path.getVertexList();

                        assertAll(
                                () -> assertThat(stations).hasSize(6),
                                () -> assertThat(stations).containsExactly(
                                        Station.of("도곡"),
                                        Station.of("구룡"),
                                        Station.of("개포동"),
                                        Station.of("대모산입구"),
                                        Station.of("수서"),
                                        Station.of("가락시장")));

                    }));
        }
    }

    @Nested
    @DisplayName("요금조회는")
    class DescribeFindFare {
        @Nested
        @DisplayName("거리가 10km이내면 기본운임1250원")
        class ContextWithLessThen10 {
            @Test
            void returnBaseFare() {
                int distance = Arbitraries.integers().between(0, 10).sample();

                assertThat(new DistanceFare(distance).getFare()).isEqualTo(1250);
            }
        }

        @Nested
        @DisplayName("거리가 10km초과부터 50km까지 5km마다 100원씩 기본요금에 추가")
        class ContextWithGreaterThan10LessThen50 {
            @Test
            void returnBaseFare() {
                int distance = Arbitraries.integers().between(10, 50).sample();
                int extraFee = (int) ((Math.floor(((distance - 10) - 1) / 5.0) + 1) * 100);

                assertThat(new DistanceFare(distance).getFare()).isEqualTo(1250 + extraFee);
            }
        }

        @Nested
        @DisplayName("거리가 50km초과부터 8km마다 100원씩 기본요금에 추가")
        class ContextWithGreaterThan50{
            @Test
            void returnBaseFare() {
                int distance = Arbitraries.integers().greaterOrEqual(51).sample();
                int extraFee = (int) ((Math.floor(((distance - 50) - 1) / 8.0) + 1) * 100);

                assertThat(new DistanceFare(distance).getFare()).isEqualTo(1250 + extraFee);
            }
        }

    }


}
