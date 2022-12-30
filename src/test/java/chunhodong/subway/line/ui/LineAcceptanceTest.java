package chunhodong.subway.line.ui;

import chunhodong.subway.AcceptanceTest;
import chunhodong.subway.line.domain.Line;
import chunhodong.subway.line.domain.LineColor;
import chunhodong.subway.line.dto.LineRequest;
import chunhodong.subway.line.dto.SectionRequest;
import chunhodong.subway.line.exception.LineException;
import chunhodong.subway.station.dto.StationRequest;
import chunhodong.subway.station.dto.StationResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static chunhodong.subway.station.StationControllerTest.지하철_역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("Line 인수테스트")
public class LineAcceptanceTest extends AcceptanceTest {


    @Nested
    @DisplayName("구간추가에서 ")
    class DescribeAddSection {
        @DisplayName("새로 추가하는 구간의 하행선이 기존과 일치하는 경우 기존구간이 나누어진다")
        @TestFactory
        Stream<DynamicTest> addWithSameDownSection() {
            HashMap<String, Long> values = new HashMap<>();
            return Stream.of(
                    dynamicTest("지하철 역 추가", () -> {
                        Long 강동역 = 지하철_역_생성_요청(StationRequest.builder().name("강동역").build())
                                .jsonPath().getLong("id");
                        Long 길동역 = 지하철_역_생성_요청(StationRequest.builder().name("길동역").build())
                                .jsonPath().getLong("id");
                        Long 천호역 = 지하철_역_생성_요청(StationRequest.builder().name("천호역").build())
                                .jsonPath().getLong("id");
                        values.put("천호역", 천호역);
                        values.put("강동역", 강동역);
                        values.put("길동역", 길동역);

                    }),
                    dynamicTest("지하철 노선 추가", () -> {
                        Long 천호역 = values.get("천호역");
                        Long 길동역 = values.get("길동역");

                        Long lineId = 지하철_노선_생성_요청(LineRequest.builder()
                                .name("5호선")
                                .color(LineColor.PURPLE)
                                .distance(20)
                                .upStationId(천호역)
                                .downStationId(길동역)
                                .build()).jsonPath().getLong("id");

                        values.put("lineId", lineId);
                    }),
                    dynamicTest("지하철 구간 추가", () -> {
                        Long lineId = values.get("lineId");
                        Long 강동역 = values.get("강동역");
                        Long 길동역 = values.get("길동역");

                        지하철_구간_추가_요청(lineId, new SectionRequest(강동역, 길동역, 10));
                    }),
                    dynamicTest("지하철 구간 조회", () -> {
                        Long lineId = values.get("lineId");

                        List<StationResponse> stations = 지하철_노선_조회_요청(lineId).jsonPath().getList("stations", StationResponse.class);

                        assertAll(
                                () -> assertThat(stations).hasSize(3),
                                () -> assertThat(stations.stream().map(StationResponse::getName).collect(Collectors.toList())).containsExactly("천호역", "강동역", "길동역")
                        );
                    })

            );
        }
        @DisplayName("새로 추가하는 구간의 상행선이 기존과 일치하는 경우 기존구간이 나누어진다")
        @TestFactory
        Stream<DynamicTest> addWithSameUpStation() {
            HashMap<String, Long> values = new HashMap<>();
            return Stream.of(
                    dynamicTest("지하철 역 추가", () -> {
                        Long 강동역 = 지하철_역_생성_요청(StationRequest.builder().name("강동역").build())
                                .jsonPath().getLong("id");
                        Long 길동역 = 지하철_역_생성_요청(StationRequest.builder().name("길동역").build())
                                .jsonPath().getLong("id");
                        Long 천호역 = 지하철_역_생성_요청(StationRequest.builder().name("천호역").build())
                                .jsonPath().getLong("id");
                        values.put("천호역", 천호역);
                        values.put("강동역", 강동역);
                        values.put("길동역", 길동역);

                    }),
                    dynamicTest("지하철 노선 추가", () -> {
                        Long 천호역 = values.get("천호역");
                        Long 길동역 = values.get("길동역");

                        Long lineId = 지하철_노선_생성_요청(LineRequest.builder()
                                .name("5호선")
                                .color(LineColor.PURPLE)
                                .distance(20)
                                .upStationId(천호역)
                                .downStationId(길동역)
                                .build()).jsonPath().getLong("id");

                        values.put("lineId", lineId);
                    }),
                    dynamicTest("지하철 구간 추가", () -> {
                        Long lineId = values.get("lineId");
                        Long 천호역 = values.get("천호역");
                        Long 강동역 = values.get("강동역");

                        지하철_구간_추가_요청(lineId, new SectionRequest(천호역, 강동역, 10));
                    }),
                    dynamicTest("지하철 구간 조회", () -> {
                        Long lineId = values.get("lineId");

                        List<StationResponse> stations = 지하철_노선_조회_요청(lineId).jsonPath().getList("stations", StationResponse.class);

                        assertAll(
                                () -> assertThat(stations).hasSize(3),
                                () -> assertThat(stations.stream().map(StationResponse::getName).collect(Collectors.toList())).containsExactly("천호역", "강동역", "길동역")
                        );
                    })

            );
        }
        @DisplayName("새로 추가하는 구간이 가장 앞쪽인 경우 새롭게 추가")
        @TestFactory
        Stream<DynamicTest> addFirstSection() {
            HashMap<String, Long> values = new HashMap<>();
            return Stream.of(
                    dynamicTest("지하철 역 추가", () -> {
                        Long 강동역 = 지하철_역_생성_요청(StationRequest.builder().name("강동역").build())
                                .jsonPath().getLong("id");
                        Long 길동역 = 지하철_역_생성_요청(StationRequest.builder().name("길동역").build())
                                .jsonPath().getLong("id");
                        Long 천호역 = 지하철_역_생성_요청(StationRequest.builder().name("천호역").build())
                                .jsonPath().getLong("id");
                        values.put("천호역", 천호역);
                        values.put("강동역", 강동역);
                        values.put("길동역", 길동역);

                    }),
                    dynamicTest("지하철 노선 추가", () -> {
                        Long lineId = 지하철_노선_생성_요청(LineRequest.builder()
                                .name("5호선")
                                .color(LineColor.PURPLE)
                                .distance(20)
                                .upStationId(values.get("강동역"))
                                .downStationId(values.get("길동역"))
                                .build()).jsonPath().getLong("id");
                        values.put("lineId", lineId);
                    }),
                    dynamicTest("지하철 구간 추가", () -> {
                        Long lineId = values.get("lineId");
                        Long 천호역 = values.get("천호역");
                        Long 강동역 = values.get("강동역");
                        지하철_구간_추가_요청(lineId, new SectionRequest(천호역, 강동역, 10));
                    }),
                    dynamicTest("지하철 구간 조회", () -> {
                        Long lineId = values.get("lineId");

                        List<StationResponse> stations = 지하철_노선_조회_요청(lineId).jsonPath().getList("stations", StationResponse.class);

                        assertAll(
                                () -> assertThat(stations).hasSize(3),
                                () -> assertThat(stations.stream().map(StationResponse::getName).collect(Collectors.toList())).containsExactly("천호역", "강동역", "길동역")
                        );
                    })

            );
        }
        @DisplayName("새로 추가하는 구간이 가장 뒤쪽인 경우 새롭게 추가")
        @TestFactory
        Stream<DynamicTest> addLastSection() {
            HashMap<String, Long> values = new HashMap<>();
            return Stream.of(
                    dynamicTest("지하철 역 추가", () -> {
                        Long 강동역 = 지하철_역_생성_요청(StationRequest.builder().name("강동역").build())
                                .jsonPath().getLong("id");
                        Long 길동역 = 지하철_역_생성_요청(StationRequest.builder().name("길동역").build())
                                .jsonPath().getLong("id");
                        Long 천호역 = 지하철_역_생성_요청(StationRequest.builder().name("천호역").build())
                                .jsonPath().getLong("id");
                        values.put("천호역", 천호역);
                        values.put("강동역", 강동역);
                        values.put("길동역", 길동역);

                    }),
                    dynamicTest("지하철 노선 추가", () -> {
                        Long lineId = 지하철_노선_생성_요청(LineRequest.builder()
                                .name("5호선")
                                .color(LineColor.PURPLE)
                                .distance(20)
                                .upStationId(values.get("천호역"))
                                .downStationId(values.get("강동역"))
                                .build()).jsonPath().getLong("id");
                        values.put("lineId", lineId);
                    }),
                    dynamicTest("지하철 구간 추가", () -> {
                        Long lineId = values.get("lineId");
                        Long 강동역 = values.get("강동역");
                        Long 길동역 = values.get("길동역");
                        지하철_구간_추가_요청(lineId, new SectionRequest(강동역, 길동역, 10));
                    }),
                    dynamicTest("지하철 구간 조회", () -> {
                        Long lineId = values.get("lineId");

                        List<StationResponse> stations = 지하철_노선_조회_요청(lineId).jsonPath().getList("stations", StationResponse.class);

                        assertAll(
                                () -> assertThat(stations).hasSize(3),
                                () -> assertThat(stations.stream().map(StationResponse::getName).collect(Collectors.toList())).containsExactly("천호역", "강동역", "길동역")
                        );
                    })

            );
        }
    }

