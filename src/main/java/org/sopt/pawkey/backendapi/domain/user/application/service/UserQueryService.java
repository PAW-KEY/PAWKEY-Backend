package org.sopt.pawkey.backendapi.domain.user.application.service;

import org.sopt.pawkey.backendapi.domain.user.api.dto.response.UserInfoResponseDto;
import org.sopt.pawkey.backendapi.domain.user.domain.repository.UserRepository;
import org.sopt.pawkey.backendapi.domain.user.exception.UserBusinessException;
import org.sopt.pawkey.backendapi.domain.user.exception.UserErrorCode;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserQueryService {

	private final UserRepository userRepository;

	public UserInfoResponseDto getUserInfo(Long userId) {

		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND));
		return UserInfoResponseDto.from(user);
	}

	public UserEntity getUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND));
	}
}
