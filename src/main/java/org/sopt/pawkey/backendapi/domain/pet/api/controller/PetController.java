package org.sopt.pawkey.backendapi.domain.pet.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.pet.api.dto.response.BreedListResponseDto;
import org.sopt.pawkey.backendapi.domain.pet.api.dto.response.PetTraitCategoryListResponseDto;
import org.sopt.pawkey.backendapi.domain.pet.application.dto.response.PetTraitCategoryResult;
import org.sopt.pawkey.backendapi.domain.pet.application.facade.PetFacade;
import org.sopt.pawkey.backendapi.domain.pet.application.service.PetQueryService;
import org.sopt.pawkey.backendapi.global.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PREFIX + "/pets")
public class PetController {

	private final PetQueryService petQueryService;

	@GetMapping("/categories")
	public ResponseEntity<ApiResponse<PetTraitCategoryListResponseDto>> getPetTraitCategories() {
		List<PetTraitCategoryResult> resultList = petQueryService.getAllPetTraitCategories();
		PetTraitCategoryListResponseDto response = PetTraitCategoryListResponseDto.from(resultList);

		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
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
