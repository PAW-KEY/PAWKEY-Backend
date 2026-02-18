package org.sopt.pawkey.backendapi.facade.walk;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.dto.command.AppendWalkPointCommand;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.dto.command.EndWalkCommand;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.dto.command.StartWalkCommand;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.facade.WalkStreamFacade;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.service.WalkStreamService;

@ExtendWith(MockitoExtension.class)
class WalkStreamFacadeTest {

	@Mock
	private WalkStreamService walkStreamService;

	@InjectMocks
	private WalkStreamFacade walkStreamFacade;

	@Test
	void start_위임_정상() {
		StartWalkCommand command = new StartWalkCommand(1L, "ios");

		walkStreamFacade.start(command);

		then(walkStreamService).should().start(command);
	}

	@Test
	void appendPoint_위임_정상() {
		AppendWalkPointCommand command =
			new AppendWalkPointCommand(1L, "route-1", 37.0, 127.0, 123L);

		walkStreamFacade.appendPoint(command);

		then(walkStreamService).should().appendPoint(command);
	}
}

