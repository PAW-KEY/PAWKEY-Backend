package org.sopt.pawkey.backendapi.domain.route;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.command.SaveRouteCommand;
import org.sopt.pawkey.backendapi.domain.routes.application.service.RouteService;
import org.sopt.pawkey.backendapi.domain.routes.domain.repository.RouteRepository;
import org.sopt.pawkey.backendapi.domain.routes.exception.RouteBusinessException;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.routes.walk.domain.model.WalkSession;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.sopt.pawkey.backendapi.fixtures.RouteFixture;

@ExtendWith(MockitoExtension.class)
class RouteServiceTest {

	@Mock
	private RouteRepository routeRepository;

	@InjectMocks
	private RouteService routeService;

	@Test
	void 세션으로부터_루트를_생성한다() {
		// given
		UserEntity user = mock(UserEntity.class);
		RegionEntity region = mock(RegionEntity.class);
		given(user.getRegion()).willReturn(region);

		WalkSession session = RouteFixture.createValidSession("route-1", 1L);
		SaveRouteCommand command = RouteFixture.createSaveRouteCommand(1L, "route-1");

		given(routeRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));

		// when
		RouteEntity route = routeService.saveRouteFromSession(user, command, session);

		// then
		assertThat(route.getCoordinates()).isNotNull();
		assertThat(route.getDistance()).isEqualTo(2200);
	}

	@Test
	void 세션이_유효하지_않으면_예외() {
		WalkSession session = mock(WalkSession.class);
		given(session.isValid()).willReturn(false);

		assertThatThrownBy(() ->
			routeService.saveRouteFromSession(
				mock(UserEntity.class),
				RouteFixture.createSaveRouteCommand(1L, "route-1"),
				session
			)
		).isInstanceOf(RouteBusinessException.class);
	}
}