package org.sopt.pawkey.backendapi.facade.route;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.command.GetRouteGeometryCommand;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.result.GetRouteGeometryResult;
import org.sopt.pawkey.backendapi.domain.routes.application.facade.query.GetRouteGeometryFacade;
import org.sopt.pawkey.backendapi.domain.routes.application.service.RouteService;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.sopt.pawkey.backendapi.fixtures.RouteFixture;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class GetRouteGeometryFacadeTest {

    @Mock
    private RouteService routeService;

    @Mock
    private UserService userService;

    @InjectMocks
    private GetRouteGeometryFacade getRouteGeometryFacade;

    @Test
    void 산책_경로_좌표_조회_유즈케이스_정상() {
        Long userId = 1L;
        Long routeId = 10L;

        UserEntity user = mock(UserEntity.class);
        RouteEntity route = RouteFixture.createRouteForGeometry(user);

        given(userService.findById(userId)).willReturn(user);
        given(routeService.getRouteById(routeId)).willReturn(route);

        GetRouteGeometryResult result =
                getRouteGeometryFacade.execute(userId, new GetRouteGeometryCommand(routeId));

        assertThat(result).isNotNull();
        assertThat(result.geometryDto()).isNotNull();
    }
}


