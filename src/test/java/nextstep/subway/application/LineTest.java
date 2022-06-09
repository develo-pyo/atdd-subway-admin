package nextstep.subway.application;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineTest {
    private Line line;
    private Station addedStation;
    private Station downStation;
    private Station upStation;

    @BeforeEach
    public void setUp() {
        upStation = new Station(1L, "상행");
        downStation = new Station(2L, "하행");
        addedStation = new Station( 3L, "새로운 지하철역");
        line = new Line("1호선", "bg-blue-300", 10, upStation, downStation);
    }

    @Test
    public void 상행_구간_연장() {
        //when
        line.addSection( 10, addedStation, upStation);

        //then
        assertThat(line.getAllDistinctStationsOrderByAscending().stream().map(Station::getName).collect(Collectors.toList())).containsExactly("새로운 "
                + "지하철역", "상행", "하행");
    }

    @Test
    public void 하행_구간_연장() {
        //when
        line.addSection(10, downStation, addedStation);

        //then
        assertThat(line.getAllDistinctStationsOrderByAscending().stream().map(Station::getName).collect(Collectors.toList())).containsExactly("상행", "하행", "새로운 지하철역");
    }

    @Test
    public void 상행_하행_사이_구간_추가() {
        //when
        line.addSection(8, upStation, addedStation);

        //then
        assertThat(line.getAllDistinctStationsOrderByAscending().stream().map(Station::getName).collect(Collectors.toList())).containsExactly("상행", "새로운 지하철역", "하행");
    }

    @Test
    public void 상행_하행_사이_구간_연속_추가() {
        //given
        상행_하행_사이_구간_추가();

        //when
        line.addSection(1, addedStation, new Station(4L, "새로추가된 역에 또 하나 더 추가함"));

        //then
        assertThat(line.getAllDistinctStationsOrderByAscending().stream().map(Station::getName).collect(Collectors.toList())).containsExactly("상행", "새로운 지하철역", "새로추가된 역에 또 하나 더 추가함", "하행");

        //when
        line.addSection( 7, upStation, new Station(5L,  "더 추가하기"));

        //then
        assertThat(line.getAllDistinctStationsOrderByAscending().stream().map(Station::getName).collect(Collectors.toList())).containsExactly("상행", "더 추가하기", "새로운 지하철역", "새로추가된 역에 또 하나 더 추가함", "하행");
    }

    @Test
    public void 상행_하행_사이_구간_연속_삭제() {
        //given
        상행_하행_사이_구간_연속_추가();

        //when
        line.deleteSection(addedStation);

        //then
        assertThat(line.getAllDistinctStationsOrderByAscending().stream().map(Station::getName).collect(Collectors.toList())).containsExactly("상행", "더 추가하기", "새로추가된 역에 또 하나 더 추가함", "하행");

        //when
        line.deleteSection(upStation);

        //then
        assertThat(line.getAllDistinctStationsOrderByAscending().stream().map(Station::getName).collect(Collectors.toList())).containsExactly("더 추가하기", "새로추가된 역에 또 하나 더 추가함", "하행");

        //when
        line.deleteSection(downStation);

        //then
        assertThat(line.getAllDistinctStationsOrderByAscending().stream().map(Station::getName).collect(Collectors.toList())).containsExactly("더 추가하기", "새로추가된 역에 또 하나 더 추가함");
    }

    @Test
    public void 상행_하행_사이_구간_삭제() {
        //given
        상행_하행_사이_구간_추가();

        //when
        line.deleteSection(addedStation);

        //then
        assertThat(line.getAllDistinctStationsOrderByAscending().stream().map(Station::getName)
                .collect(Collectors.toList())).containsExactly("상행", "하행");
    }

    @Test
    public void 상행_구간_삭제() {
        //given
        상행_하행_사이_구간_추가();

        //when
        line.deleteSection(upStation);

        //then
        assertThat(line.getAllDistinctStationsOrderByAscending().stream().map(Station::getName)
                .collect(Collectors.toList())).containsExactly("새로운 지하철역", "하행");
    }

    @Test
    public void 하행_구간_삭제() {
        //given
        상행_하행_사이_구간_추가();

        //when
        line.deleteSection(downStation);

        //then
        assertThat(line.getAllDistinctStationsOrderByAscending().stream().map(Station::getName)
                .collect(Collectors.toList())).containsExactly("상행", "새로운 지하철역");
    }

    @Test
    public void 사이_구간_추가시_범위_벗어나는_경우_구간_추가가_불가능하다() {
        //then
        assertThatThrownBy(() -> line.addSection( 10, upStation, addedStation)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 구간_추가시_같은_역_구간은_추가가_불가능하다() {
        //then
        assertThatThrownBy(() -> line.addSection( 5, addedStation, addedStation)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 구간_추가시_이미_존재하는_구간은_구간_추가가_불가능하다() {
        //then
        assertThatThrownBy(() -> line.addSection(  5, upStation, downStation)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 사이_구간_삭제시_구간이_하나인_경우_삭제가_불가능하다() {
        //then
        assertThatThrownBy(() -> line.deleteSection(upStation)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 구간에_포함되있지_않은_역은_삭제가_불가능하다() {
        //then
        assertThatThrownBy(() -> line.deleteSection(addedStation)).isInstanceOf(IllegalArgumentException.class);
    }
}
