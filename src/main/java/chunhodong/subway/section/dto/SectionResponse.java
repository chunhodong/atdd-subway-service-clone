package chunhodong.subway.section.dto;

import chunhodong.subway.section.domain.Section;
import lombok.Getter;

@Getter
public class SectionResponse {

    private Long id;

    public SectionResponse(Section section) {
        this.id = section.getId();
    }

    public static SectionResponse of(Section section) {
        return new SectionResponse(section);
    }
}
