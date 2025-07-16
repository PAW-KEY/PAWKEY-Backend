package org.sopt.pawkey.backendapi.domain.region.application.service;

import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;

public interface RegionService {
	RegionEntity getRegionByIdOrThrow(Long regionId);

	RegionEntity getDongTypeRegionByIdOrThrow(Long regionId);
}
