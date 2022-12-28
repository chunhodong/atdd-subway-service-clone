package chunhodong.subway.section.ui;

import chunhodong.subway.section.application.SectionService;
import chunhodong.subway.section.dto.SectionRequest;
import chunhodong.subway.section.dto.SectionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sections")
public class SectionController {
    private final SectionService sectionService;

    @PostMapping
    public ResponseEntity addSection(@RequestBody SectionRequest sectionRequest) {
        SectionResponse section = sectionService.addSection(sectionRequest);
        return ResponseEntity.created(URI.create("/sections" + section.getId())).body(section);
    }
}
