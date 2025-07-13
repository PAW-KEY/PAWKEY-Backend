package org.sopt.pawkey.backendapi.domain.region.application.service.query;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.region.application.dto.command.GetRegionListCommand;
import org.sopt.pawkey.backendapi.domain.region.domain.RegionQueryRepository;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegionQueryServiceImpl implements RegionQueryService {

	private final RegionQueryRepository regionQueryRepository;

	@Override
	public List<RegionEntity> searchGusWithRegion(GetRegionListCommand command) {

		return regionQueryRepository.findDistrictByRegionNameWithChildren(command.searchKeyword());
	}
}
