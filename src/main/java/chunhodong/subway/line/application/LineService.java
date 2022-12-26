package chunhodong.subway.line.application;

import chunhodong.subway.line.domain.Line;
import chunhodong.subway.line.dto.LineRequest;
import chunhodong.subway.line.dto.LineResponse;
import chunhodong.subway.line.exception.LineException;
import chunhodong.subway.line.exception.LineExceptionCode;
import chunhodong.subway.line.persistence.LineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;

    public LineResponse createLine(LineRequest lineRequest) {
        return LineResponse.of(lineRepository.save(lineRequest.toLine()));
    }

    public void modifyLine(Long lineId, LineRequest lineRequest) {
        lineRepository.findById(lineId).orElseThrow(() -> new LineException(LineExceptionCode.NONE_EXISTS_LINE));
        lineRepository.save(lineRequest.toLine());
    }

    public LineResponse findLine(Long lineId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new LineException(LineExceptionCode.NONE_EXISTS_LINE));
        return LineResponse.of(line);
    }
}
