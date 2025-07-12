package org.sopt.pawkey.backendapi.domain.category.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.application.service.CategoryQueryService;
import org.sopt.pawkey.backendapi.domain.category.application.service.CategoryService;
import org.sopt.pawkey.backendapi.domain.pet.api.dto.response.PetTraitCategoryListResponseDto;
import org.sopt.pawkey.backendapi.domain.pet.application.dto.response.PetTraitCategoryResult;
import org.sopt.pawkey.backendapi.domain.pet.application.service.PetQueryService;
import org.sopt.pawkey.backendapi.global.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping(API_PREFIX + "/posts/categories")
public class CategoryController {
	private final CategoryQueryService categoryQueryService;
	private final CategoryService categoryService;

	@GetMapping("/")
	public ResponseEntity<ApiResponse<PetTraitCategoryListResponseDto>> getPetTraitCategories() {
		List<PetTraitCategoryResult> resultList = petQueryService.getAllPetTraitCategories();
		PetTraitCategoryListResponseDto response = PetTraitCategoryListResponseDto.from(resultList);

		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));


}


// @RestController
// @RequiredArgsConstructor
// @RequestMapping(API_PREFIX + "/pets/traits")
// public class PetController {
//
// 	private final PetQueryService petQueryService;
//
// 	@GetMapping("/categories")
// 	public ResponseEntity<ApiResponse<PetTraitCategoryListResponseDto>> getPetTraitCategories() {
// 		List<PetTraitCategoryResult> resultList = petQueryService.getAllPetTraitCategories();
// 		PetTraitCategoryListResponseDto response = PetTraitCategoryListResponseDto.from(resultList);
//
// 		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
// 	}
// }
