package org.sopt.pawkey.backendapi.domain.region.application.service;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.region.domain.RegionRepository;
import org.sopt.pawkey.backendapi.domain.region.domain.model.RegionType;
import org.sopt.pawkey.backendapi.domain.region.exception.RegionBusinessException;
import org.sopt.pawkey.backendapi.domain.region.exception.RegionErrorCode;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegionService {

	private final RegionRepository regionRepository;
	
	public RegionEntity getDongTypeRegionByIdOrThrow(Long regionId) {
		RegionEntity region = findRegion(regionId)
			.orElseThrow(() -> new RegionBusinessException(RegionErrorCode.REGION_NOT_FOUND));

		if (!region.getRegionType().equals(RegionType.DONG)) {
			throw new RegionBusinessException(RegionErrorCode.REGION_TYPE_NOT_DONG);
		}

		return region;
	}

	public RegionEntity getRegionByIdOrThrow(Long regionId) {

		return findRegion(regionId)
			.orElseThrow(() -> new RegionBusinessException(RegionErrorCode.REGION_NOT_FOUND));
	}

	private Optional<RegionEntity> findRegion(Long regionId) {
		return regionRepository.getById(regionId);
	}
}

