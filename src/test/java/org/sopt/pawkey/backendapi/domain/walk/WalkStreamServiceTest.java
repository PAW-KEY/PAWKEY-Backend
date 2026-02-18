package org.sopt.pawkey.backendapi.domain.walk;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.dto.command.AppendWalkPointCommand;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.dto.command.StartWalkCommand;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.dto.result.StartWalkResult;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.service.WalkStreamService;
import org.sopt.pawkey.backendapi.domain.routes.walk.domain.model.WalkSession;
import org.sopt.pawkey.backendapi.domain.routes.walk.domain.repository.WalkSessionRepository;
import org.sopt.pawkey.backendapi.domain.routes.walk.exception.WalkBusinessException;


@ExtendWith(MockitoExtension.class)
class WalkStreamServiceTest {

	@Mock
	private WalkSessionRepository redisRepository;

	@InjectMocks
	private WalkStreamService walkStreamService;

	@Test
	void 이미_산책중이면_start_실패() {
		// given
		Long userId = 1L;
		given(redisRepository.existsActiveSession(userId)).willReturn(true);

		// when & then
		assertThatThrownBy(() ->
			walkStreamService.start(new StartWalkCommand(userId, "ios"))
		).isInstanceOf(WalkBusinessException.class);
	}

	@Test
	void 다른_유저가_좌표추가하면_예외() {
		// given
		WalkSession session = mock(WalkSession.class);
		given(session.getUserId()).willReturn(1L);
		given(redisRepository.loadSession("route-1")).willReturn(session);

		AppendWalkPointCommand command =
			new AppendWalkPointCommand(2L, "route-1", 37.0, 127.0, 123L);

		// when & then
		assertThatThrownBy(() -> walkStreamService.appendPoint(command))
			.isInstanceOf(WalkBusinessException.class);
	}

	@Test
	void 정상적인_산책시작은_세션생성_성공() {
		// given
		given(redisRepository.existsActiveSession(1L)).willReturn(false);
		given(redisRepository.createSession(any(), any(), anyLong())).willReturn("route-1");

		// when
		StartWalkResult result = walkStreamService.start(new StartWalkCommand(1L, "ios"));

		// then
		assertThat(result.routeId()).isEqualTo("route-1");
	}
}

