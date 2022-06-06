package nextstep.subway;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import nextstep.subway.dto.LineResponse;
import nextstep.subway.dto.StationResponse;
import org.springframework.http.HttpStatus;

public class AcceptanceTestFactory {
    public static ExtractableResponse<Response> 지하철_역_생성(String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);

        return RestAssuredTemplate.post("/stations", params);
    }

    public static Long 지하철_역_생성_ID_추출(String name) {
        return 지하철_역_생성(name).jsonPath().getObject("id", Long.class);
    }

    public static void 생성된_지하철_역_찾기(String... name) {
        List<String> 지하철역_목록 = 지하철_역_목록_조회();
        목록_조회_성공_확인(지하철역_목록, name);
    }

    public static void 생성된_지하철_역_찾을_수_없음(String... name) {
        List<String> 지하철역_목록 = 지하철_역_목록_조회();
        목록_조회_실패_확인(지하철역_목록, name);
    }

    public static List<String> 지하철_역_목록_조회() {
        return RestAssuredTemplate.get("/stations").jsonPath().getList("name", String.class);
    }

    public static void 지하철_역_삭제(Long id) {
        RestAssuredTemplate.deleteWithId("/stations/{id}", id);
    }

    private static Map<String, Object> 지하철_노선_정보_생성(String lineName, String upStationName, String downStationName) {
        Long 상행역_ID = 지하철_역_생성_ID_추출(upStationName);
        Long 하행역_ID = 지하철_역_생성_ID_추출(downStationName);

        Map<String, Object> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", "bg-red-600");
        params.put("upStationId", 상행역_ID);
        params.put("downStationId", 하행역_ID);
        params.put("distance", 10);

        return params;
    }

    public static ExtractableResponse<Response> 지하철_노선_생성(String lineName, String upStationName, String downStationName) {
        Map<String, Object> params = 지하철_노선_정보_생성(lineName, upStationName, downStationName);
        return RestAssuredTemplate.post("/lines", params);
    }

    public static Long 지하철_노선_생성_ID_추출(String lineName, String upStationName, String downStationName) {
        return 지하철_노선_생성(lineName, upStationName, downStationName)
                .jsonPath().getObject("id", Long.class);
    }

    public static List<String> 지하철_노선_목록_조회() {
        return RestAssuredTemplate.get("/lines").jsonPath().getList("name", String.class);
    }

    public static ExtractableResponse<Response> 지하철_노선_조회(Long id) {
        return RestAssuredTemplate.getWithId("/lines/{id}", id);
    }

    public static ExtractableResponse<Response> 지하철_노선_수정(Long id, String name, String color) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return RestAssuredTemplate.putWithId("/lines/{id}", id, params);
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(Long id) {
        return RestAssuredTemplate.deleteWithId("/lines/{id}", id);
    }

    public static void 조회_성공_확인(ExtractableResponse<Response> response, String lineName, String... stationName) {
        LineResponse lineResponse = response.jsonPath().getObject(".", LineResponse.class);
        List<String> stationNames = lineResponse.getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(lineResponse.getName()).contains(lineName),
                () -> assertThat(stationNames).contains(stationName)
        );
    }

    public static void 목록_조회_성공_확인(List<String> names, String... name) {
        assertThat(names).contains(name);
    }

    public static void 목록_조회_실패_확인(List<String> names, String... name) {
        assertThat(names).doesNotContain(name);
    }

    public static void 생성_성공_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 생성_실패_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 수정_성공_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 삭제_성공_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
