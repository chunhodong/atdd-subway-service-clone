package chunhodong.subway.line.domain;

import chunhodong.subway.line.exception.LineException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
                assertThatThrownBy(() -> new Line(null,null))
                        .isInstanceOf(LineException.class)
                        .hasMessageContaining("노선이름을 입력해야 합니다");
            }
        }

        @Nested
        @DisplayName("노선의 색깔이 없으면 예외발생")
        class ContextWithNoColor {
            @Test
            void throwsExeption() {
                assertThatThrownBy(() -> new Line("2호선",null))
                        .isInstanceOf(LineException.class)
                        .hasMessageContaining("노선색상을 입력해야 합니다");
            }
        }

        @DisplayName("노선의 이름과 컬러를 입력하면 생성에 성공")
        @Test
        void returnLine() {
            assertThat(new Line("2호선",LineColor.BLUE)).isNotNull();
        }
    }

    @Nested
    @DisplayName("이름 수정기능은 ")
    class DescribeModifyLine {

        @Nested
        @DisplayName("노선의 이름이 없으면 예외발생")
        class ContextWithNoName {
            @Test
            void throwsExeption() {
                Line line = new Line("2호선",LineColor.BLUE);

                assertThatThrownBy(() -> line.modifyName(null))
                        .isInstanceOf(LineException.class)
                        .hasMessageContaining("노선이름을 입력해야 합니다");
            }
        }

        @DisplayName("노선의 이름을 입력하면 이름수정")
        @Test
        void returnLine() {
            Line line = new Line("2호선",LineColor.BLUE);

            line.modifyName("3호선");

            assertThat(line.getName()).isEqualTo("3호선");
        }
    }
}
