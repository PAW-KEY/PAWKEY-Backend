package org.sopt.pawkey.backendapi.domain.region.domain;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;

public interface RegionQueryRepository {
	List<RegionEntity> findDistrictByRegionNameWithChildren(String keyword);
}
