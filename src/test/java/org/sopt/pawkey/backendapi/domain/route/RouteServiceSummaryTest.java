package org.sopt.pawkey.backendapi.domain.route;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.sopt.pawkey.backendapi.domain.routes.application.service.RouteService;
import org.sopt.pawkey.backendapi.domain.routes.domain.repository.RouteRepository;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.sopt.pawkey.backendapi.fixtures.RouteFixture;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RouteServiceSummaryTest {

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private RouteService routeService;

    @Test
    void 산책_요약정보_조회_성공() {
        Long routeId = 10L;

        UserEntity user = mock(UserEntity.class);
        given(user.getRegion()).willReturn(mock(RegionEntity.class));

        RouteEntity route = RouteFixture.createRoute(user);
        given(routeRepository.getRouteByRouteId(routeId))
                .willReturn(Optional.of(route));

        RouteEntity result = routeService.getRouteById(routeId);

        assertThat(result.getDistance()).isEqualTo(2200);
        assertThat(result.getDuration()).isEqualTo(1200);
        assertThat(result.getStepCount()).isEqualTo(1500);
    }
}
