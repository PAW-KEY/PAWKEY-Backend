package org.sopt.pawkey.backendapi.controller.route;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopt.pawkey.backendapi.domain.routes.api.controller.RouteController;
import org.sopt.pawkey.backendapi.domain.routes.api.dto.request.SaveRouteRequestDTO;
import org.sopt.pawkey.backendapi.domain.routes.api.dto.response.SaveRouteResponseDTO;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.result.FinishWalkResult;
import org.sopt.pawkey.backendapi.domain.routes.application.facade.command.RouteRegisterFacade;
import org.sopt.pawkey.backendapi.global.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class RouteControllerTest {

	@Mock
	private RouteRegisterFacade routeRegisterFacade;

	@InjectMocks
	private RouteController routeController;

	@Test
	void 산책종료_API_응답_정상() {
		Long userId = 1L;
		String routeId = "route-1";

		SaveRouteRequestDTO req = mock(SaveRouteRequestDTO.class);
		FinishWalkResult result = mock(FinishWalkResult.class);

		given(routeRegisterFacade.finishWalk(eq(userId), any())).willReturn(result);

		ResponseEntity<ApiResponse<SaveRouteResponseDTO>> response =
			routeController.finishWalk(userId, routeId, req);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
	}
}

