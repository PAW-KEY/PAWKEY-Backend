package org.sopt.pawkey.backendapi.domain.region.application.facade.query;

import org.sopt.pawkey.backendapi.domain.region.application.dto.command.GetRegionCoordinatesCommand;
import org.sopt.pawkey.backendapi.domain.region.application.dto.result.GetRegionCoordinatesResult;
import org.sopt.pawkey.backendapi.domain.region.application.service.RegionService;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetRegionCoordinatesFacade {

	private final RegionService regionService;


	public GetRegionCoordinatesResult execute(GetRegionCoordinatesCommand getRegionCoordinatesCommand) {

		RegionEntity region = regionService.getDongTypeRegionByIdOrThrow(getRegionCoordinatesCommand.regionId());

		return GetRegionCoordinatesResult.from(region);
	}
}
