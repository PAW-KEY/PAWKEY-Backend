package org.sopt.pawkey.backendapi.domain.routes.walktest;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import org.sopt.pawkey.backendapi.domain.routes.walktest.dto.WalkPointRequest;
import org.sopt.pawkey.backendapi.domain.routes.walktest.dto.WalkStartRequest;
import org.sopt.pawkey.backendapi.domain.routes.walktest.dto.WalkStartResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(API_PREFIX + "/routes/stream")
@RequiredArgsConstructor
public class WalkStreamController {

	private final WalkStreamService walkStreamService;

	@PostMapping("/start")
	public ResponseEntity<WalkStartResponse> start(@RequestBody WalkStartRequest req) {
		WalkStartResponse response = walkStreamService.startSession(req);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/point")
	public ResponseEntity<Void> point(@RequestBody WalkPointRequest req) {
		walkStreamService.appendPoint(req);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/end")
	public ResponseEntity<Void> end(@RequestParam String routeId) {
		walkStreamService.endSession(routeId);
		return ResponseEntity.ok().build();
	}

}
