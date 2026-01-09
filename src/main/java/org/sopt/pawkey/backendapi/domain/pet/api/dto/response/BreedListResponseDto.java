package org.sopt.pawkey.backendapi.domain.pet.api.dto.response;

import java.util.List;

public record BreedListResponseDto(
	List<String> breedList
) {
	public static BreedListResponseDto from(List<String> breedList) {
		return new BreedListResponseDto(breedList);
	}
}