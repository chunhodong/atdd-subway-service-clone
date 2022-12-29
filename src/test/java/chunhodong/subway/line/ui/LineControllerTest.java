package chunhodong.subway.line.ui;

import chunhodong.subway.AcceptanceTest;
import chunhodong.subway.line.domain.LineColor;
import chunhodong.subway.line.dto.LineRequest;
import chunhodong.subway.line.dto.SectionRequest;
import chunhodong.subway.station.dto.StationRequest;
import chunhodong.subway.station.dto.StationResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static chunhodong.subway.station.StationControllerTest.지하철_역_생성_요청;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Line컨트롤러 테스트")
public class LineControllerTest extends AcceptanceTest {

    @Test
    @DisplayName("새로운 구간이 가장 앞쪽에 추가")
    void addNewSection() {
        StationRequest upStationRequest = StationRequest.builder()
                .name("강동역")
                .build();
        StationRequest downStationRequest = StationRequest.builder()
                .name("길동역")
                .build();

        Long upStationId = 지하철_역_생성_요청(upStationRequest).jsonPath().getLong("id");
        Long downStationId = 지하철_역_생성_요청(downStationRequest).jsonPath().getLong("id");
        Long lineId = 지하철_노선_생성_요청(LineRequest.builder()
                .name("5호선")
                .color(LineColor.PURPLE)
                .distance(5)
                .upStationId(upStationId)
                .downStationId(downStationId)
                .build()).jsonPath().getLong("id");

        StationRequest newUpStationRequest = StationRequest.builder()
                .name("천호역")
                .build();
        Long newUpStationId = 지하철_역_생성_요청(newUpStationRequest).jsonPath().getLong("id");
        지하철_구간_추가_요청(lineId,new SectionRequest(newUpStationId,upStationId,10));
        List<StationResponse> stations = 지하철_노선_조회_요청(lineId).jsonPath().getList("stations");

    }

    public static ExtractableResponse<Response> 지하철_노선목록_조회_요청() {
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

    public static ExtractableResponse<Response> 지하철_구간_추가_요청(Long lineId, SectionRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(Long lineId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();
    }
}
