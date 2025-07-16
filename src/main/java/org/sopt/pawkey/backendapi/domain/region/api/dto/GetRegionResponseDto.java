package org.sopt.pawkey.backendapi.domain.region.api.dto;

import org.sopt.pawkey.backendapi.domain.region.application.dto.result.GetRegionResult;

public record GetRegionResponseDto(String fullRegionName) {
	public static GetRegionResponseDto from(GetRegionResult result) {
		return new GetRegionResponseDto(result.fullRegionName());
	}
}