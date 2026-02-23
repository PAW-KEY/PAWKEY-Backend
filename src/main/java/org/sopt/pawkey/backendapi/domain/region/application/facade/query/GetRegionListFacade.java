package org.sopt.pawkey.backendapi.domain.region.application.facade.query;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.region.application.dto.command.GetRegionListCommand;
import org.sopt.pawkey.backendapi.domain.region.application.dto.result.GetRegionListResult;
import org.sopt.pawkey.backendapi.domain.region.application.service.RegionQueryService;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetRegionListFacade {

	private final RegionQueryService regionQueryService;

	public GetRegionListResult execute() {
		List<RegionEntity> GusWithDongs = regionQueryService.findAllGusWithDongs();

		return GetRegionListResult.from(GusWithDongs);
	}
}
