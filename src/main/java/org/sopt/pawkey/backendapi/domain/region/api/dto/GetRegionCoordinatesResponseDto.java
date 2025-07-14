package org.sopt.pawkey.backendapi.domain.region.api.dto;

import java.util.Map;

import org.sopt.pawkey.backendapi.domain.region.application.dto.result.GetRegionCoordinatesResult;

import lombok.Builder;

@Builder
public record GetRegionCoordinatesResponseDto(
	String regionName,
	String preRegionName,
	Map<String, Object> geometryDto
) {
	public static GetRegionCoordinatesResponseDto from(GetRegionCoordinatesResult result) {
		return GetRegionCoordinatesResponseDto.builder()
			.preRegionName(result.preRegionName())
			.regionName(result.regionName())
			.geometryDto(result.geometryDto())
			.build();
	}
}
