package org.sopt.pawkey.backendapi.domain.pet.api.dto.response;

import java.util.List;

public record BreedListResponseDto(
	List<BreedDto> breedList
) {
	public record BreedDto(
		Long id,
		String name
	) {
	}

	public static BreedListResponseDto from(List<BreedDto> breedList) {
		return new BreedListResponseDto(breedList);
	}
}