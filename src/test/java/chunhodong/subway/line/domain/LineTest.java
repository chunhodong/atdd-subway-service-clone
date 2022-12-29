package chunhodong.subway.line.domain;

import chunhodong.subway.line.exception.LineException;
import chunhodong.subway.station.domain.Station;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    }

}
