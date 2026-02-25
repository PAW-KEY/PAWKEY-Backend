package org.sopt.pawkey.backendapi.domain.routes.walk.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.sopt.pawkey.backendapi.domain.auth.annotation.UserId;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.dto.command.EndWalkCommand;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.dto.result.StartWalkResult;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.facade.WalkStreamFacade;
import org.sopt.pawkey.backendapi.domain.routes.walk.api.dto.request.WalkPointRequestDTO;
import org.sopt.pawkey.backendapi.domain.routes.walk.api.dto.request.WalkStartRequestDTO;
import org.sopt.pawkey.backendapi.domain.routes.walk.api.dto.response.WalkStartResponseDTO;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.service.WalkStreamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(API_PREFIX + "/walks/stream")
@RequiredArgsConstructor
public class WalkStreamController {

	private final WalkStreamFacade walkStreamFacade;

	@PostMapping("/start")
	@Operation(summary = "산책 시작" , description = "산책 세션 시작 API", tags = {"Walk"})
	public ResponseEntity<WalkStartResponseDTO> start(@Parameter(hidden = true) @UserId Long userId, @RequestBody WalkStartRequestDTO req) {
		StartWalkResult result = walkStreamFacade.start(req.toCommand(userId));
		return ResponseEntity.ok(WalkStartResponseDTO.from(result));
	}

	@PostMapping("/point")
	@Operation(summary = "산책 스트리밍", description = "산책 중 좌표 스트리밍 API", tags = {"Walk"})
	public ResponseEntity<Void> point(@Parameter(hidden=true) @UserId Long userId, @RequestBody WalkPointRequestDTO req){
		walkStreamFacade.appendPoint(req.toCommand(userId));
		return ResponseEntity.ok().build();
	}

	// @PostMapping("/end")
	// public ResponseEntity<Void> end(@Parameter(hidden=true) @UserId Long userId, @RequestParam String routeId){
	// 	EndWalkCommand command = new EndWalkCommand(userId,routeId);
	// 	walkStreamFacade.end(command);
	// 	return ResponseEntity.ok().build();
	// }

}
