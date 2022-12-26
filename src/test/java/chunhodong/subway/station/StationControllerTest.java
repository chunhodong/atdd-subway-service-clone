package chunhodong.subway.station;

import chunhodong.subway.AcceptanceTest;
import chunhodong.subway.station.dto.StationRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class StationControllerTest extends AcceptanceTest {

    @DisplayName("지하철 역을 생성한다")
    @Test
    void createLine() {
        ExtractableResponse<Response> response = 지하철_역_생성_요청(new StationRequest("강동역"));

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.body().jsonPath().getString("name")).isEqualTo("강동역"));
    }

    @DisplayName("지하철 역이름이 없으면 생성실패")
    @Test
    void throwsExceptionWhenEmptyName() {
        ExtractableResponse<Response> response = 지하철_역_생성_요청(new StationRequest(null));

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철 목록을 조회한다")
    @Test
    void findStations() {
        지하철_역_생성_요청(new StationRequest("강동역"));
        지하철_역_생성_요청(new StationRequest("신답역"));
        지하철_역_생성_요청(new StationRequest("영일역"));

        ExtractableResponse<Response> response = 지하철_역_목록조회_요청();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.body().jsonPath().getList(".")).hasSize(3));
    }

    public static ExtractableResponse<Response> 지하철_역_생성_요청(StationRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_역_목록조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

}
