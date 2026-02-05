package org.sopt.pawkey.backendapi.domain.region.infra.persistence;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.region.domain.RegionRepository;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RegionRepositoryImpl implements RegionRepository {

	private final SpringDataRegionRepository springDataRegionRepository;

	@Override
	public Optional<RegionEntity> getById(Long regionId) {
		return springDataRegionRepository.getByRegionId(regionId);
	}

	@Override
	public RegionEntity save(RegionEntity region) {
		return springDataRegionRepository.save(region);
	}
}