    @Nested
    @DisplayName("구간제거에서 ")
    class DescribeRemoveSection{
        @DisplayName("중간 겹치는 역을 제거하는경우 기존 구간에서 남아있는 역들이 하나로 합쳐진다")
        @TestFactory
        Stream<DynamicTest> removeMiddleSection() {
            HashMap<String, Long> values = new HashMap<>();
            return Stream.of(
                    dynamicTest("지하철 역 추가", () -> {
                        Long 천호역 = 지하철_역_생성_요청(StationRequest.builder().name("천호역").build())
                                .jsonPath().getLong("id");
                        Long 강동역 = 지하철_역_생성_요청(StationRequest.builder().name("강동역").build())
                                .jsonPath().getLong("id");
                        Long 길동역 = 지하철_역_생성_요청(StationRequest.builder().name("길동역").build())
                                .jsonPath().getLong("id");
                        values.put("천호역", 천호역);
                        values.put("강동역", 강동역);
                        values.put("길동역", 길동역);

                    }),
                    dynamicTest("지하철 노선 추가", () -> {
                        Long 천호역 = values.get("천호역");
                        Long 강동역 = values.get("강동역");

                        Long lineId = 지하철_노선_생성_요청(LineRequest.builder()
                                .name("5호선")
                                .color(LineColor.PURPLE)
                                .distance(5)
                                .upStationId(천호역)
                                .downStationId(강동역)
                                .build()).jsonPath().getLong("id");

                        values.put("lineId", lineId);
                    }),
                    dynamicTest("지하철 구간 추가", () -> {
                        Long lineId = values.get("lineId");
                        Long 강동역 = values.get("강동역");
                        Long 길동역 = values.get("길동역");

                        지하철_구간_추가_요청(lineId, new SectionRequest(강동역, 길동역, 10));
                    }),
                    dynamicTest("지하철 구간 제거", () -> {
                        Long lineId = values.get("lineId");
                        Long 강동역 = values.get("강동역");
                        지하철_구간_제거_요청(lineId,강동역);
                    }),
                    dynamicTest("지하철 구간 조회", () -> {
                        Long lineId = values.get("lineId");

                        List<StationResponse> stations = 지하철_노선_조회_요청(lineId).jsonPath().getList("stations", StationResponse.class);

                        assertAll(
                                () -> assertThat(stations).hasSize(2),
                                () -> assertThat(stations.stream().map(StationResponse::getName).collect(Collectors.toList())).containsExactly("천호역", "길동역")
                        );
                    })

            );
        }

