package org.sopt.pawkey.backendapi.domain.user.application.service;

import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.SocialAccountEntity;
import org.sopt.pawkey.backendapi.domain.user.api.dto.response.UserInfoResponseDto;
import org.sopt.pawkey.backendapi.domain.user.domain.repository.SocialAccountRepository;
import org.sopt.pawkey.backendapi.domain.user.domain.repository.UserRepository;
import org.sopt.pawkey.backendapi.domain.user.exception.UserBusinessException;
import org.sopt.pawkey.backendapi.domain.user.exception.UserErrorCode;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {

	private final UserRepository userRepository;
	private final SocialAccountRepository socialAccountRepository;

	public UserInfoResponseDto getUserInfo(Long userId) {

		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND));
<<<<<<< HEAD

		String email = socialAccountRepository.findByUser_UserId(userId)
			.map(SocialAccountEntity::getPrimaryEmail)
			.orElse("");

		return UserInfoResponseDto.of(user, email);
=======
		return UserInfoResponseDto.from(user);
	}
>>>>>>> origin/feat/#202

	public UserEntity getUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND));
	}
}
