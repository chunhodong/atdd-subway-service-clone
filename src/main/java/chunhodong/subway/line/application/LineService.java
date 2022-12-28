package chunhodong.subway.line.application;

import chunhodong.subway.line.domain.Line;
import chunhodong.subway.line.dto.LineRequest;
import chunhodong.subway.line.dto.LineResponse;
import chunhodong.subway.line.exception.LineException;
import chunhodong.subway.line.exception.LineExceptionCode;
import chunhodong.subway.line.persistence.LineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;

    @Transactional
    public LineResponse createLine(LineRequest lineRequest) {
        return LineResponse.of(lineRepository.save(lineRequest.toLine()));
    }

    @Transactional
    public void modifyLine(Long lineId, LineRequest lineRequest) {
        lineRepository.findById(lineId).orElseThrow(() -> new LineException(LineExceptionCode.NONE_EXISTS_LINE));
        lineRepository.save(lineRequest.toLine());
    }

    public LineResponse findLine(Long lineId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new LineException(LineExceptionCode.NONE_EXISTS_LINE));
        return LineResponse.of(line);
    }

    public List<LineResponse> findLines() {
        return lineRepository.findAll()
                .stream()
                .map(LineResponse::of).collect(Collectors.toList());
    }

    public void validateLine(Long lineId){
        lineRepository.findById(lineId)
                .orElseThrow(() -> new LineException(LineExceptionCode.NONE_EXISTS_LINE));
    }
}
