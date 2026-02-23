package org.sopt.pawkey.backendapi.domain.region.infra.persistence;

import java.util.List;
import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.region.domain.RegionQueryRepository;
import org.sopt.pawkey.backendapi.domain.region.domain.model.RegionType;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.QRegionEntity;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RegionQueryRepositoryImpl implements RegionQueryRepository {
	private final SpringDataRegionRepository springDataRegionRepository;
	private final JPAQueryFactory queryFactory;



	@Override
	public List<RegionEntity> findAllGusWithDongs() {
		QRegionEntity regionEntity = QRegionEntity.regionEntity;
		QRegionEntity childRegion = new QRegionEntity("childRegion");
		return queryFactory
				.selectFrom(regionEntity)
				.where(regionEntity.regionType.eq(RegionType.GU))
				.leftJoin(regionEntity.childrenRegionList, childRegion).fetchJoin()
				.fetch();
	}
}
