package org.sopt.pawkey.backendapi.domain.region.application.service.query;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.region.application.dto.command.GetRegionListCommand;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;

public interface RegionQueryService {
	List<RegionEntity> searchGusWithRegion(GetRegionListCommand command);
}
