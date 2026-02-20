package org.sopt.pawkey.backendapi.facade.route;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.sopt.pawkey.backendapi.domain.routes.application.dto.command.GetRouteSummaryCommand;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.result.GetRouteSummaryResult;
import org.sopt.pawkey.backendapi.domain.routes.application.facade.query.GetRouteSummaryFacade;
import org.sopt.pawkey.backendapi.domain.routes.application.service.RouteService;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.sopt.pawkey.backendapi.fixtures.RouteFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class GetRouteSummaryFacadeTest {

    @Mock
    private RouteService routeService;

    @Mock
    private UserService userService;

    @InjectMocks
    private GetRouteSummaryFacade getRouteSummaryFacade;

    @Test
    void 산책_요약정보_조회_유즈케이스_정상() {
        // given
        Long userId = 1L;
        Long routeId = 10L;

        UserEntity user = mock(UserEntity.class);
        RouteEntity route = RouteFixture.createRouteForSummary(user);

        given(userService.findById(userId)).willReturn(user);
        given(routeService.getRouteById(routeId)).willReturn(route);

        // when
        GetRouteSummaryResult result =
                getRouteSummaryFacade.execute(userId, new GetRouteSummaryCommand(routeId));

        // then
        assertThat(result).isNotNull();
        assertThat(result.routeId()).isEqualTo(10L);
        assertThat(result.locationText()).isEqualTo("강남구 역삼동");
        assertThat(result.metaTagTexts())
                .containsExactlyInAnyOrder(
                        "2.2km",
                        "30분",
                        "12345걸음"
                );
    }
}


