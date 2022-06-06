package nextstep.subway.line;

import static nextstep.subway.AcceptanceTestFactory.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.List;

import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    /**
     * When: 사용자는 지하철 노선 생성을 요청한다.
     * Then: 지하철 역이 생성된다.
     * Then: 사용자는 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        ExtractableResponse<Response> 지하철_노선_생성_응답_결과 =
                지하철_노선_생성("9호선", "강남역", "신논현역");

        생성_성공_확인(지하철_노선_생성_응답_결과);
    }

    /**
     * Given: 2개의 지하철 노선이 생성되어 있다.
     * When: 사용자는 지하철 노선 목록 조회를 요청한다.
     * Then: 사용자는 2개의 지하철 노선 목록을 응답받는다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLines() {
        지하철_노선_생성("9호선", "강남역", "신논현역");
        지하철_노선_생성("신분당선", "판교역", "양재역");

        List<String> 지하철_노선_목록 = 지하철_노선_목록_조회();

        목록_조회_성공_확인(지하철_노선_목록, "9호선", "신분당선");
    }

    /**
     * Given: 지하철 노선이 생성되어 있다.
     * When: 사용자는 생성한 지하철 노선 조회를 요청한다.
     * Then: 사용자는 생성한 지하철 노선의 정보를 응답받는다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        Long 지하철_노선_ID = 지하철_노선_생성_ID_추출("9호선", "강남역", "신논현역");

        ExtractableResponse<Response> 지하철_노선_조회_결과 = 지하철_노선_조회(지하철_노선_ID);

        조회_성공_확인(지하철_노선_조회_결과, "9호선", "강남역", "신논현역");
    }

    /**
     * Given: 지하철 노선이 생성되어 있다.
     * When: 사용자는 생성한 지하철 노선 수정을 요청한다.
     * Then: 사용자는 지하철 노선 조회 시 수정된 정보를 응답받는다.
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        Long 지하철_노선_ID = 지하철_노선_생성_ID_추출("9호선", "강남역", "신논현역");

        ExtractableResponse<Response> 지하철_노선_수정_결과 = 지하철_노선_수정(지하철_노선_ID, "8호선", "bg-blue-600");

        수정_성공_확인(지하철_노선_수정_결과);
    }

    /**
     * Given: 지하철역이 생성되어 있다.
     * When: 사용자는 지하철 노선 삭제를 요청한다.
     * Then: 해당 지하철 노선 정보는 삭제된다.
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        Long 지하철_노선_ID = 지하철_노선_생성_ID_추출("9호선", "강남역", "신논현역");

        ExtractableResponse<Response> 지하철_노선_삭제_응답_결과 = 지하철_노선_삭제_요청(지하철_노선_ID);

        삭제_성공_확인(지하철_노선_삭제_응답_결과);
    }
}
