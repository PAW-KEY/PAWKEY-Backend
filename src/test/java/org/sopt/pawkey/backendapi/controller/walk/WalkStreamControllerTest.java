package org.sopt.pawkey.backendapi.controller.walk;

import static org.hamcrest.MatcherAssert.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopt.pawkey.backendapi.domain.routes.walk.api.controller.WalkStreamController;
import org.sopt.pawkey.backendapi.domain.routes.walk.api.dto.request.WalkPointRequestDTO;
import org.sopt.pawkey.backendapi.domain.routes.walk.api.dto.request.WalkStartRequestDTO;
import org.sopt.pawkey.backendapi.domain.routes.walk.api.dto.response.WalkStartResponseDTO;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.dto.result.StartWalkResult;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.facade.WalkStreamFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class WalkStreamControllerTest {

	@Mock
	private WalkStreamFacade walkStreamFacade;

	@InjectMocks
	private WalkStreamController walkStreamController;

	@Test
	void 산책시작_정상응답() {
		Long userId = 1L;
		WalkStartRequestDTO req = new WalkStartRequestDTO("ios");

		StartWalkResult result = new StartWalkResult("route-1", System.currentTimeMillis());
		given(walkStreamFacade.start(any())).willReturn(result);

		ResponseEntity<WalkStartResponseDTO> response =
			walkStreamController.start(userId, req);

		assertThat(response.getStatusCode().value()).isEqualTo(200);
		assertThat(response.getBody()).isNotNull();
	}

	@Test
	void 좌표추가_정상응답() {
		Long userId = 1L;
		WalkPointRequestDTO req =
			new WalkPointRequestDTO("route-1", 37.0, 127.0, System.currentTimeMillis());

		ResponseEntity<Void> response =
			walkStreamController.point(userId, req);

		assertThat(response.getStatusCode().value()).isEqualTo(200);
		then(walkStreamFacade).should().appendPoint(any());
	}
}
