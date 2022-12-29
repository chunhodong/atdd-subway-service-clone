package chunhodong.subway.line.domain;

import chunhodong.subway.line.exception.LineException;
import chunhodong.subway.station.domain.Station;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Line도메인테스트")
public class LineTest {

    @Nested
    @DisplayName("생성자는")
    class DescribeCreateLine {

        @Nested
        @DisplayName("노선의 이름이 없으면 예외발생")
        class ContextWithNoName {
            @Test
            void throwsExeption() {
                assertThatThrownBy(() -> Line.builder().build())
                        .isInstanceOf(LineException.class)
                        .hasMessageContaining("노선이름을 입력해야 합니다");
            }
        }

        @Nested
        @DisplayName("노선의 색깔이 없으면 예외발생")
        class ContextWithNoColor {
            @Test
            void throwsExeption() {
                assertThatThrownBy(() -> Line.builder().name("2호선").build())
                        .isInstanceOf(LineException.class)
                        .hasMessageContaining("노선색상을 입력해야 합니다");
            }
        }

        @DisplayName("노선의 이름과 컬러를 입력하면 생성에 성공")
        @Test
        void returnLine() {
            assertThat(Line.builder().name("2호선").color(LineColor.BLUE).build()).isNotNull();
        }
    }

    @Nested
    @DisplayName("구간추가는")
    class DescribeAddSection {

        @Nested
        @DisplayName("기존에 존재하는 역이 없으면 예외발생")
        class ContextWithNoSameStation {

            private Line line;
            private Section section;

            @BeforeEach
            void before(){
                line = Line.builder()
                        .name("2호선")
                        .color(LineColor.BLUE)
                        .build();
                section = Section.builder().build();
            }

            @Test
            void throwsExeption() {
                assertThatThrownBy(() -> line.addSection(section))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessageContaining("등록된 역이 없습니다");
            }
        }

        @Nested
        @DisplayName("기존에 존재하는 역과 일치하는 역이 없으면 예외발생")
        class ContextWithNoColor {

            private Line line;
            private Section lineSection;
            private Section newSection;

            @BeforeEach
            void before(){
                lineSection = Section.builder()
                        .upStation(Station.of("강동역"))
                        .downStation(Station.of("길동역"))
                        .build();
                line = Line.builder()
                        .name("5호선")
                        .color(LineColor.PURPLE)
                        .section(lineSection)
                        .build();
                newSection = Section.builder()
                        .upStation(Station.of("강남역"))
                        .downStation(Station.of("잠실역"))
                        .build();
            }

            @Test
            void throwsExeption() {
                assertThatThrownBy(() -> line.addSection(newSection))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessageContaining("등록할 수 없는 구간 입니다.");
            }
        }

        @Nested
        @DisplayName("기존에 존재하는 상행역과 새로운 상행역이 일치할경우 새로운 구간이 크거나 같으면 예외발생")
        class ContextWithNewUpStationHasLongDistance {

            private Line line;
            private Section lineSection;
            private Section newSection;

            @BeforeEach
            void before(){
                lineSection = Section.builder()
                        .upStation(Station.of("강동역"))
                        .downStation(Station.of("명일역"))
                        .distance(10)
                        .build();
                line = Line.builder()
                        .name("5호선")
                        .color(LineColor.PURPLE)
                        .section(lineSection)
                        .build();
                newSection = Section.builder()
                        .upStation(Station.of("강동역"))
                        .downStation(Station.of("길동역"))
                        .distance(10)
                        .build();
            }

            @Test
            void throwsExeption() {
                assertThatThrownBy(() -> line.addSection(newSection))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessageContaining("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
            }
        }

        @Nested
        @DisplayName("기존에 존재하는 하행역과 새로운 하행역이 일치할경우 새로운 구간이 크거나 같으면 예외발생")
        class ContextWithNewDownStationHasLongDistance {

            private Line line;
            private Section lineSection;
            private Section newSection;

            @BeforeEach
            void before(){
                lineSection = Section.builder()
                        .upStation(Station.of("강동역"))
                        .downStation(Station.of("명일역"))
                        .distance(10)
                        .build();
                line = Line.builder()
                        .name("5호선")
                        .color(LineColor.PURPLE)
                        .section(lineSection)
                        .build();
                newSection = Section.builder()
                        .upStation(Station.of("길동역"))
                        .downStation(Station.of("명일역"))
                        .distance(10)
                        .build();
            }

