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
import static org.junit.jupiter.api.Assertions.assertAll;

public class LineControllerTest extends AcceptanceTest {

    @DisplayName("지하철 노선을 생성한다")
    @Test
    void createLine() {
        LineRequest lineRequest = new LineRequest("신분당선", LineColor.BLUE);

        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("지하철 노선이름이 없으면 노선추가에 실패한다")
    @Test
    void throwsExceptionWithNoName() {
        LineRequest lineRequest = new LineRequest("", LineColor.BLUE);

        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.body().asString()).isEqualTo("노선이름을 입력해야 합니다"));

    }

    @DisplayName("지하철 노선컬러가 없으면 노선추가에 실패한다")
    @Test
    void throwsExceptionWithNoColor() {
        LineRequest lineRequest = new LineRequest("2호선", null);

        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.body().asString()).isEqualTo("노선색상을 입력해야 합니다"));

    }

    @DisplayName("지하철 노선 수정에 성공한다")
    @Test
    void modifyLine() {
        String lineId = 지하철_노선_생성_요청(new LineRequest("2호선", LineColor.GREEN))
                .jsonPath().get("id").toString();
        지하철_노선_수정_요청(lineId, new LineRequest(Long.valueOf(lineId),"3호선", LineColor.GREEN));

        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineId);

        assertThat(response.body().jsonPath().getString("name")).isEqualTo("3호선");
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(String lineId, LineRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().put("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(String lineId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();
    }

    @DisplayName("지하철 노선을 수정한다")
    @Test
    void updateLine() {
        LineRequest lineRequest = new LineRequest("신분당선", LineColor.BLUE);

        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("지하철 노선을 조회한다")
    @Test
    void getLine() {
        LineRequest lineRequest = new LineRequest("신분당선", LineColor.BLUE);

        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("지하철 노선목록을 조회한다")
    @Test
    void getLines() {
        LineRequest lineRequest = new LineRequest("신분당선", LineColor.BLUE);

        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}
