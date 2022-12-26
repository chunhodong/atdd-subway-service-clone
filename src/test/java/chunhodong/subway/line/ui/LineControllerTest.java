package chunhodong.subway.line.ui;

import chunhodong.subway.AcceptanceTest;
import chunhodong.subway.line.domain.LineColor;
import chunhodong.subway.line.dto.LineRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class LineControllerTest extends AcceptanceTest {

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        LineRequest lineRequest = new LineRequest("신분당선", LineColor.BLUE);

        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines")
                .then().log().all().extract();
    }
}
