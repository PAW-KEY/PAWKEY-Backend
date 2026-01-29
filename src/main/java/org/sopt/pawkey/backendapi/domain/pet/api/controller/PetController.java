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
import org.sopt.pawkey.backendapi.domain.pet.api.dto.response.BreedListResponseDto;
import org.sopt.pawkey.backendapi.domain.pet.application.facade.PetFacade;
import org.sopt.pawkey.backendapi.domain.pet.application.service.PetQueryService;
import org.sopt.pawkey.backendapi.global.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
	public ResponseEntity<ApiResponse<PetProfileResponseDto>> getMyPet(
		@Parameter(hidden = true) @UserId Long userId
	) {
		PetProfileResponseDto response = petQueryFacade.getUserPet(userId);
		return ResponseEntity.ok(ApiResponse.success(response));
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

	private final PetFacade petFacade;

	@Operation(summary = "견종 옵션 리스트 조회", description = "온보딩 과정에서 선택 가능한 전체 견종 리스트를 가나다순으로 조회합니다.", tags = {"Users"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "견종 리스트 조회 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json"))
	})
	@GetMapping("/breeds")
	public ResponseEntity<ApiResponse<BreedListResponseDto>> getBreeds() {
		BreedListResponseDto response = petFacade.getBreedList();

		return ResponseEntity.status(HttpStatus.OK)
			.body(ApiResponse.success(response));
	}
}
