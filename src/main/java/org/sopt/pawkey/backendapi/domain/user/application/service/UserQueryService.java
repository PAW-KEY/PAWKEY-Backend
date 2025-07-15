package org.sopt.pawkey.backendapi.domain.user.application.service;

import org.sopt.pawkey.backendapi.domain.user.api.dto.response.UserInfoResponseDto;

public interface UserQueryService {

	UserInfoResponseDto getUserInfo(Long userId);
}
