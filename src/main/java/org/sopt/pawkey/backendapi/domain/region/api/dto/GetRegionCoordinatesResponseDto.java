package org.sopt.pawkey.backendapi.domain.region.api.dto;

import java.util.Map;

import org.sopt.pawkey.backendapi.domain.region.application.dto.result.GetRegionCoordinatesResult;

import lombok.Builder;

@Builder
public record GetRegionCoordinatesResponseDto(
		Long regionId,
		String regionName,
		Map<String, Object> geometry
) {
	public static GetRegionCoordinatesResponseDto from(GetRegionCoordinatesResult result) {
		return new GetRegionCoordinatesResponseDto(
				result.regionId(),
				result.regionName(),
				result.geometry()
		);
	}
}
