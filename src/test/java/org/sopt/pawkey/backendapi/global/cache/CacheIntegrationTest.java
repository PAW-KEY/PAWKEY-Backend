package org.sopt.pawkey.backendapi.global.cache;

import org.junit.jupiter.api.Test;
import org.sopt.pawkey.backendapi.domain.homeWeather.application.service.HomeService;
import org.sopt.pawkey.backendapi.domain.region.domain.RegionRepository;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.sopt.pawkey.backendapi.domain.routes.application.service.RouteService;
import org.sopt.pawkey.backendapi.domain.user.domain.repository.UserRepository;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.sopt.pawkey.backendapi.fixtures.RegionFixture;
import org.sopt.pawkey.backendapi.fixtures.RouteFixture;
import org.sopt.pawkey.backendapi.fixtures.UserFixture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CacheIntegrationTest {

	@Autowired
	private HomeService homeService;
	@Autowired
	private RouteService routeService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RegionRepository regionRepository;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Test
	void 산책_저장_시_기존_캐시가_삭제되는지_검증() {

		// Given
		RegionEntity region = regionRepository.save(RegionFixture.createRegion());
		UserEntity user = userRepository.save(UserFixture.createUser(region));
		Long userId = user.getUserId();

		homeService.getHomeInfo(userId);
		String cacheKey = "homeInfo::" + userId;
		assertThat(redisTemplate.hasKey(cacheKey)).isTrue();

		// When
		routeService.saveRoute(user, RouteFixture.createRegisterCommand(), null);

		// Then
		assertThat(redisTemplate.hasKey(cacheKey)).isFalse();
	}
}