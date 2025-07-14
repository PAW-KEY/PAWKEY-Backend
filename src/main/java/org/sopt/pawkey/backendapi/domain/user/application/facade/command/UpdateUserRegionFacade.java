package org.sopt.pawkey.backendapi.domain.user.application.facade.command;

import org.sopt.pawkey.backendapi.domain.region.application.service.RegionService;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.sopt.pawkey.backendapi.domain.user.application.dto.request.UpdateUserRegionCommand;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class UpdateUserRegionFacade {

	private final RegionService regionService;
	private final UserService userService;

	public void execute(Long userId, UpdateUserRegionCommand command) {
		UserEntity user = userService.findById(userId);
		RegionEntity region = regionService.getRegionByIdOrThrow(command.regionId());
		userService.updateUserRegion(user, region);
	}
}