        @DisplayName("겹치는 역이 없는 상태에서 첫번쨰 구간인경우 구간이 제거")
        @TestFactory
        Stream<DynamicTest> removeFirstSection() {
            HashMap<String, Long> values = new HashMap<>();
            return Stream.of(
                    dynamicTest("지하철 역 추가", () -> {
                        Long 천호역 = 지하철_역_생성_요청(StationRequest.builder().name("천호역").build())
                                .jsonPath().getLong("id");
                        Long 강동역 = 지하철_역_생성_요청(StationRequest.builder().name("강동역").build())
                                .jsonPath().getLong("id");
                        Long 길동역 = 지하철_역_생성_요청(StationRequest.builder().name("길동역").build())
                                .jsonPath().getLong("id");
                        values.put("천호역", 천호역);
                        values.put("강동역", 강동역);
                        values.put("길동역", 길동역);

                    }),
                    dynamicTest("지하철 노선 추가", () -> {
                        Long 천호역 = values.get("천호역");
                        Long 강동역 = values.get("강동역");

                        Long lineId = 지하철_노선_생성_요청(LineRequest.builder()
                                .name("5호선")
                                .color(LineColor.PURPLE)
                                .distance(5)
                                .upStationId(천호역)
                                .downStationId(강동역)
                                .build()).jsonPath().getLong("id");

                        values.put("lineId", lineId);
                    }),
                    dynamicTest("지하철 구간 추가", () -> {
                        Long lineId = values.get("lineId");
                        Long 강동역 = values.get("강동역");
                        Long 길동역 = values.get("길동역");

                        지하철_구간_추가_요청(lineId, new SectionRequest(강동역, 길동역, 10));
                    }),
                    dynamicTest("지하철 구간 제거", () -> {
                        Long lineId = values.get("lineId");
                        Long 천호역 = values.get("천호역");
                        지하철_구간_제거_요청(lineId,천호역);
                    }),
                    dynamicTest("지하철 구간 조회", () -> {
                        Long lineId = values.get("lineId");

                        List<StationResponse> stations = 지하철_노선_조회_요청(lineId).jsonPath().getList("stations", StationResponse.class);

                        assertAll(
                                () -> assertThat(stations).hasSize(2),
                                () -> assertThat(stations.stream().map(StationResponse::getName).collect(Collectors.toList())).containsExactly("강동역", "길동역")
                        );
                    })

            );
        }

