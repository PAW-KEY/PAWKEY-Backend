package org.sopt.pawkey.backendapi.domain.user.application.facade.query;

import org.sopt.pawkey.backendapi.domain.user.api.dto.response.UserInfoResponseDto;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserQueryService;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryFacade {
	private final UserQueryService userQueryService;

	public UserInfoResponseDto getUserInfo(Long userId) {

		return userQueryService.getUserInfo(userId);
	}
}