            @Test
            void throwsExeption() {
                assertThatThrownBy(() -> line.addSection(newSection))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessageContaining("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
            }
        }

        @Nested
        @DisplayName("기존에 존재하는 상행역과 새로운 상행역이 일치할경우 새로운 구간이 기존구간에 포함")
        class ContextWithNewUpStationContains {

            private Line line;
            private Section lineSection;
            private Section newSection;

            @BeforeEach
            void before(){
                lineSection = Section.builder()
                        .upStation(Station.of("강동역"))
                        .downStation(Station.of("명일역"))
                        .distance(10)
                        .build();
                line = Line.builder()
                        .name("5호선")
                        .color(LineColor.PURPLE)
                        .section(lineSection)
                        .build();
                newSection = Section.builder()
                        .upStation(Station.of("강동역"))
                        .downStation(Station.of("길동역"))
                        .distance(3)
                        .build();
            }

            @Test
            void returnStations() {
                line.addSection(newSection);

                List<Station> stations = line.getStations();
                assertAll(
                        () -> assertThat(stations).hasSize(3),
                        () -> assertThat(stations.stream().map(Station::getName).collect(Collectors.toList()))
                                .contains("강동역","명일역","길동역"));
            }
        }

        @Nested
        @DisplayName("기존에 존재하는 상행역과 새로운 하행역이 일치할경우 새로운 구간이 추가")
        class ContextWithNewUpStationAdd {

            private Line line;
            private Section lineSection;
            private Section newSection;

            @BeforeEach
            void before(){
                lineSection = Section.builder()
                        .upStation(Station.of("강동역"))
                        .downStation(Station.of("명일역"))
                        .distance(10)
                        .build();
                line = Line.builder()
                        .name("5호선")
                        .color(LineColor.PURPLE)
                        .section(lineSection)
                        .build();
                newSection = Section.builder()
                        .upStation(Station.of("천호역"))
                        .downStation(Station.of("강동역"))
                        .distance(11)
                        .build();
            }

            @Test
            void returnStations() {
                line.addSection(newSection);
                List<Station> stations = line.getStations();
                assertAll(
                        () -> assertThat(stations).hasSize(3),
                        () -> assertThat(stations.stream().map(Station::getName).collect(Collectors.toList()))
                                .contains("천호역","강동역","명일역"));
            }
        }

        @Nested
        @DisplayName("기존에 존재하는 하행역과 새로운 하행역이 일치할경우 새로운 구간이 기존구간에 포함")
        class ContextWithNewDownStationContains {

            private Line line;
            private Section lineSection;
            private Section newSection;

            @BeforeEach
            void before(){
                lineSection = Section.builder()
                        .upStation(Station.of("강동역"))
                        .downStation(Station.of("명일역"))
                        .distance(10)
                        .build();
                line = Line.builder()
                        .name("5호선")
                        .color(LineColor.PURPLE)
                        .section(lineSection)
                        .build();
                newSection = Section.builder()
                        .upStation(Station.of("길동역"))
                        .downStation(Station.of("명일역"))
                        .distance(3)
                        .build();
            }

            @Test
            void returnStations() {
                line.addSection(newSection);

                List<Station> stations = line.getStations();
                assertAll(
                        () -> assertThat(stations).hasSize(3),
                        () -> assertThat(stations.stream().map(Station::getName).collect(Collectors.toList()))
                                .contains("강동역","명일역","길동역"));
            }
        }

        @Nested
        @DisplayName("기존에 존재하는 상행역과 새로운 상행역이 일치할경우 새로운 구간이 기존구간에 포함")
        class ContextWithNewDownStationAdd {

            private Line line;
            private Section lineSection;
            private Section newSection;

            @BeforeEach
            void before(){
                lineSection = Section.builder()
                        .upStation(Station.of("강동역"))
                        .downStation(Station.of("명일역"))
                        .distance(10)
                        .build();
                line = Line.builder()
                        .name("5호선")
                        .color(LineColor.PURPLE)
                        .section(lineSection)
                        .build();
                newSection = Section.builder()
                        .upStation(Station.of("명일역"))
                        .downStation(Station.of("고덕역"))
                        .distance(3)
                        .build();
            }

            @Test
            void returnStations() {
                line.addSection(newSection);

                List<Station> stations = line.getStations();
                assertAll(
                        () -> assertThat(stations).hasSize(3),
                        () -> assertThat(stations.stream().map(Station::getName).collect(Collectors.toList()))
                                .contains("강동역","명일역","고덕역"));
            }
        }

    }

}
