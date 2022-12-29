package chunhodong.subway.line.ui;

import chunhodong.subway.line.application.LineService;
import chunhodong.subway.line.dto.LineRequest;
import chunhodong.subway.line.dto.LineResponse;
import chunhodong.subway.line.dto.SectionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
@RequiredArgsConstructor
public class LineController {

    private final LineService lineService;

    @PostMapping
    public ResponseEntity createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.createLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLine(@PathVariable("id") Long id) {
        return ResponseEntity.ok(lineService.findLine(id));
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findLines() {
        return ResponseEntity.ok(lineService.findLines());
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity addLineStation(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
        lineService.addSection(lineId, sectionRequest);
        return ResponseEntity.ok().build();
    }
}

