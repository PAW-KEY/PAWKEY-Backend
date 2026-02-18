package org.sopt.pawkey.backendapi.facade.route;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.command.SaveRouteCommand;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.result.FinishWalkResult;
import org.sopt.pawkey.backendapi.domain.routes.application.facade.command.RouteRegisterFacade;
import org.sopt.pawkey.backendapi.domain.routes.application.service.RouteService;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.service.WalkStreamService;
import org.sopt.pawkey.backendapi.domain.routes.walk.domain.model.WalkSession;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.sopt.pawkey.backendapi.fixtures.RouteFixture;

@ExtendWith(MockitoExtension.class)
class RouteRegisterFacadeTest {

	@Mock private WalkStreamService walkStreamService;
	@Mock private RouteService routeService;
	@Mock private UserService userService;

	@InjectMocks
	private RouteRegisterFacade routeRegisterFacade;

	@Test
	void 산책종료_후_루트저장_유즈케이스_정상() {
		// given
		Long userId = 1L;
		String routeId = "route-1";

		SaveRouteCommand command = RouteFixture.createSaveRouteCommand(userId, routeId);
		WalkSession session = RouteFixture.createValidSession(routeId, userId);

		ImageEntity profileImage = mock(ImageEntity.class);
		given(profileImage.getImageUrl()).willReturn("https://test.com/profile.png");

		PetEntity pet = mock(PetEntity.class);
		given(pet.getProfileImage()).willReturn(profileImage);

		UserEntity user = mock(UserEntity.class);
		given(user.getPet()).willReturn(pet);

		RouteEntity route = RouteFixture.createRoute(user);

		given(walkStreamService.end(any())).willReturn(session);
		given(userService.findById(userId)).willReturn(user);
		given(routeService.saveRouteFromSession(user, command, session)).willReturn(route);

		// when
		FinishWalkResult result = routeRegisterFacade.finishWalk(userId, command);

		// then
		assertThat(result).isNotNull();
		then(walkStreamService).should().end(any());
		then(routeService).should().saveRouteFromSession(user, command, session);
	}
}
