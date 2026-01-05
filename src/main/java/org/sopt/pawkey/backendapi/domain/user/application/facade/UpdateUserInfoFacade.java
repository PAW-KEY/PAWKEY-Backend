package org.sopt.pawkey.backendapi.domain.user.application.facade;

import org.sopt.pawkey.backendapi.domain.user.api.dto.response.UserInfoResponseDto;
import org.sopt.pawkey.backendapi.domain.user.application.dto.request.UpdateUserInfoCommand;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserQueryService;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateUserInfoFacade {

	private final UserService userService;
	private final UserQueryService userQueryService;

	@Transactional
	public UserInfoResponseDto execute(Long userId, UpdateUserInfoCommand command) {
		userService.updateUserInfo(userId, command);
		return userQueryService.getUserInfo(userId);
	}
}