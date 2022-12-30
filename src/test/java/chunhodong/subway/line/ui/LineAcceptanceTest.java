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
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static chunhodong.subway.station.StationControllerTest.지하철_역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("Line기능")
public class LineAcceptanceTest extends AcceptanceTest {


    @DisplayName("구간을 추가한다")
    @TestFactory
    Stream<DynamicTest> addSection() {
        HashMap<String, Long> values = new HashMap<>();
        return Stream.of(
                dynamicTest("지하철 역 추가", () -> {
                    StationRequest upStationRequest = StationRequest.builder().name("강동역").build();
                    StationRequest downStationRequest = StationRequest.builder().name("길동역").build();
                    StationRequest newUpStationRequest = StationRequest.builder().name("천호역").build();

                    Long saveUpStationId = 지하철_역_생성_요청(upStationRequest).jsonPath().getLong("id");
                    Long saveDownStationId = 지하철_역_생성_요청(downStationRequest).jsonPath().getLong("id");
                    Long newUpStationId = 지하철_역_생성_요청(newUpStationRequest).jsonPath().getLong("id");

                    values.put("saveUpStationId", saveUpStationId);
                    values.put("saveDownStationId", saveDownStationId);
                    values.put("newUpStationId", newUpStationId);

                }),
                dynamicTest("지하철 노선 추가", () -> {
                    Long saveUpStationId = values.get("saveUpStationId");
                    Long saveDownStationId = values.get("saveDownStationId");

                    Long lineId = 지하철_노선_생성_요청(LineRequest.builder()
                            .name("5호선")
                            .color(LineColor.PURPLE)
                            .distance(5)
                            .upStationId(saveUpStationId)
                            .downStationId(saveDownStationId)
                            .build()).jsonPath().getLong("id");

                    values.put("lineId", lineId);
                }),
                dynamicTest("지하철 구간 추가", () -> {
                    Long lineId = values.get("lineId");
                    Long saveUpStationId = values.get("saveUpStationId");
                    Long newUpStationId = values.get("newUpStationId");

                    지하철_구간_추가_요청(lineId, new SectionRequest(newUpStationId, saveUpStationId, 10));
                }),
                dynamicTest("지하철 구간 조회", () -> {
                    Long lineId = values.get("lineId");

                    List<StationResponse> stations = 지하철_노선_조회_요청(lineId).jsonPath().getList("stations", StationResponse.class);

                    assertAll(
                            () -> assertThat(stations).hasSize(3),
                            () -> assertThat(stations.stream().map(StationResponse::getName).collect(Collectors.toList())).contains("천호역", "강동역", "길동역")
                    );
                })

        );
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
                .when().post("/lines/{lineId}/sections", lineId)
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
