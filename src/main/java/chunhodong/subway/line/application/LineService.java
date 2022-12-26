package chunhodong.subway.line.application;

import chunhodong.subway.line.persistence.LineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LineService {
    private final LineRepository lineRepository;

    public void createLine(){

    }
}
