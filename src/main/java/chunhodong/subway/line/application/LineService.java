package chunhodong.subway.line.application;

import chunhodong.subway.line.dto.LineRequest;
import chunhodong.subway.line.dto.LineResponse;
import chunhodong.subway.line.persistence.LineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;

    public LineResponse createLine(LineRequest lineRequest){
        return LineResponse.of(lineRepository.save(lineRequest.toLine()));
    }
}
