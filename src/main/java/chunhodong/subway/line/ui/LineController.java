package chunhodong.subway.line.ui;

import chunhodong.subway.line.application.LineService;
import chunhodong.subway.line.dto.LineRequest;
import chunhodong.subway.line.dto.LineResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

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
}
