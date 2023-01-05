package chunhodong.subway.station.ui;

import chunhodong.subway.station.application.StationService;
import chunhodong.subway.station.dto.StationRequest;
import chunhodong.subway.station.dto.StationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/stations")
@RequiredArgsConstructor
public class StationController {
    private final StationService stationService;

    @PostMapping
    public ResponseEntity createStation(@RequestBody StationRequest stationRequest) {
        StationResponse station = stationService.createStation(stationRequest);
        return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(station);
    }

    @GetMapping
    public ResponseEntity<List<StationResponse>> findStations() {
        return ResponseEntity.ok(stationService.findStations());
    }
}
