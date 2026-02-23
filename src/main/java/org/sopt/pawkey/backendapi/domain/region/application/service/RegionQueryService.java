package org.sopt.pawkey.backendapi.domain.region.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.region.application.dto.command.GetRegionListCommand;
import org.sopt.pawkey.backendapi.domain.region.domain.RegionQueryRepository;
import org.sopt.pawkey.backendapi.domain.region.exception.RegionBusinessException;
import org.sopt.pawkey.backendapi.domain.region.exception.RegionErrorCode;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.sopt.pawkey.backendapi.domain.user.domain.repository.UserRepository;
import org.sopt.pawkey.backendapi.domain.user.exception.UserBusinessException;
import org.sopt.pawkey.backendapi.domain.user.exception.UserErrorCode;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegionQueryService {

	private final RegionQueryRepository regionQueryRepository;
	private final UserRepository userRepository;


	public List<RegionEntity> findAllGusWithDongs() {
		return regionQueryRepository.findAllGusWithDongs();
	}

	public RegionEntity getCurrentRegion(Long userId) {
		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND));

		RegionEntity region = user.getRegion();

		if (region == null) {
			throw new RegionBusinessException(RegionErrorCode.REGION_NOT_FOUND);
		}

		return region;
	}
}
