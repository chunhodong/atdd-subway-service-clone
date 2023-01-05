package chunhodong.subway.path.ui;


import chunhodong.subway.auth.domain.AuthenticationPrincipal;
import chunhodong.subway.auth.domain.LoginMember;
import chunhodong.subway.path.application.PathService;
import chunhodong.subway.path.dto.PathResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PathController {
    private PathService pathService;

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findShortestPath(@AuthenticationPrincipal LoginMember loginMember,
                                                         @RequestParam long sourceId, @RequestParam long targetId) {
        return ResponseEntity.ok(pathService.findShortestPath(sourceId, targetId, loginMember.getAge()));
    }
}