        @DisplayName("겹치는 역이 없는 상태에서 마지막 구간인경우 구간이 제거")
        @TestFactory
        Stream<DynamicTest> removeLastSection() {
            HashMap<String, Long> values = new HashMap<>();
            return Stream.of(
                    dynamicTest("지하철 역 추가", () -> {
                        Long 천호역 = 지하철_역_생성_요청(StationRequest.builder().name("천호역").build())
                                .jsonPath().getLong("id");
                        Long 강동역 = 지하철_역_생성_요청(StationRequest.builder().name("강동역").build())
                                .jsonPath().getLong("id");
                        Long 길동역 = 지하철_역_생성_요청(StationRequest.builder().name("길동역").build())
                                .jsonPath().getLong("id");
                        values.put("천호역", 천호역);
                        values.put("강동역", 강동역);
                        values.put("길동역", 길동역);

                    }),
                    dynamicTest("지하철 노선 추가", () -> {
                        Long 천호역 = values.get("천호역");
                        Long 강동역 = values.get("강동역");

                        Long lineId = 지하철_노선_생성_요청(LineRequest.builder()
                                .name("5호선")
                                .color(LineColor.PURPLE)
                                .distance(5)
                                .upStationId(천호역)
                                .downStationId(강동역)
                                .build()).jsonPath().getLong("id");

                        values.put("lineId", lineId);
                    }),
                    dynamicTest("지하철 구간 추가", () -> {
                        Long lineId = values.get("lineId");
                        Long 강동역 = values.get("강동역");
                        Long 길동역 = values.get("길동역");

                        지하철_구간_추가_요청(lineId, new SectionRequest(강동역, 길동역, 10));
                    }),
                    dynamicTest("지하철 구간 제거", () -> {
                        Long lineId = values.get("lineId");
                        Long 길동역 = values.get("길동역");
                        지하철_구간_제거_요청(lineId,길동역);
                    }),
                    dynamicTest("지하철 구간 조회", () -> {
                        Long lineId = values.get("lineId");

                        List<StationResponse> stations = 지하철_노선_조회_요청(lineId).jsonPath().getList("stations", StationResponse.class);

                        assertAll(
                                () -> assertThat(stations).hasSize(2),
                                () -> assertThat(stations.stream().map(StationResponse::getName).collect(Collectors.toList())).containsExactly("천호역", "강동역")
                        );
                    })

            );
        }
    }

    public static ExtractableResponse<Response> 지하철_구간_제거_요청(Long lineId,Long stationId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("stationId",stationId)
                .when().delete("/lines/{lineId}/sections", lineId)
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
