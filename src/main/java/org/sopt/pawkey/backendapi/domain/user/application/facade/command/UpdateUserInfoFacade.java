package org.sopt.pawkey.backendapi.domain.user.application.facade.command;

import org.sopt.pawkey.backendapi.domain.user.application.dto.request.UpdateUserInfoCommand;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateUserInfoFacade {

	private final UserService userService;

	@Transactional
	public void execute(Long userId, UpdateUserInfoCommand command) {
		userService.updateUserInfo(userId, command);
	}
}