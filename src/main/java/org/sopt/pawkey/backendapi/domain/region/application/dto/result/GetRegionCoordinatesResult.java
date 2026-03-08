package org.sopt.pawkey.backendapi.domain.region.application.dto.result;

import java.util.Map;

import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;

import lombok.Builder;

@Builder
public record GetRegionCoordinatesResult(
	Long regionId,
	String regionName,
	Map<String, Object> geometry
) {
	public static GetRegionCoordinatesResult from(RegionEntity region) {

		return GetRegionCoordinatesResult.builder()
				.regionId(region.getRegionId())
				.regionName(region.getFullRegionName())
				.geometry(region.getGeoJson())
				.build();
	}

}
