package org.sopt.pawkey.backendapi.domain.pet.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.auth.annotation.UserId;
import org.sopt.pawkey.backendapi.domain.pet.api.dto.request.UpdatePetRequestDto;
import org.sopt.pawkey.backendapi.domain.pet.api.dto.response.PetProfileResponseDto;
import org.sopt.pawkey.backendapi.domain.pet.application.facade.PetCommandFacade;
import org.sopt.pawkey.backendapi.domain.pet.application.facade.PetQueryFacade;
import org.sopt.pawkey.backendapi.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PREFIX + "/pets")
public class PetController {

	private final PetCommandFacade petCommandFacade;
	private final PetQueryFacade petQueryFacade;

	@Operation(summary = "반려견 프로필 조회", description = "반려견의 정보를 조회합니다.", tags = {"Users"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자 없음"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@GetMapping("/me/pets")
	public ResponseEntity<ApiResponse<List<PetProfileResponseDto>>> getMyPets(
		@Parameter(hidden = true) @UserId Long userId
	) {
		List<PetProfileResponseDto> petDtos = petQueryFacade.getUserPets(userId);
		return ResponseEntity.ok(ApiResponse.success(petDtos));
	}

	@PatchMapping("/{petId}")
	@Operation(summary = "반려견 정보 수정", description = "반려견의 프로필 정보를 수정합니다.", tags = {"Users"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "수정 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "해당 반려견에 대한 수정 권한이 없음"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "반려견 정보를 찾을 수 없음")
	})
	public ResponseEntity<ApiResponse<Void>> updatePetInfo(
		@Parameter(hidden = true) @UserId Long userId,
		@PathVariable Long petId,
		@Valid @RequestBody UpdatePetRequestDto requestDto
	) {
		petCommandFacade.updatePetInfo(userId, petId, requestDto.toCommand());
		return ResponseEntity.ok(ApiResponse.success());
	}

}
