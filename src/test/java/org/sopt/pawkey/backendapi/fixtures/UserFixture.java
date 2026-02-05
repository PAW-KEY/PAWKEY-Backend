package org.sopt.pawkey.backendapi.fixtures;

import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;

public class UserFixture {
	public static UserEntity createUser(RegionEntity region) {
		return UserEntity.builder()
			.name("테스트유저")
			.region(region)
			.build();
	}
}
