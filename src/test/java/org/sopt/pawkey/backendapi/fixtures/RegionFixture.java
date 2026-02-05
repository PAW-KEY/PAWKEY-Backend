package org.sopt.pawkey.backendapi.fixtures;

import org.sopt.pawkey.backendapi.domain.region.domain.model.RegionType;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;

public class RegionFixture {
	public static RegionEntity createRegion() {
		return RegionEntity.builder()
			.regionName("신정동")
			.latitude(37.5193)
			.longitude(126.8576)
			.regionType(RegionType.DONG)
			.build();
	}
}
