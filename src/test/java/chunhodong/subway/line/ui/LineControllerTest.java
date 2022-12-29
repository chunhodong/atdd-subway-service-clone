package chunhodong.subway.line.ui;

import chunhodong.subway.AcceptanceTest;
import chunhodong.subway.line.domain.LineColor;
import chunhodong.subway.line.dto.LineRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Line컨트롤러 테스트")
public class LineControllerTest extends AcceptanceTest {

    public static ExtractableResponse<Response> 지하철_노선목록_생성_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();
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
}
