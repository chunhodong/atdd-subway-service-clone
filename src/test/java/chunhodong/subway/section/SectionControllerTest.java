package chunhodong.subway.section;

import chunhodong.subway.AcceptanceTest;
import chunhodong.subway.line.domain.LineColor;
import chunhodong.subway.line.dto.LineRequest;
import chunhodong.subway.section.dto.SectionRequest;
import chunhodong.subway.station.dto.StationRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static chunhodong.subway.line.ui.LineControllerTest.지하철_노선_생성_요청;
import static chunhodong.subway.station.StationControllerTest.지하철_역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class SectionControllerTest extends AcceptanceTest {

    @Test
    @DisplayName("구간등록에 성공")
    public void addSection() {
        Long lineId = 지하철_노선_생성_요청(new LineRequest("5호선", LineColor.PURPLE)).jsonPath().getLong("id");
        Long upStationId = 지하철_역_생성_요청(new StationRequest("강동역")).jsonPath().getLong("id");
        Long downStationId = 지하철_역_생성_요청(new StationRequest("길동역")).jsonPath().getLong("id");

        ExtractableResponse<Response> response = 구간_등록_요청(new SectionRequest(lineId,upStationId,downStationId,10));

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public ExtractableResponse<Response> 구간_등록_요청(SectionRequest request) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .body(request)
                .post("/sections")
                .then()
                .log().all()
                .extract();
    }
}
