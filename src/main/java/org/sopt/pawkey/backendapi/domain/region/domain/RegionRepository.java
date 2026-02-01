package org.sopt.pawkey.backendapi.domain.region.domain;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;

public interface RegionRepository {
	Optional<RegionEntity> getById(Long regionId);

	RegionEntity save(RegionEntity region);
}
