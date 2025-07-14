package org.sopt.pawkey.backendapi.domain.region.application.dto.result;

import java.util.Map;

import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;

import lombok.Builder;

@Builder
public record GetRegionCoordinatesResult(
	String regionName,
	String preRegionName,
	Map<String, Object> geometryDto
) {
	public static GetRegionCoordinatesResult from(String preRegionName, RegionEntity region) {

		return GetRegionCoordinatesResult.builder()
			.preRegionName(preRegionName)
			.regionName(region.getFullRegionName())
			.geometryDto(region.getGeoJson())
			.build();
	}

}
