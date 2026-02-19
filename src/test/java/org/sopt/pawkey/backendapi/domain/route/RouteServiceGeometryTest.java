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
import org.sopt.pawkey.backendapi.domain.routes.application.service.RouteService;
import org.sopt.pawkey.backendapi.domain.routes.domain.repository.RouteRepository;
import org.sopt.pawkey.backendapi.domain.routes.exception.RouteBusinessException;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.sopt.pawkey.backendapi.fixtures.RouteFixture;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RouteServiceGeometryTest {

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private RouteService routeService;

    @Test
    void routeId로_루트_조회_성공() {
        Long routeId = 10L;

        RouteEntity route = RouteFixture.createRoute(mock(UserEntity.class));
        given(routeRepository.getRouteByRouteId(routeId))
                .willReturn(Optional.of(route));

        RouteEntity result = routeService.getRouteById(routeId);

        assertThat(result).isNotNull();
        assertThat(result.getCoordinates()).isNotNull();
    }

    @Test
    void routeId로_루트_조회_실패() {
        Long routeId = 999L;
        given(routeRepository.getRouteByRouteId(routeId))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> routeService.getRouteById(routeId))
                .isInstanceOf(RouteBusinessException.class);
    }
}
