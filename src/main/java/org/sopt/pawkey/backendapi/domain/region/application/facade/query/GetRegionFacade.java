package org.sopt.pawkey.backendapi.domain.region.application.facade.query;

import org.sopt.pawkey.backendapi.domain.region.application.dto.command.GetRegionCommand;
import org.sopt.pawkey.backendapi.domain.region.application.dto.result.GetRegionResult;
import org.sopt.pawkey.backendapi.domain.region.application.service.query.RegionQueryService;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetRegionFacade {
	private final RegionQueryService regionQueryService;

	public GetRegionResult execute(Long userId){
		RegionEntity currentRegion = regionQueryService.getCurrentRegion(userId);

		return new GetRegionResult(currentRegion.getRegionId(),currentRegion.getFullRegionName());
	}

}
