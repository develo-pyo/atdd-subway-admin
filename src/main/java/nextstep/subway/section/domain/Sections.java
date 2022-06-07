package nextstep.subway.section.domain;

import nextstep.subway.section.exception.SectionDuplicationException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
    private List<Section> sections = new ArrayList<>();

    public void addSection(Section section) {
        validateDuplication(section);
        sections.add(section);
    }

    private void validateDuplication(Section section) {
        if (sections.stream().anyMatch(section::matchAllStations)) {
            throw new SectionDuplicationException();
        }
    }
}
